package org.example.helper;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize(){
        try(Connection connection = DatabaseConnector.connect(); Statement statement = connection.createStatement()) {
            // USERS TABLE
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "username TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "isadmin BOOLEAN NOT NULL" +
                    ");"
            );

            // BUSES TABLE
            statement.execute("CREATE TABLE IF NOT EXISTS buses (" +
                    "bus_id TEXT PRIMARY KEY, " +
                    "bus_type TEXT, " +
                    "total_seats INTEGER" +
                    ");"
            );

            // TRIPS TABLE
            statement.execute("CREATE TABLE IF NOT EXISTS trips (" +
                    "trip_id TEXT PRIMARY KEY NOT NULL, " +
                    "origin TEXT NOT NULL, " +
                    "destination TEXT NOT NULL, " +
                    "departure_time TEXT NOT NULL, " +
                    "time TEXT NOT NULL, " +
                    "bus_id INTEGER, " +
                    "FOREIGN KEY(bus_id) REFERENCES buses(bus_id)" +
                    ");"
            );

            // SEATS TABLE
            statement.execute("CREATE TABLE IF NOT EXISTS seats (" +
                    "seat_id TEXT KEY NOT NULL, " +
                    "row_number INTEGER, " +
                    "column_number INTEGER, " +
                    "is_reserved INTEGER, " +
                    "user_id TEXT, " +
                    "trip_id TEXT, " +
                    "bus_id TEXT, " +
                    "FOREIGN KEY(trip_id) REFERENCES trips(trip_id), " +
                    "FOREIGN KEY(bus_id) REFERENCES buses(bus_id), " +
                    "FOREIGN KEY(user_id) REFERENCES users(id)"+
                    ");"
            );

            // RESERVATIONS TABLE
            statement.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "reservation_id TEXT PRIMARY KEY NOT NULL, " +
                    "user_id TEXT, " +
                    "trip_id TEXT, " +
                    "seat_id INTEGER, " +
                    "reservation_time TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id), " +
                    "FOREIGN KEY(trip_id) REFERENCES trips(trip_id), " +
                    "FOREIGN KEY(seat_id) REFERENCES seats(seat_id)" +
                    ");"
            );

            System.out.println("Database baglandiiiiiii");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Database olmadiiiiiii");
        }
    }
}
