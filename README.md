# Database testing

This project is devoted to overviewing some popular databases (Sqlite, Postgres, Mongo), the role of right indexing and DAO pattern in Java. 
Requires Java 17, Sqlite 3.36.0.3, Postgres 42.3.1, MongoDB 3.12.10.

## Intro

The program creates dbs with one table/collection `Films`, containing three attributes/fields: `id`, `name`, `year`.

- Insert rows/docs: program calculates time of executing given number of INSERT-operations in one batch.
- Execute queries: program calculates time of executing given number of SELECT-operations. One query-one connection. No caching.

After each test indexes are dropped, tables are cleared.

## How to start

You can change some database's settings in `application.properties`. Program creates folder `tmpProject` to store DBs, so make sure it has all permissions.

You will need at least `1024 mb`  heap space and far more in case of huge table/collection.

Build the project with `./mvn package`

Run with `java -jar ./target/DBtest-1.0.jar`. Nota bene! Execution can take up to 40 minutes in case of a million-rows table (depends on hardware). 
To reduce running time, decrease number of rows/documents in .properties file.

The program creates `result.html`, where you can view test details.
