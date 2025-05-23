package org.example.views;

import javax.swing.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Giriş Ekranı");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        userLabel.setBounds(20, 20, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(130, 20, 130, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setBounds(20, 60, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 60, 130, 25);
        add(passwordField);

        JButton loginButton = new JButton("Giriş");
        loginButton.setBounds(90, 110, 100, 30);
        add(loginButton);


        setVisible(true);



    }
}
