package org.example.managers;

import org.example.helper.dao.UserDAO;
import org.example.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;
    private final List<UserModel> userList;
    private UserDAO userDAO = new UserDAO();
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

        return userDAO.createUser(user);
    }

    public UserModel login(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }

    /*public List<UserModel> getAllUsers() {
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
    }*/
}
