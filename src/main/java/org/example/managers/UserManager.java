package org.example.managers;

import org.example.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;

    private final List<UserModel> userList;

    private UserManager() {
        this.userList = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean registerUser(UserModel user) {
        // Aynı ID'ye sahip kullanıcı varsa kayıt etme
        for (UserModel u : userList) {
            if (u.getId().equals(user.getId())) {
                return false; // Kullanıcı zaten var
            }
        }
        userList.add(user);
        return true;
    }

    public UserModel login(String username, String password) {
        for (UserModel u : userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public List<UserModel> getAllUsers() {
        return new ArrayList<>(userList);
    }

    public boolean deleteUserById(String id) {
        return userList.removeIf(u -> u.getId().equals(id));
    }

    public UserModel getUserById(String id) {
        for (UserModel user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public void clearUsers() {
        userList.clear();
    }
}
