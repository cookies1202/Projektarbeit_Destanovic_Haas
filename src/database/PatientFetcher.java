package database;

import model.Patient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientFetcher {

    public static List<Patient> fetchPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT SVN, vorname, nachname, gebdatum, station FROM Patient";

        try (Connection connection = DBAccess.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int SVN = rs.getInt("SVN");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                LocalDate gebdatum = rs.getDate("gebdatum").toLocalDate();
                int station = rs.getInt("station");

                patients.add(new Patient(SVN, vorname, nachname, gebdatum, station));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return patients;
    }


    }


