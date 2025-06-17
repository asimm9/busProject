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

    //singleton için nesne üretimi ve kontrol burda yapılır.
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    //kullanıcı kayıt edilir kullanıcı var mı kontrolü yapılıyor
    public boolean registerUser(UserModel user) {
        // Aynı ID'ye sahip kullanıcı varsa kayıt etme

        return userDAO.createUser(user);
    }

    //kullanıcı kayıdı burda yapılıyor
    public UserModel login(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public UserModel getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }


}
