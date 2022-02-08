
import java.io.File;
import java.sql.*;
import java.util.Random;


public class Testi {
    public static void main(String[] args) {
        String dbURL = "C://db/elokuvat.db";
        String createTable = "CREATE TABLE Elokuvat (id INTEGER PRIMARY KEY, nimi TEXT, vuosi INTEGER)";
        Testi testi = new Testi();

        try {
            testi.createTable(dbURL, createTable);
        } catch (SQLException e) {
            System.out.println("Error: cannot create table" + e.getMessage());
        }
//1
        System.out.println("1 test without enhanced indexing");
        try {
            testi.insertMillionRows(dbURL);
            testi.thousandQueries(dbURL);
        } catch (SQLException e) {
            System.out.println("Error: test 1 processing failed: " + e.getMessage());
        }
        System.out.println("Database size is "+testi.getDatabaseSize(dbURL)+" mb\n");

        System.out.print("Clearing table... ");
        try {
            testi.clearAllRows(dbURL);
        } catch (SQLException e) {
            System.out.println("FAIL");
        } finally {
            System.out.println("");
        }
//2
        System.out.println("2 test, using enhanced indexing before adding rows");
        try {
            testi.addEnhancedIndex(dbURL);
            testi.insertMillionRows(dbURL);
            testi.thousandQueries(dbURL);
            testi.dropEnhancedIndex(dbURL);
        } catch (SQLException e) {
            System.out.println("Error: 2 test processing failed: " + e.getMessage());
        }
        System.out.println("Database size is "+testi.getDatabaseSize(dbURL)+" mb\n");

        System.out.print("Clearing table... ");
        try {
            testi.clearAllRows(dbURL);
        } catch (SQLException e) {
            System.out.println("FAIL");
        } finally {
            System.out.println("");
        }
//3
        System.out.println("3 test, using enhanced indexing before executing queries");
        try {
            testi.insertMillionRows(dbURL);
            testi.addEnhancedIndex(dbURL);
            testi.thousandQueries(dbURL);
            testi.dropEnhancedIndex(dbURL);
        } catch (SQLException e) {
            System.out.println("Error: 3 test processing failed: " + e.getMessage());
        }
        System.out.println("Database size is "+testi.getDatabaseSize(dbURL)+" mb\n");
    }

    void insertMillionRows(String dbURL) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        Statement s = db.createStatement();
        db.setAutoCommit(false);
        PreparedStatement p = db.prepareStatement("INSERT INTO Elokuvat (nimi,vuosi) VALUES (?,?)");
        long startTimeInsert = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Testi testi = new Testi();
            p.setString(1, testi.randomString(8));
            p.setInt(2, testi.randomYear(1900, 2000));
            p.addBatch();
        }
        p.executeBatch();
        db.commit();
        long endTimeInsert = System.currentTimeMillis();
        db.setAutoCommit(true);
        System.out.println("total time taken to insert the batch of one million inserts = " + (endTimeInsert - startTimeInsert) / 1000.0 + " s");
        db.close();
    }

    void thousandQueries(String dbURL) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        Statement s = db.createStatement();
        PreparedStatement p = db.prepareStatement("SELECT COUNT(DISTINCT nimi) FROM Elokuvat WHERE vuosi=?");
        Testi testi = new Testi();
        long startTimeQueries = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            p.setInt(1, testi.randomYear(1900, 2000));
            p.executeQuery();
        }
        long endTimeQueries = System.currentTimeMillis();
        System.out.println("total time taken to query one thousand select counts = " + (endTimeQueries - startTimeQueries) / 1000.0 + " s");
        db.close();
    }

    void addEnhancedIndex(String dbURL) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        Statement s = db.createStatement();
        s.execute("CREATE INDEX idx_vuosi ON Elokuvat (vuosi)");
        db.close();
    }

    void dropEnhancedIndex(String dbURL) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        Statement s = db.createStatement();
        s.execute("DROP INDEX idx_vuosi");
        db.close();
    }

    long getDatabaseSize(String dbURL)  {
        File file = new File(dbURL);
        return (file.length() / 1024) / 1024;
    }

    void createTable(String dbURL, String table) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        Statement s = db.createStatement();
        s.execute(table);
        db.close();
    }

    void clearAllRows(String dbURL) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        PreparedStatement p = db.prepareStatement("DELETE FROM Elokuvat");
        p.executeUpdate();
        db.close();

    }

    String randomString(int length) {
        StringBuilder str = new StringBuilder();
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(s.length());
            str.append(s.charAt(index));
        }
        return str + "";
    }

    int randomYear(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}

