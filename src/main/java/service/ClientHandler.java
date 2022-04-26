package service;

import dao.MongoFilmDao;
import dao.PostgresFilmDao;
import dao.SqliteFilmDao;
import domain.Film;
import util.PropertiesHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private ResultTableHtml page;
    private ServerSocket server;
    private List<List<Film>> results;
    private ResultServer rs;

    // Constructor
    public ClientHandler(Socket socket, ServerSocket server, ResultTableHtml page, List<List<Film>> results, ResultServer rs) {
        this.clientSocket = socket;
        this.page = page;
        this.server = server;
        this.results = results;
        this.rs = rs;
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
                parsePostReq(request);

            }

            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            //writer.println(page.getContent());
            writer.println(getPostPage());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean parseGetReq(String request, PrintWriter writer) {
        if (request.contains("/resultTable")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(page.getContent());
            return true;
        }
        if (request.contains("/sqlite1")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite", 0));
            return true;
        }
        if (request.contains("/s1queries")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Sqlite Queries", 1));
            return true;
        }
        if (request.contains("/postgres1")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres", 2));
            return true;
        }
        if (request.contains("/p1queries")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Postgres Queries", 3));
            return true;
        }
        if (request.contains("/mongo1")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo", 4));
            return true;
        }
        if (request.contains("/m1queries")) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("");
            writer.println(getResultPage("Mongo Queries", 5));
            return true;
        }
        return false;
    }

    private Boolean parsePostReq(String request) {
        String parts[] = request.split("=");
        if(parts.length>1){
            String parts2[] = parts[1].split(" ");
            if(parts2[0]!=null){
                PropertiesHandler prop = new PropertiesHandler();

                SqliteFilmDao sqlite = new SqliteFilmDao(prop.getProperty("folder"), prop.getProperty("sqlite"));
                PostgresFilmDao postgres = postgres = new PostgresFilmDao(prop.getProperty("folder"),
                        prop.getProperty("postgres"),
                        prop.getProperty("postgresUser"),
                        prop.getProperty("postgresPass"));
                MongoFilmDao mongo = new MongoFilmDao(prop.getProperty("mongoURI"));

                TestService testService = new TestService(sqlite, postgres, mongo, Integer.valueOf(parts2[0]),rs );
                testService.test();
                return true;
            }
        }

        return false;
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
                "</style>+" +
                "</head>" +
                "<body><h1>" + name + "</h1>");
        results.get(index).stream().forEach(film -> html.append("<p>" + film + "</p>"));
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }

    private String getPostPage() {
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
                "  font-size: 12px;\n" +
                "}\n" +
                "</style>+" +
                "</head>" +
                "<body><h1>" + "Index" + "</h1>");
        html.append("<form action=\"/\">\n" +
                "  <label for=\"numberOfRows\">Rows/documents:</label><br>\n" +
                "  <input type=\"text\" id=\"numberOfRows\" name=\"numberOfRows\" value=\"\"><br>\n" +
                "  <input type=\"submit\" value=\"Submit\">\n" +
                "</form>");
        html.append("    <td><a href=\"resultTable\" target=\"_blank\">Result Table </a></td>\n");
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }
}

