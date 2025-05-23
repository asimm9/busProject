package org.example.views;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;


public class AdminPanel  extends JFrame {

    private JTextField departureField;
    private JTextField arrivalField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField priceField;
    private JTable tripTable;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setTitle("Otobüs Rezervasyon - Admin Paneli");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Form Paneli
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Yeni Sefer Ekle"));

        departureField = new JTextField();
        arrivalField = new JTextField();
        dateField = new JTextField();
        timeField = new JTextField();
        priceField = new JTextField();

        formPanel.add(new JLabel("Kalkış:"));
        formPanel.add(departureField);
        formPanel.add(new JLabel("Varış:"));
        formPanel.add(arrivalField);
        formPanel.add(new JLabel("Tarih (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Saat (HH:MM):"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Fiyat:"));
        formPanel.add(priceField);

        JButton addButton = new JButton("Sefer Ekle");
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        // Tablo Paneli
        String[] columnNames = {"Kalkış", "Varış", "Tarih", "Saat", "Fiyat"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tripTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(tripTable);

        // Buton Aksiyonu
        addButton.addActionListener((ActionEvent e) -> {
            String departure = departureField.getText();
            String arrival = arrivalField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String price = priceField.getText();

            if (departure.isEmpty() || arrival.isEmpty() || date.isEmpty() || time.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{departure, arrival, date, time, price});

            // Alanları temizle
            departureField.setText("");
            arrivalField.setText("");
            dateField.setText("");
            timeField.setText("");
            priceField.setText("");
        });

        // Ana Yerleşim
        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

}
