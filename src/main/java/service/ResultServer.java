package service;

import dao.Dao;
import dao.NoSqlDao;
import domain.Film;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class ResultServer {
    private ResultTableHtml page;
    private List<List<Film>> results;
    private String clientId;

    public ResultServer() {
        this.clientId = UUID.randomUUID().toString();
    }

    public void setPage(ResultTableHtml page) {
        this.page = page;
    }

    public void setResults(List<List<Film>> results) {
        this.results = results;
    }

    public void startServer() throws IOException {
        try {
            ServerSocket server = new ServerSocket(8080);
            server.setReuseAddress(true);
            System.out.println("Server started at 8080");
            while (true) {
                Socket socket = server.accept();
                ClientHandler clientSock = new ClientHandler(socket, page, this, results, clientId);
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}