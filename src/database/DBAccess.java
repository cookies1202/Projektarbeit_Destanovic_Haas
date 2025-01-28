package database;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBAccess {

private static Connection connection;

    private static final String URL = "jdbc:mysql://Localhost:3306/Projekt Ahmed_Manuel";
    private static final String USER = "root";
    private static final String PASSWORD = "Manuel6021";

    public static Connection connect() {


             connection = null;
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.err.println("Fehler beim Herstellen der Datenbankverbindung: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        // Hier könnte eine Überprüfung oder ein Log erfolgen,
                        // aber typischerweise würde man die Verbindung hier nicht schließen.
                        // Da der Aufrufer die Verantwortung für das Schließen der Verbindung trägt.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return connection;
        }

    public  static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Verbindung auf null setzen, um Neuverbindung zu ermöglichen
                JOptionPane.showMessageDialog(null, "Datenbankverbindung wurde getrennt", "Verbindung getrennt", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Fehler beim Schließen der Verbindung: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    }





