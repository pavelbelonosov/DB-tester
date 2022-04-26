package service;

import dao.MongoFilmDao;
import dao.PostgresFilmDao;
import dao.SqliteFilmDao;
import domain.Film;
import util.PropertiesHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ResultServer {
    private ResultTableHtml page;
    private List<List<Film>> results;

    public ResultServer() {

    }

    public void setPage(ResultTableHtml page) {
        this.page = page;
    }

    public void setResults(List<List<Film>> results) {
        this.results = results;
    }

    public ResultServer getInstance(){
        return this;
    }

    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(8080);
        server.setReuseAddress(true);
        System.out.println("Server started at 8080");
        while (true) {
            try  {
                Socket socket = server.accept();
               ClientHandler clientSock = new ClientHandler(socket,server,page,results, this);
                new Thread(clientSock).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
/*
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

                TestService testService = new TestService(sqlite, postgres, mongo, Integer.valueOf(parts2[0]), this);
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
                "<body> <h1>" + name + "</h1>");
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
                "<body> <h1>" + "Index" + "</h1>");
        html.append("<form action=\"/post\">\n" +
                "  <label for=\"numberOfRows\">Rows/documents:</label><br>\n" +
                "  <input type=\"text\" id=\"numberOfRows\" name=\"numberOfRows\" value=\"100\"><br>\n" +
                "  <input type=\"submit\" value=\"Submit\">\n" +
                "</form>");
        html.append("    <td><a href=\"resultTable\" target=\"_blank\">Result Table </a></td>\n");
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }*/
}
