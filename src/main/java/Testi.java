
import java.sql.*;
import java.util.Random;


public class Testi {
    public static void main(String[] args) throws SQLException {
        Testi testi = new Testi();

        Connection db = DriverManager.getConnection("jdbc:sqlite:C://Users/pablo/IdeaProjects/sqllitetest/.idea/elokuvat.db");
        db.setAutoCommit(false);
        Statement s = db.createStatement();
        s.execute("CREATE TABLE Elokuvat (id INTEGER PRIMARY KEY, nimi TEXT, vuosi INTEGER)");

        System.out.println("1 test without enhanced indexing");
        testi.insertMillionRows(db);
        testi.thousandQueries(db);

        s.execute("DELETE * FROM Elokuvat");//clearing data in the table

        System.out.println("2 test, using enhanced indexing before adding rows");
        s.execute("CREATE INDEX idx_vuosi ON Elokuvat (vuosi)");
        testi.insertMillionRows(db);
        testi.thousandQueries(db);

        //s.execute("DELETE * FROM Elokuvat");//clearing data in the table

        System.out.println("3 test, using enhanced indexing before executing queries");
        testi.insertMillionRows(db);
        s.execute("CREATE INDEX idx_vuosi ON Elokuvat (vuosi)");
        testi.thousandQueries(db);
    }

    void insertMillionRows(Connection db) throws SQLException {
        PreparedStatement p = db.prepareStatement("INSERT INTO Elokuvat (nimi,vuosi) VALUES (?,?)");
        long startTimeInsert = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Testi testi = new Testi();
            p.setString(1, testi.randomString(8));
            p.setInt(2, testi.randomYear(1900, 2000));
            p.addBatch();
        }
        p.executeBatch();
        long endTimeInsert = System.currentTimeMillis();
        System.out.println("total time taken to insert the batch of inserts = " + (endTimeInsert - startTimeInsert) + " ms");
        p.close();
    }

    void thousandQueries(Connection db) throws SQLException {
        PreparedStatement p = db.prepareStatement("SELECT COUNT(DISTINCT nimi) FROM Elokuvat WHERE vuosi=?");
        Testi testi = new Testi();
        long startTimeQueries = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            p.setInt(1, testi.randomYear(1900, 2000));
            p.executeQuery();
        }
        long endTimeQueries = System.currentTimeMillis();
        System.out.println("total time taken to query all select counts = " + (endTimeQueries - startTimeQueries) + " ms\n");
        p.close();
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

