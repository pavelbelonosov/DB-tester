
import dao.*;
import service.TestService;
import util.PropertiesHandler;

public class MainApp {
    public static void main(String[] args) {

        PropertiesHandler prop = new PropertiesHandler();

        SqliteFilmDao sqlite = new SqliteFilmDao(prop.getProperty("folder"), prop.getProperty("sqlite"));
        PostgresFilmDao postgres = postgres = new PostgresFilmDao(prop.getProperty("folder"),
                    prop.getProperty("postgres"),
                    prop.getProperty("postgresUser"),
                    prop.getProperty("postgresPass"));
        MongoFilmDao mongo = new MongoFilmDao(prop.getProperty("mongoURI"));

        TestService testService = new TestService(sqlite, postgres, mongo, Integer.valueOf(prop.getProperty("rows/docs")));
        testService.test();
    }

}

