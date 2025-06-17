package com.example.databaseTest;

import org.example.helper.DatabaseInitializer;
import org.example.managers.UserManager;
import org.example.models.UserModel;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    private static UserManager userManager;


    @BeforeAll
    public static void setup() {
        DatabaseInitializer.initialize(); // Veritabanını başlat
        userManager = UserManager.getInstance();
    }



    @Test
    @Order(1)
    public void testRegisterUser() {
        UserModel user = new UserModel();
        user.setId(UUID.randomUUID().toString()); // artık benzersiz
        user.setUsername("testuser");
        user.setEmail("testmail@example.com");
        user.setPassword("password123");
        user.setAdmin(false);

        boolean result = userManager.registerUser(user);
        assertTrue(result, "Kullanıcı başarılı şekilde kaydedilmelidir.");
    }

    @Test
    @Order(2)
    public void testLoginSuccess() {
        UserModel loggedInUser = userManager.login("testuser", "password123");
        assertNotNull(loggedInUser, "Giriş başarılı olmalı.");
        assertEquals("testuser", loggedInUser.getUsername(), "Kullanıcı adı eşleşmeli.");
    }

    @Test
    @Order(3)
    public void testLoginFail() {
        UserModel loggedInUser = userManager.login("wronguser", "wrongpassword");
        assertNull(loggedInUser, "Yanlış bilgilerle giriş başarısız olmalı.");
    }
}
