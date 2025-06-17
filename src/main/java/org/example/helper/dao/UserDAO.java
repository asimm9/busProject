package org.example.helper.dao;

import org.example.helper.DatabaseConnector;
import org.example.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    //dbye Kullanıcı ekleme kodu
    public boolean createUser(UserModel user) {
        String sql = "INSERT INTO users(id, username, password, email, isadmin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isAdmin());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Giriş yapmak için kullanılıyor username ve password eşleşme kontrolü yapılıyor.
    public UserModel findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserModel user = new UserModel.Builder().id(rs.getString("id"))
                        .username(rs.getString("username")).password(rs.getString("password"))
                        .email(rs.getString("email")).admin(rs.getBoolean("isadmin")).build();
                System.out.println(user.toString());
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //idye göre dbden tek bir kullanıcı getiriyor
    public static UserModel getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserModel user = new UserModel.Builder().id(rs.getString("id"))
                        .username(rs.getString("username")).password(rs.getString("password"))
                        .email(rs.getString("email")).admin(rs.getBoolean("isadmin")).build();
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //userı delete etmek istersek idye göre burdan delete ediyoruz.
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //userı güncellemek istersek bu metodu kullanarak güncelliyoruz
    public boolean updateUser(UserModel user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
