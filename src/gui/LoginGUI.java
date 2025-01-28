package gui;

import database.DBAccess;
import database.PatientFetcher;
import model.Patient;
import service.AccountManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static gui.PatientenGUI.createAndShowGUI;

public class LoginGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().createLoginForm());
    }

    public static void createLoginForm() {
        // Erstellen des Hauptfensters
        JFrame frame = new JFrame("Login");

        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null); // Fenster zentrieren

        // Hauptpanel mit Padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Titel
        JLabel titleLabel = new JLabel("Willkommen! Bitte einloggen:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Formularpanel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel userLabel = new JLabel("Benutzername:");
        JTextField userTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Passwort:");
        JPasswordField passwordField = new JPasswordField(20);

        formPanel.add(userLabel);
        formPanel.add(userTextField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttonpanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.setToolTipText("Klicken Sie hier, um sich anzumelden.");

        buttonPanel.add(loginButton);

        JButton abbrechenButton = new JButton("Abbrechen");
        abbrechenButton.setPreferredSize(new Dimension(100, 30));
        abbrechenButton.setToolTipText("Klicken Sie hier, um abzubrechen");

        buttonPanel.add(abbrechenButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Login-Logik
        loginButton.addActionListener(e -> {
            String username = userTextField.getText();
            String password = new String(passwordField.getPassword());

            AccountManager accountManager = AccountManager.getInstance();

            if (accountManager.login(username, password)) {
                ImageIcon successIcon = new ImageIcon("src/lib/Erfolgreiche_Anmeldung.png");
                JOptionPane.showMessageDialog(frame, new JLabel(new ImageIcon(successIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))),
                        "Login erfolgreich!", JOptionPane.PLAIN_MESSAGE);

                List<Patient> patients = PatientFetcher.fetchPatients();
                SwingUtilities.invokeLater(() -> createAndShowGUI(patients));
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ungültiger Benutzername oder Passwort!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        abbrechenButton.addActionListener(e -> System.exit(0));

        // Enter-Key für Login aktivieren
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };
        userTextField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // Fenster-Schließen-Logik hinzufügen
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Fenster nicht automatisch schließen

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Möchten Sie das Programm wirklich beenden?",
                        "Programm beenden",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
//ddas
                if (confirm == JOptionPane.YES_OPTION) {
                    // Datenbankverbindung schließen
                    DBAccess.closeConnection();
                    System.exit(0);
                }
            }
        });

        // Hinzufügen des Panels und Fenster sichtbar machen
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}//