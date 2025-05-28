package org.example.helper.dao;
import org.example.helper.DatabaseConnector;
import org.example.models.Reservation;
import org.example.models.Trip;
import org.example.models.UserModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // 1) CREATE: reservation_id, user_id, trip_id, seat_id, reservation_time
    public boolean createReservation(Reservation reservation) {
        String sql = """
            INSERT INTO reservations(reservation_id, user_id, trip_id, seat_id, reservation_time)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservation.getId());                           // reservation_id TEXT
            stmt.setString(2, reservation.getUser().getId());                  // user_id TEXT
            stmt.setString(   3, reservation.getTrip().getTripID());              // trip_id INTEGER
            stmt.setString(   4, reservation.getSeat().getSeatID());          // seat_id INTEGER
            stmt.setString(5, reservation.getReservationDateTime().toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2) DELETE: reservation_id üzerinden
    public boolean cancelReservationById(String reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3) GET BY USER ID (örnek ihtiyaç hâline göre)
    public Reservation getReservationById(String userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // varsayalım ki User, Trip ve Seat sınıflarını id’den bulacak helper metodların var
                Trip trip1 = new Trip();
                trip1.setTripID("1");
                Reservation reservation = new Reservation();
                reservation.setId(rs.getString("reservation_id"));
                reservation.setTrip(trip1);
                reservation.setSeat(SeatDAO.getSeatById(rs.getInt("seat_id")));
                reservation.setReservationDateTime(rs.getTimestamp("reservation_time").toLocalDateTime());
                reservation.setUser(new UserDAO().getUserById(rs.getString("user_id")));
                reservation.setId(rs.getString("reservation_id"));
                return reservation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4) LIST ALL
    public List<Reservation> getReservationsByUserId(String userId) {
        List<Reservation> list = new ArrayList<>();
        // Sorguyu sadece bu userId’ye ait rezervasyonları almak üzere değiştiriyoruz
        String sql = "SELECT * FROM reservations WHERE user_id = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Parametre olarak kullanıcı ID’sini veriyoruz
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getString("reservation_id"));

                // userId zaten bildiğimiz için doğrudan atayabiliriz
                UserModel user = new UserDAO().getUserById(userId);
                if (user == null) {
                    user = new UserModel();
                    user.setId(userId);
                    user.setUsername("Bilinmeyen Kullanıcı (" + userId + ")");
                    user.setEmail("");
                }
                reservation.setUser(user);

                // Trip nesnesini TripDAO’dan çekiyoruz
                reservation.setTrip(new TripDAO().getTrip(rs.getString("trip_id")));

                // Seat nesnesini SeatDAO’dan çekiyoruz
                reservation.setSeat(SeatDAO.getSeatById(rs.getInt("seat_id")));

                // reservation_time alanını ISO-8601 formatında parse ediyoruz
                reservation.setReservationDateTime(
                        LocalDateTime.parse(rs.getString("reservation_time"))
                );

                list.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}