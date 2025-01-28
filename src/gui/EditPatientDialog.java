package gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import database.PatientDatabase;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

public class EditPatientDialog extends JDialog {

    private JTextField svnField;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JDateChooser gebdatumPicker; // JDateChooser
    private JSpinner stationSpinner; // JSpinner

    private DefaultTableModel model;
    private int rowIndex;

    public EditPatientDialog(JFrame parentFrame, Patient patient, DefaultTableModel model, int rowIndex) {
        super(parentFrame, "Patientendaten bearbeiten", true);

        // Modernes Look-and-Feel setzen
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        this.model = model;
        this.rowIndex = rowIndex;

        // Dialog-Einstellungen
        setSize(300, 400);
        setLayout(new BorderLayout());

        // GUI erstellen
        createAndShowGUI(patient);
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private void createAndShowGUI(Patient patient) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Felder initialisieren
        svnField = new JTextField(String.valueOf(patient.getSVN()));
        vornameField = new JTextField(patient.getVorname());
        nachnameField = new JTextField(patient.getNachname());

        gebdatumPicker = new JDateChooser();
        gebdatumPicker.setDateFormatString("dd.mm.yyyy");
        gebdatumPicker.setDate(java.sql.Date.valueOf(patient.getGebdatum()));

        stationSpinner = new JSpinner(new SpinnerNumberModel(patient.getStation(), 1, 4, 1));
        stationSpinner.setEditor(new JSpinner.NumberEditor(stationSpinner, "#"));
        ((JSpinner.DefaultEditor) stationSpinner.getEditor()).getTextField().setEditable(false);

        // Labels und Felder hinzufügen
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("SVN:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(svnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Vorname:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(vornameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Nachname:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nachnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Geburtsdatum:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(gebdatumPicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Station:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(stationSpinner, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Button-Leiste unten
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Aktionen für Buttons
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleSave() {
        if (validateInput()) {
            try {
                int svn = Integer.parseInt(svnField.getText());
                String vorname = vornameField.getText();
                String nachname = nachnameField.getText();

                // Datum aus JDateChooser abrufen
                Date date = gebdatumPicker.getDate();
                if (date == null) {
                    JOptionPane.showMessageDialog(this, "Bitte ein gültiges Datum auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDate geburtsdatum = LocalDate.of(
                        date.getYear() + 1900, // Jahr korrigieren
                        date.getMonth() + 1,  // Monat von 0-basiert auf 1-basiert ändern
                        date.getDate()
                );

                // Station aus dem Spinner holen
                int station = (int) stationSpinner.getValue();

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

    private boolean validateInput() {
        String svn = svnField.getText().trim();
        boolean isSVNValid = svn.length() == 4 && svn.matches("\\d+");

        int station = (int) stationSpinner.getValue(); // Wert des Spinners holen
        boolean isStationValid = station >= 1 && station <= 4;

        if (!isSVNValid) {
            JOptionPane.showMessageDialog(this, "Die SVN muss genau 4 Stellen haben und nur Zahlen enthalten.",
                    "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isStationValid) {
            JOptionPane.showMessageDialog(this, "Die Station muss zwischen 1 und 4 sein.",
                    "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}//
