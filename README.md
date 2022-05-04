# Database testing
This project is devoted to overviewing some popular databases (Sqlite, Postgres, Mongo), the role of right indexing and DAO pattern in Java.


https://user-images.githubusercontent.com/96337244/166820350-6d67d136-b1c6-44fb-b909-4100076ac38a.mp4




## Intro
The program creates DBs with one table/collection `Films`, containing three attributes/fields: `id`, `name`, `year`.

- Insert rows/docs: program calculates time of executing given number of INSERT-operations in one batch (max 1_000_000_000).
- Execute queries: program calculates time of executing given number of SELECT-operations. One query-one connection. No caching (max 1_000).

After each test indexes are dropped, tables are cleared.

## Prerequisites for development
Requires Java 17+, Sqlite 3, Postgres 14.1+, MongoDB 5.0.6+. You can use Docker containers to build all necessary databases together.

You can change Sqlite database's settings in `application.properties`. Program creates folder `tmpProject` to store DBs, so make sure it has all permissions.

You will need at least `1024 mb` heap space and far more in case of huge table/collection.

## Build project
Run `./mvn package`. It will generate a jar named "DBtest-1.0.jar" in target folder.

## Execute project
Server accepts the following environment variables:
 - `_JAVA_OPTIONS` Size of JVM heap space. The more memory you set, the more chances of successful execution you will have.
 - `POSTGRES_HOST` The hostname for postgres database. (port will default to 5432 the default for Postgres).
 - `POSTGRES_USER` database user.
 - `POSTGRES_PASSWORD` database password.
 - `POSTGRES_DATABASE` database name.
 - `MONGO_HOST` The hostname for mongo database. (port will default to 27017 the default for MongoDB).
 - `MONGO_USER` database user.
 - `MONGO_PASSWORD` database password.

Locally:
Run with `java -jar DBtest-1.0.jar`. Don't forget to set variables: 
  On linux, execute: `$ export VAR_NAME="value"`
  On windows: `C:\>SomeDir>set VAR_NAME="value"`

VIA Docker:
Run with `docker-compose up`. It will settle multi-container project.

## How to start
Go to `localhost:8080` and follow instructions.

Nota bene! Calculation can take up to 40 minutes in case of a million-rows table and one thousand queries (depends on hardware). 
To reduce running time, decrease number of rows/documents.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://github.com/pavelbelonosov/DB-tester/blob/master/LICENSE)
