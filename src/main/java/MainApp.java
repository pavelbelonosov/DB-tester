
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
       /* PropertiesHandler prop = new PropertiesHandler();

        SqliteFilmDao sqlite = new SqliteFilmDao(prop.getProperty("folder"), prop.getProperty("sqlite"));
        PostgresFilmDao postgres = postgres = new PostgresFilmDao(prop.getProperty("folder"),
                prop.getProperty("postgres"),
                prop.getProperty("postgresUser"),
                prop.getProperty("postgresPass"));
        MongoFilmDao mongo = new MongoFilmDao(prop.getProperty("mongoURI"));

        TestService testService = new TestService(sqlite, postgres, mongo, Integer.valueOf(prop.getProperty("rows/docs")));
        testService.test();*/
    }

}

