package dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import domain.Film;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.*;


public class MongoFilmDao implements NoSqlDao<Film, Integer> {
    private final MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;
    private final Set<Integer> ids;

    public MongoFilmDao(String mongoURI) {
        mongoClient = MongoClients.create(mongoURI);
        ids = new HashSet<>();
    }

    @Override
    public void connectDB() {
        mongoDatabase = mongoClient.getDatabase("dataBase");
    }

    @Override
    public void connectCollection() {
        collection = mongoDatabase.getCollection("Films");
    }

    @Override
    public void create(Film film) {
        collection.insertOne(new Document("_id", createUniqId(ids))
                .append("name", film.getName())
                .append("year", film.getYear()));
    }

    @Override
    public void createMany(List<Film> films) {
        List<Document> documents = new ArrayList<>();
        for (Film film : films) {
            Document doc = new Document("_id", createUniqId(ids))
                    .append("name", film.getName())
                    .append("year", film.getYear());
            documents.add(doc);
        }
        collection.insertMany(documents);
    }

    @Override
    public Film read(Integer key) {
        Document document = collection.find(eq("_id", key)).first();
        if (document != null) {
            return new Film(key, (String) document.get("name"), (Integer) document.get("year"));
        }
        return null;
    }

    @Override
    public List<Film> findByAttribute(Object year) {
        List<Film> films = new ArrayList<>();
        List<Document> documents = collection.find(eq("year", (Integer) year)).into(new ArrayList<>());
        for (Document document : documents) {
            films.add(new Film((Integer) document.get("_id"), (String) document.get("name"), (Integer) year));
        }
        return films;
    }

    @Override
    public Film update(Film film) {
        Bson filter = eq("_id", film.getId());
        Bson updateName = set("name", film.getName());
        Bson updateYear = set("year", film.getYear());
        Bson updates = combine(updateName, updateYear);
        UpdateResult updateResult = collection.updateOne(filter, updates);
        return film;
    }

    @Override
    public void delete(Integer key) {
        Bson filter = eq("_id", key);
        DeleteResult result = collection.deleteOne(filter);
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        for (Document d : collection.find()) {
            films.add(new Film((Integer) d.get("_id"), (String) d.get("name"), (Integer) d.get("year")));
        }
        return films;
    }

    public void addEnhancedIndex() {
        collection.createIndex(Indexes.ascending( "year"));
    }

    public void dropEnhancedIndex() {
        collection.dropIndexes();
    }

    @Override
    public void dropCollection() {
        collection.drop();
    }

    public long getDatabaseSize() {
        Document collStatsResults = mongoDatabase.runCommand(new Document("collStats", "Films"));
        return Long.valueOf(collStatsResults.get("totalSize").toString()) / 1024 / 1024;
    }
}
