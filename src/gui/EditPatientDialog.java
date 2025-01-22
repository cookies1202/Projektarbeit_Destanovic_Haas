package gui;

import database.PatientDatabase;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class EditPatientDialog extends JDialog {

    private JTextField svnField;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JTextField geburtsdatumField;
    private JTextField stationField;

    private DefaultTableModel model;
    private int rowIndex;

    public EditPatientDialog(JFrame parentFrame, Patient patient, DefaultTableModel model, int rowIndex) {
        super(parentFrame, "Patientendaten bearbeiten", true);
        this.model = model;
        this.rowIndex = rowIndex;
        System.out.println("Das ist Edit");

        createAndShowGUI(patient);
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private void createAndShowGUI(Patient patient) {
        setSize(300, 400);
        setLayout(new GridLayout(6, 2));

        svnField = new JTextField(String.valueOf(patient.getSVN()));
        vornameField = new JTextField(patient.getVorname());
        nachnameField = new JTextField(patient.getNachname());
        geburtsdatumField = new JTextField(patient.getGebdatum().toString());
        stationField = new JTextField(String.valueOf(patient.getStation()));

        add(new JLabel("SVN:"));
        add(svnField);
        add(new JLabel("Vorname:"));
        add(vornameField);
        add(new JLabel("Nachname:"));
        add(nachnameField);
        add(new JLabel("Geburtsdatum (YYYY-MM-DD):"));
        add(geburtsdatumField);
        add(new JLabel("Station:"));
        add(stationField);

        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleSave() {
        try {
            int svn = Integer.parseInt(svnField.getText());
            String vorname = vornameField.getText();
            String nachname = nachnameField.getText();
            LocalDate geburtsdatum = LocalDate.parse(geburtsdatumField.getText());
            int station = Integer.parseInt(stationField.getText());

            Patient updatedPatient = new Patient(svn, vorname, nachname, geburtsdatum, station);
            PatientDatabase.updatePatient(updatedPatient);

            model.setValueAt(svn, rowIndex, 0);
            model.setValueAt(vorname, rowIndex, 1);
            model.setValueAt(nachname, rowIndex, 2);
            model.setValueAt(geburtsdatum, rowIndex, 3);
            model.setValueAt(station, rowIndex, 4);

            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Aktualisieren des Patienten. Bitte überprüfen Sie die Eingaben.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
