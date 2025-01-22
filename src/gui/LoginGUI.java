package gui;

import database.PatientFetcher;
import model.Patient;
import gui.EditPatientDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import static gui.PatientenGUI.createAndShowGUI;

public class LoginGUI {

    public static void main(String[] args) {
        // GUI im Event-Dispatch-Thread starten
        SwingUtilities.invokeLater(() -> new LoginGUI().createLoginForm());

    }

    public void createLoginForm() {
        // Erstellen des JFrame (Hauptfenster)
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);  // Fenster wird in der Mitte des Bildschirms angezeigt

        // Erstellen eines Panels, um das Layout zu gestalten
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Erstellen der Eingabefelder und Labels
        JLabel userLabel = new JLabel("Benutzername:");
        JTextField userTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Passwort:");
        JPasswordField passwordField = new JPasswordField(20);

        // Login-Button
        JButton loginButton = new JButton("Login");

        // Button ActionListener für den Login-Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Holen des Benutzernamens und Passworts
                String username = userTextField.getText();
                char[] password = passwordField.getPassword();

                // Einfacher Login-Check (nur ein Beispiel)
                if (username.equals("a") && String.valueOf(password).equals("a")) {
                    // Erfolgsanzeige im eigenen Panel mit Bild
                    JPanel successPanel = new JPanel();
                    successPanel.setLayout(new BorderLayout());



                    // Laden des Bildes
                    ImageIcon erfolgreicheAnmeldung = new ImageIcon("/Users/haas.manuel/Desktop/Gesundheits Informatik/Software/Erfolgreiche_Anmeldung.png"); // Bild einfügen
                    JLabel imageLabel = new JLabel(erfolgreicheAnmeldung);
                    successPanel.add(imageLabel, BorderLayout.CENTER);

                    // Textnachricht hinzufügen
                    JLabel successMessage = new JLabel("Login erfolgreich!");
                    successMessage.setHorizontalAlignment(SwingConstants.CENTER);  // Nachricht zentrieren
                    successMessage.setForeground(Color.BLACK);  // Schriftfarbe auf Schwarz setzen
                    successPanel.add(successMessage, BorderLayout.SOUTH);

                    // Erstelle und zeige das Panel im Fenster
                    JOptionPane.showMessageDialog(frame, successPanel, "Erfolgreich angemeldet", JOptionPane.PLAIN_MESSAGE);

                    // Nach erfolgreichem Login die Patientenliste anzeigen
                    List<Patient> patients = PatientFetcher.fetchPatients();
                    SwingUtilities.invokeLater(() -> createAndShowGUI(patients));
                    frame.dispose(); // Schließt das Login-Fenster nach erfolgreichem Login
                } else {
                    JOptionPane.showMessageDialog(frame, "Ungültiger Benutzername oder Passwort!");
                }
            }
        });

        // Hinzufügen der Komponenten zum Panel
        panel.add(userLabel);
        panel.add(userTextField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());  // Leer für Layout
        panel.add(loginButton);

        // **KeyListener für die Eingabefelder**
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();  // Simuliert den Klick auf den Login-Button
                }
            }
        };

        // Den KeyListener sowohl für das Benutzername- als auch das Passwortfeld hinzufügen
        userTextField.addKeyListener(keyAdapter);
        passwordField.addKeyListener(keyAdapter);

        // Panel zum Frame hinzufügen
        frame.add(panel);

        // Sichtbarkeit des Fensters setzen
        frame.setVisible(true);
    }
}
