package dao;

import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Random;
import java.util.Set;

public interface NoSqlDao<T, K> {

    void connectDB();

    void connectCollection();

    void create(T object);

    void createMany(List<T> objects);

    T read(K key);

    List<T> findByAttribute(Object attribute);

    T update(T object);

    void delete(K key);

    List<T> findAll();

    void addEnhancedIndex();

    void dropEnhancedIndex();

    void dropCollection();

    long getDatabaseSize();

    default Integer createUniqId(Set ids) {
        int id = 0;
        while (true) {
            Random random = new Random();
            int randomInt = random.nextInt(Integer.MAX_VALUE);
            if (!ids.contains(randomInt)) {
                ids.add(randomInt);
                id = randomInt;
                break;
            }
        }
        return id;
    }
}
