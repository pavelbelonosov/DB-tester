package dao;

import domain.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteFilmDao implements Dao<Film, Integer> {
    private final String dbURL;

    public SqliteFilmDao(String folder, String db) {
        dbURL = folder + db;
    }

    @Override
    public void createDB() throws SQLException {
        Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
        db.close();
    }

    @Override
    public void createTable() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             Statement s = db.createStatement()) {
            String createTable = "CREATE TABLE Films (id INTEGER PRIMARY KEY, name TEXT, year INTEGER)";
            s.execute(createTable);
        }
    }

    @Override
    public void create(Film film) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             PreparedStatement stmt = db.prepareStatement("INSERT INTO Films (name,year) VALUES (?,?)")) {
            stmt.setString(1, film.getName());
            stmt.setInt(2, film.getYear());
            stmt.executeUpdate();
        }
    }

    @Override
    public void createMany(List<Film> films) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
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
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
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
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             PreparedStatement stmt = db.prepareStatement("SELECT * FROM Films WHERE year=?")) {
            stmt.setInt(1, (Integer) year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                films.add(new Film(rs.getInt("id"), rs.getString("name"), rs.getInt("year")));
            }
            rs.close();
        }
        return films;
    }

    @Override
    public Film update(Film film) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
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
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             PreparedStatement stmt = db.prepareStatement("DELETE FROM Films WHERE id=?")) {
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    @Override
    public void clearAllRows() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             Statement s = db.createStatement()) {
            s.execute("DELETE FROM Films");
        }
    }

    @Override
    public void dropDB() throws SQLException {
        File db = new File(dbURL);
        db.delete();
    }

    @Override
    public List<Film> findAll() throws SQLException {
        List<Film> films = new ArrayList<>();
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
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
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             Statement s = db.createStatement()) {
            s.execute("CREATE INDEX idx_year ON Films (year, name)");
        }
    }

    @Override
    public void dropEnhancedIndex() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
             Statement s = db.createStatement()) {
            s.execute("DROP INDEX idx_year");
        }
    }

    @Override
    public long getDatabaseSize() {
        File file = new File(dbURL);
        return (file.length() / 1024) / 1024;
    }
}
