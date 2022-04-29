package service;

import dao.Dao;
import dao.NoSqlDao;
import domain.Film;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestService {
    private ResultTableHtml page;
    private final Dao sqlite;
    private final Dao postgres;
    private final NoSqlDao mongo;
    private long rows;
    private long queries;
    private final List<Long> firstTestResults;
    private final List<Long> secondTestResult;
    private final List<Long> thirdTestResult;
    private List<List<Film>> queriesResultList;
    private ResultServer server;


    public TestService(Dao sqlite, Dao postgres, NoSqlDao mongo,
                       ResultServer server, long rows, long queries, String clientID) {
        firstTestResults = new ArrayList<>();
        secondTestResult = new ArrayList<>();
        thirdTestResult = new ArrayList<>();
        queriesResultList = new ArrayList<>();
        page = new ResultTableHtml(rows, queries, firstTestResults, secondTestResult, thirdTestResult, clientID);
        this.sqlite = sqlite;
        this.postgres = postgres;
        this.mongo = mongo;
        this.rows = rows;
        this.queries = queries;
        this.server = server;

    }


    public void test() {
        //1 test creating DB, table, inserting rows, executing queries, no enhanced indexing
        System.out.println("1 test without enhanced indexing");
        try {
            System.out.println("Sqlite");
            sqlite.createDB();
            sqlite.createTable();
            firstTestResults.add(insert(sqlite));
            queriesResultList.add(sqlite.findAll());
            firstTestResults.add(executeQueries(sqlite));
            firstTestResults.add(sqlite.getDatabaseSize());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("**********************");

        try {
            System.out.println("Postgres");
            postgres.createDB();
            postgres.createTable();
            postgres.clearAllRows();
            firstTestResults.add(insert(postgres));
            queriesResultList.add(postgres.findAll());
            firstTestResults.add(executeQueries(postgres));
            firstTestResults.add(postgres.getDatabaseSize());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("**********************");

        System.out.println("Mongo");
        mongo.connectDB();
        mongo.connectCollection();
        mongo.dropCollection();
        firstTestResults.add(insert(mongo));
        queriesResultList.add(mongo.findAll());
        firstTestResults.add(executeQueries(mongo));
        firstTestResults.add(mongo.getDatabaseSize());


        //2 test, enhanced indexing before inserting
        System.out.println("2 test, using enhanced indexing before adding rows");
        try {
            System.out.println("Sqlite");
            sqlite.clearAllRows();
            sqlite.addEnhancedIndex();
            secondTestResult.add(insert(sqlite));
            queriesResultList.add(sqlite.findAll());
            secondTestResult.add(executeQueries(sqlite));
            secondTestResult.add(sqlite.getDatabaseSize());
            sqlite.dropEnhancedIndex();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("**********************");

        try {
            System.out.println("Postgres");
            postgres.clearAllRows();
            postgres.addEnhancedIndex();
            secondTestResult.add(insert(postgres));
            queriesResultList.add(postgres.findAll());
            secondTestResult.add(executeQueries(postgres));
            secondTestResult.add(postgres.getDatabaseSize());
            postgres.dropEnhancedIndex();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("**********************");

        System.out.println("Mongo");
        mongo.connectDB();
        mongo.connectCollection();
        mongo.dropCollection();
        mongo.addEnhancedIndex();
        secondTestResult.add(insert(mongo));
        queriesResultList.add(mongo.findAll());
        secondTestResult.add(executeQueries(mongo));
        secondTestResult.add(mongo.getDatabaseSize());
        mongo.dropEnhancedIndex();

        //3 test
        System.out.println("3 test, using enhanced indexing before executing queries");
        try {
            System.out.println("Sqlite");
            sqlite.clearAllRows();
            thirdTestResult.add(insert(sqlite));
            queriesResultList.add(sqlite.findAll());
            sqlite.addEnhancedIndex();
            thirdTestResult.add(executeQueries(sqlite));
            thirdTestResult.add(sqlite.getDatabaseSize());
            sqlite.dropEnhancedIndex();
            sqlite.dropDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("**********************");

        try {
            System.out.println("Postgres");
            postgres.clearAllRows();
            thirdTestResult.add(insert(postgres));
            queriesResultList.add(postgres.findAll());
            postgres.addEnhancedIndex();
            thirdTestResult.add(executeQueries(postgres));
            thirdTestResult.add(postgres.getDatabaseSize());
            postgres.dropEnhancedIndex();
            postgres.dropDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("**********************");

        System.out.println("Mongo");
        mongo.connectDB();
        mongo.connectCollection();
        mongo.dropCollection();
        thirdTestResult.add(insert(mongo));
        queriesResultList.add(mongo.findAll());
        mongo.addEnhancedIndex();
        thirdTestResult.add(executeQueries(mongo));
        thirdTestResult.add(mongo.getDatabaseSize());
        mongo.dropEnhancedIndex();
        mongo.dropCollection();

        page.createContent();
        server.setResults(queriesResultList);
        server.setPage(page);
    }

    private long insert(Dao dao) throws SQLException {
        long startTimeInsert = System.currentTimeMillis();
        List<Film> films = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            films.add(new Film(1, randomString(8), randomYear(1900, 2022)));
        }
        dao.createMany(films);
        long endTimeInsert = System.currentTimeMillis();
        long result = endTimeInsert - startTimeInsert;
        System.out.println("total time taken to insert the batch of " + rows + " inserts = " + result / 1000 + " s");
        return result;
    }

    private long insert(NoSqlDao dao) {
        long startTimeInsert = System.currentTimeMillis();
        List<Film> films = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            films.add(new Film(1, randomString(8), randomYear(1900, 2022)));
        }
        dao.createMany(films);
        long endTimeInsert = System.currentTimeMillis();
        long result = endTimeInsert - startTimeInsert;
        System.out.println("total time taken to insert the batch of " + rows + " inserts = " + result / 1000 + " s");
        return result;
    }

    private long executeQueries(Dao dao) throws SQLException {
        long startTimeQueries = System.currentTimeMillis();
        List<Film> temp = new ArrayList<>();
        for (int i = 0; i < queries; i++) {
            temp.addAll(dao.findByAttribute(randomYear(1900, 2022)));
        }
        long endTimeQueries = System.currentTimeMillis();
        long result = endTimeQueries - startTimeQueries;
        System.out.println("total time taken to query " + queries + " select counts = " + (endTimeQueries - startTimeQueries) / 1000 + " s");
        //temp.stream().forEach(System.out::println);
        queriesResultList.add(temp);
        return result;
    }

    private long executeQueries(NoSqlDao dao) {
        long startTimeQueries = System.currentTimeMillis();
        List<Film> temp = new ArrayList<>();
        for (int i = 0; i < queries; i++) {
            temp.addAll(dao.findByAttribute(randomYear(1900, 2022)));
        }
        long endTimeQueries = System.currentTimeMillis();
        long result = endTimeQueries - startTimeQueries;
        System.out.println("total time taken to query " + queries + " select counts = " + (endTimeQueries - startTimeQueries) / 1000 + " s");
        //temp.stream().forEach(System.out::println);
        queriesResultList.add(temp);
        return result;
    }

    private String randomString(int length) {
        StringBuilder str = new StringBuilder();
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(s.length());
            str.append(s.charAt(index));
        }
        return str + "";
    }

    private int randomYear(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public ResultTableHtml getPage() {
        return page;
    }

    public List<List<Film>> getQueriesResultList() {
        return queriesResultList;
    }


}
