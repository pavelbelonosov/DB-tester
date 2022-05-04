package service;

import dao.*;
import domain.Film;
import util.PropertiesHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private ResultTableHtml page;
    private List<List<Film>> results;
    private ResultServer rs;
    private String htmlLine;
    private String clientID;


    public ClientHandler(Socket socket, ResultTableHtml page,
                         ResultServer rs, List<List<Film>> results, String id) {
        this.clientSocket = socket;
        this.page = page;
        this.results = results;
        this.rs = rs;
        htmlLine = "";
        this.clientID = id;
    }

    public void run() {
        try (Scanner reader = new Scanner(clientSocket.getInputStream());
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream())) {
            if (reader.hasNextLine()) {
                String request = reader.nextLine();
                System.out.println(request);
                if (parseGetReq(request, writer)) {
                    writer.flush();
                    return;
                }
                parseForm(request);
            }
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getIndexPage());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean parseGetReq(String request, PrintWriter writer) {

        if (request.contains("/resultTable" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(page.getContent());
            return true;
        }
        if (request.contains("/sqlite1" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite", 0));
            return true;
        }
        if (request.contains("/s1queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite Queries", 1));
            return true;
        }
        if (request.contains("/sqlite2" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite", 6));
            return true;
        }
        if (request.contains("/s2queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite Queries", 7));
            return true;
        }
        if (request.contains("/sqlite3" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite", 12));
            return true;
        }
        if (request.contains("/s3queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite Queries", 13));
            return true;
        }
        if (request.contains("/postgres1" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres", 2));
            return true;
        }
        if (request.contains("/p1queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres Queries", 3));
            return true;
        }
        if (request.contains("/postgres2" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres", 8));
            return true;
        }
        if (request.contains("/p2queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres Queries", 9));
            return true;
        }
        if (request.contains("/postgres3" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres", 14));
            return true;
        }
        if (request.contains("/p3queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres Queries", 15));
            return true;
        }
        if (request.contains("/mongo1" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo", 4));
            return true;
        }
        if (request.contains("/m1queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo Queries", 5));
            return true;
        }
        if (request.contains("/mongo2" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo", 10));
            return true;
        }
        if (request.contains("/m2queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo Queries", 11));
            return true;
        }
        if (request.contains("/mongo3" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo", 16));
            return true;
        }
        if (request.contains("/m3queries" + clientID)) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo Queries", 17));
            return true;
        }
        return false;
    }

    private Boolean parseForm(String request) {
        String parts[] = request.split("&");
        if (parts.length > 1) {
            String parts2[] = parts[0].split("=");
            long rows = Long.parseLong(parts2[1]);

            String parts3[] = parts[1].split("=");
            long queries = Long.parseLong(parts3[1].replace(" HTTP/1.1", ""));
            if (rows <= 1000000 && queries <= 1000) {// depends on java heap space//heroku limits 512 dyno memory
                instantiateTestService(rows, queries);
                htmlLine = "<td><a href=\"resultTable" + clientID + "\" target=\"_blank\">Result Table </a></td>\n";
                return true;
            } else {
                htmlLine = "<p><b>Pss, buddy, I'm not a supercomputer, ok? Just slow down</b></p>";
            }
        }
        return false;
    }

    private void instantiateTestService(long rows, long numQueries) {
        PropertiesHandler prop = new PropertiesHandler();
        SqliteFilmDao sqlite = new SqliteFilmDao(prop.getProperty("folder"), prop.getProperty("sqlite"));
        PostgresFilmDao postgres = new PostgresFilmDao();
        MongoFilmDao mongo = new MongoFilmDao();
        TestService service = new TestService(sqlite, postgres, mongo, rs, rows, numQueries, clientID);
        service.test();
    }

    private String getResultPage(String name, int index) {
        StringBuilder html = new StringBuilder();
        html.append("<html lang=\"en\">" +
                "<head><title>" + name + "</title>\n" +
                "<style>\n" +
                "h1 {\n" +
                "  color: black;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "p {\n" +
                "  font-family: verdana;\n" +
                "  font-size: 12px;\n" +
                "}\n" +
                "</style>" +
                "</head>" +
                "<body><h1>" + name + "</h1>");
        results.get(index).stream().forEach(film -> html.append("<p>" + film + "</p>"));
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }

    private String getIndexPage() {
        StringBuilder html = new StringBuilder();
        html.append("<html lang=\"en\">\n" +
                "<head>\n" +
                "<title>" + "Index" + "</title>\n" +
                "<style>\n" +
                "h1 {\n" +
                "  color: black;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "p {\n" +
                "  font-family: verdana;\n" +
                "  font-size: 15px;\n" +
                "}\n" +
                "input, textarea, select, button {\n" +
                "  width : 150px;\n" +
                "  margin: 5;\n" +
                "  box-sizing: border-box;\n" +
                "}" +
                "</style>" +
                "</head>" +
                "<body><p>>Hey, I'm DBtester.</p>" +
                "<p>>Wanna some calculationz?</p>" +
                "<p>>Type number of rows and number of queries.</p>" +
                "<p>>And we'll see what i got for you.</p>");
        html.append("<form action=\"/\">\n" +
                "  <label for=\"numberOfRows\">Rows/documents:</label><br>\n" +
                "  <input type=\"text\" id=\"numberOfRows\" name=\"numberOfRows\" value=\"\"><br>\n" +
                "  <label for=\"numberOfQueries\">Queries:</label><br>\n" +
                "  <input type=\"text\" id=\"numberOfQueries\" name=\"numberOfQueries\" value=\"\"><br>\n" +
                "  <input type=\"submit\" value=\"LET'S GO\">\n" +
                "</form>");
        html.append(htmlLine);
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }
}

