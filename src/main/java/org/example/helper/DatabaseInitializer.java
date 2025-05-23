package org.example.helper;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

        public static void initialize(){
            try(Connection connection = DatabaseConnector.connect(); Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "email TEXT NOT NULL," +
                        "password TEXT NOT NULL ");

                statement.execute(
                        "CREATE TABLE IF NOT EXISTS buses (\n" +
                                "    bus_id INTEGER PRIMARY KEY,\n" +
                                "    bus_type TEXT,\n" +
                                "    total_seats INTEGER,\n" +
                                "    plate_number INTEGER\n" +
                                ");"
                );

                statement.execute("" +
                        "CREATE TABLE IF NOT EXISTS trips (\n" +
                        "    trip_id INTEGER PRIMARY KEY,\n" +
                        "    origin TEXT NOT NULL,\n" +
                        "    destination TEXT NOT NULL,\n" +
                        "    departure_time TEXT NOT NULL,\n" +
                        "    bus_id INTEGER,\n" +
                        "    FOREIGN KEY(bus_id) ");

                statement.execute("CREATE TABLE IF NOT EXISTS seats (\n" +
                        "    seat_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    seat_number INTEGER,\n" +
                        "    row_number INTEGER,\n" +
                        "    column_number INTEGER,\n" +
                        "    is_reserved INTEGER,\n" +
                        "    passenger_name TEXT,\n" +
                        "    trip_id INTEGER,\n" +
                        "    FOREIGN KEY(trip_id) REFERENCES trips(trip_id)\n" +
                        ");");

                statement.execute("CREATE TABLE IF NOT EXISTS reservations (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    user_id TEXT,\n" +
                        "    trip_id INTEGER,\n" +
                        "    seat_id INTEGER,\n" +
                        "    reservation_time TEXT,\n" +
                        "    FOREIGN KEY(user_id) REFERENCES users(id),\n" +
                        "    FOREIGN KEY(trip_id) REFERENCES trips(trip_id),\n" +
                        "    FOREIGN KEY(seat_id) REFERENCES seats(seat_id)\n" +
                        ");");


            }catch (Exception e){
                e.printStackTrace();
            }
        }
}
