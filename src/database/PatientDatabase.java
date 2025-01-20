package database;

import model.Patient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientDatabase {

    public static void addPatient(Patient patient) {
        String insertSQL = "INSERT INTO patient (SVN, vorname, nachname, gebdatum, siteid) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, patient.getSVN());
            preparedStatement.setString(2, patient.getVorname());
            preparedStatement.setString(3, patient.getNachname());

            LocalDate geburtsdatum = patient.getGebdatum();
            Date sqlDate = Date.valueOf(geburtsdatum);
            preparedStatement.setDate(4, sqlDate);

            preparedStatement.setInt(5, patient.getSiteid());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient erfolgreich hinzugefügt.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Hinzufügen des Patienten.");
        }
    }

    public static void updatePatient(Patient patient) {
        String updateSQL = "UPDATE patient SET vorname = ?, nachname = ?, gebdatum = ?, siteid = ? WHERE SVN = ?";

        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, patient.getVorname());
            preparedStatement.setString(2, patient.getNachname());
            preparedStatement.setDate(3, java.sql.Date.valueOf(patient.getGebdatum()));
            preparedStatement.setInt(4, patient.getSiteid());
            preparedStatement.setInt(5, patient.getSVN());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Weitere CRUD-Methoden könnten hier hinzugefügt werden
}