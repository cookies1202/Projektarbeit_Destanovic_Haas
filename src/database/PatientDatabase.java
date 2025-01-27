package database;

import model.Patient;

import java.sql.*;
import java.time.LocalDate;

public class PatientDatabase {

    public static void addPatient(Patient patient) {
        String insertSQL = "INSERT INTO patient (SVN, vorname, nachname, gebdatum, station) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, patient.getSVN());
            preparedStatement.setString(2, patient.getVorname());
            preparedStatement.setString(3, patient.getNachname());
            LocalDate geburtsdatum = patient.getGebdatum();
            Date sqlDate = Date.valueOf(geburtsdatum);
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setInt(5, patient.getStation());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient erfolgreich hinzugefügt.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Hinzufügen des Patienten.");
        }
    }
//Prüft ob es SVN schon gibt
    public static boolean isSVNExists(int svn) {
        String query = "SELECT COUNT(*) FROM patient WHERE SVN = ?";
        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, svn);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Rückgabe true, wenn SVN existiert
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void updatePatient(Patient patient) {
        String updateSQL = "UPDATE patient SET vorname = ?, nachname = ?, gebdatum = ?, station = ? WHERE SVN = ?";


        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, patient.getVorname());
            preparedStatement.setString(2, patient.getNachname());
            preparedStatement.setDate(3, java.sql.Date.valueOf(patient.getGebdatum()));
            preparedStatement.setInt(4, patient.getStation());
            preparedStatement.setInt(5, patient.getSVN());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean deletePatient(int svn) {
        String deleteSQL = "DELETE FROM patient WHERE SVN = ?";

        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            // Setze den Wert der SVN in die SQL-Abfrage
            preparedStatement.setInt(1, svn);

            // Führe die DELETE-Operation aus
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Gibt true zurück, wenn mindestens eine Zeile gelöscht wurde

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gibt false zurück, wenn ein Fehler auftritt
        }
    }

}







    // Weitere CRUD-Methoden könnten hier hinzugefügt werden
