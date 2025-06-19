package com.example.databaseTest;

import org.example.helper.DatabaseInitializer;
import org.example.managers.UserManager;
import org.example.models.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    private static UserManager userManager;

    @BeforeAll
    static void setUpBeforeClass() {
        userManager = UserManager.getInstance();
        DatabaseInitializer.initialize();
    }

    @Test
    public void registerUser() {
        UserModel user = new UserModel.Builder().id("test user").email("test email").password("test password").username("test userName").admin(false).build();
        assertTrue(userManager.registerUser(user));
    }

    @Test
    public void loginUser() {
        UserModel actualUser = userManager.login("test userName", "test password");
        assertNotNull(actualUser);
    }

    @Test
    public void getUser() {
        UserModel actualUser =  userManager.getUserByEmail("test email");
        assertNotNull(actualUser);
    }

}
