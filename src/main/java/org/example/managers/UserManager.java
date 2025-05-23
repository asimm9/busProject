package org.example.managers;

import org.example.models.UserModel;

import java.util.List;

public class UserManager {

    private static UserManager instance;
    private List<UserModel> userList;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }else {
            return instance;
        }
        return instance;
    }




}
