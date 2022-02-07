
import java.sql.*;
import java.util.Random;


public class Testi {
    public static void main(String[] args) throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:C://Users/pablo/IdeaProjects/sqllitetest/.idea/elokuvat.db");
        Statement s = db.createStatement();
        //s.execute("CREATE INDEX idx_vuosi ON Elokuvat (vuosi)");

        db.setAutoCommit(false);
        try {
            s.execute("CREATE TABLE Elokuvat (id INTEGER PRIMARY KEY, nimi TEXT, vuosi INTEGER)");
        } catch (SQLException e) {
            System.out.println("Table already exist");
        }

        Random rand = new Random();
        Testi testi = new Testi();
        int year = rand.nextInt(101) + 1900;
        String filmName = testi.randomString(8);

//Inserting values into table
        /*String sqlInsert = "INSERT INTO Elokuvat (nimi,vuosi) VALUES (?,?)";
        PreparedStatement p = db.prepareStatement(sqlInsert);

        long startInsert = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            int year = rand.nextInt(101) + 1900;
            String filmName = testi.randomString(8);
            p.setString(1, filmName);
            p.setInt(2, year);
            p.addBatch();
        }
        p.executeBatch();
        long endInsert = System.currentTimeMillis();
        System.out.println("total time taken to insert the batch of inserts = " + (endInsert - startInsert) + " ms");
        p.close();*/

//Making query to retrieve number of row movies in year x
        String sqlCount = "SELECT COUNT(DISTINCT nimi) FROM Elokuvat WHERE vuosi=?";
        s.execute("CREATE INDEX idx_vuosi ON Elokuvat (vuosi)");
        p = db.prepareStatement(sqlCount);
        long startCount = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int year = rand.nextInt(101) + 1900;
            p.setInt(1, year);
            p.executeQuery();
            //r.next();
            //System.out.println(r.getInt(1));
        }
        long endCount = System.currentTimeMillis();
        System.out.println("total time taken to query counts = " + (endCount - startCount) + " ms");
        p.close();
        db.close();


        /*PreparedStatement pp = db.prepareStatement( "SELECT COUNT(DISTINCT nimi) FROM Elokuvat WHERE vuosi=?");
        int year = rand.nextInt(101) + 1900;
        pp.setInt(1, year);
        ResultSet r = pp.executeQuery();
        while (r.next()) {
            System.out.println(r.getInt(1));
        }*/
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
    int randomYear(int min, int max){
        Random rand = new Random();
        int year = rand.nextInt((max-min)+1) + min;
        return year;
    }

    private void insertValues(Connection db, int rows, String firstColumn, int secondColumn) {
        String sqlInsert = "INSERT INTO Elokuvat (nimi,vuosi) VALUES (?,?)";
        PreparedStatement p = db.prepareStatement(sqlInsert);
        for (int i = 0; i < rows; i++) {
            Testi testi = new Testi();
            p.setString(1, testi.randomString(8));
            p.setInt(2, testi.randomYear(1900,2000));
            p.addBatch();
        }
        p.executeBatch();
        p.close();
    }
}

