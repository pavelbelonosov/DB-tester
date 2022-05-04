package dao;

import java.sql.SQLException;
import java.util.*;

public interface Dao<T, K> {

    void createDB() throws SQLException;

    void createTable() throws SQLException;

    void create(T object) throws SQLException;

    void createMany(List<T> objects) throws SQLException;

    T read(K key) throws SQLException;

    List<T> findByAttribute(Object attribute) throws SQLException;

    T update(T object) throws SQLException;

    void delete(K key) throws SQLException;

    void clearAllRows() throws SQLException;

    void dropDB() throws SQLException;

    List<T> findAll() throws SQLException;

    void addEnhancedIndex() throws SQLException;

    void dropEnhancedIndex() throws SQLException;

    long getDatabaseSize() throws SQLException;

}

