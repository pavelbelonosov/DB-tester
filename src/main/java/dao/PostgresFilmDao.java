package dao;

import domain.Film;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgresFilmDao implements Dao<Film, Integer> {
    private final String dbURL;
    private final String user;
    private final String pass;
    private File file;
    private String dbName;
    private String dbSpace;
    private String indexName;

    public PostgresFilmDao(String folder, String dbUrl, String user, String pass) {
        try {
            Class.forName("org.postgresql.Driver");//loading driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.dbURL = dbUrl;
        this.user = user;
        this.pass = pass;
        this.file = new File(folder + "postgres" + UUID.randomUUID() + "/");
        this.dbSpace = "dbspace" + UUID.randomUUID().toString().replaceAll("-", "");
        this.dbName = "db" + UUID.randomUUID().toString().replaceAll("-", "");
        this.indexName = "idx_" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public void createDB() throws SQLException {
        file.mkdirs();
        String tablespace = "CREATE TABLESPACE " + dbSpace + " LOCATION '" + file.getAbsolutePath() + "'";
        String createDB = "CREATE DATABASE " + dbName + " OWNER " + user + " TABLESPACE " + dbSpace;
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            s.execute(tablespace);
            s.execute(createDB);
        }
    }

    @Override
    public void createTable() throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Films (id serial PRIMARY KEY, name VARCHAR(8), year SMALLINT)";
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            s.execute(createTable);
        }
    }

    @Override
    public void create(Film film) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             PreparedStatement stmt = db.prepareStatement("INSERT INTO Films (name,year) VALUES (?,?)")) {
            stmt.setString(1, film.getName());
            stmt.setInt(2, film.getYear());
            stmt.executeUpdate();
        }
    }

    @Override
    public void createMany(List<Film> films) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             PreparedStatement stmt = db.prepareStatement("INSERT INTO Films (name,year) VALUES (?,?)")) {
            db.setAutoCommit(false);
            for (Film film : films) {
                stmt.setString(1, film.getName());
                stmt.setInt(2, film.getYear());
                stmt.addBatch();
            }
            stmt.executeBatch();
            db.commit();
            db.setAutoCommit(true);
        }
    }

    @Override
    public Film read(Integer key) throws SQLException {
        Film film;
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             PreparedStatement stmt = db.prepareStatement("SELECT * FROM Films WHERE id=?")) {
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            film = new Film(rs.getInt("id"), rs.getString("name"), rs.getInt("year"));
            rs.close();
        }
        return film;
    }

    @Override
    public List<Film> findByAttribute(Object year) throws SQLException {
        List<Film> films = new ArrayList<>();
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             PreparedStatement stmt = db.prepareStatement("SELECT * FROM Films WHERE year=?")) {
            stmt.setInt(1, (Integer) year);
            ResultSet rs = stmt.executeQuery();
            while ((rs.next())) {
                films.add(new Film(rs.getInt("id"), rs.getString("name"), rs.getInt("year")));
            }
            rs.close();
        }
        return films;
    }


    @Override
    public Film update(Film film) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             PreparedStatement stmt = db.prepareStatement("UPDATE Films SET name=?,year=? WHERE id=?")) {
            stmt.setString(1, film.getName());
            stmt.setInt(2, film.getYear());
            stmt.setInt(3, film.getId());
            stmt.executeUpdate();
        }
        return film;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             PreparedStatement stmt = db.prepareStatement("DELETE FROM Films WHERE id=?")) {
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    @Override
    public void clearAllRows() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            s.executeUpdate("DELETE FROM Films");
        }
    }

    @Override
    public void dropDB() throws SQLException {
        String dropDB = "DROP DATABASE IF EXISTS " + dbName;
        String dropTableSpace = "DROP TABLESPACE IF EXISTS " + dbSpace;
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            s.execute(dropDB);
            s.execute(dropTableSpace);
        }
    }

    @Override
    public List<Film> findAll() throws SQLException {
        List<Film> films = new ArrayList<>();
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT * FROM Films");
            while ((rs.next())) {
                Film film = new Film(rs.getInt("id"), rs.getString("name"), rs.getInt("year"));
                films.add(film);
            }
        }
        return films;
    }

    @Override
    public void addEnhancedIndex() throws SQLException {

        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            s.execute("CREATE INDEX CONCURRENTLY " + indexName + " ON Films (year)");
        }
    }

    @Override
    public void dropEnhancedIndex() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:postgresql:" + dbURL, user, pass);
             Statement s = db.createStatement()) {
            s.execute("DROP INDEX CONCURRENTLY IF EXISTS "+indexName);
        }
    }

    @Override
    public long getDatabaseSize() {
        return FileUtils.sizeOfDirectory(file) / 1024;
    }

}
