
import dao.*;
import service.ResultServer;
import service.TestService;
import util.PropertiesHandler;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) {

        ResultServer resultServer = new ResultServer();
        try {
            resultServer.startServer();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}

