package gui;

import database.PatientDatabase;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;



public class AddPatientDialog extends JDialog {

    private JTextField svnField;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JTextField geburtsdatumField;
    private JTextField stationField;


    public AddPatientDialog(JFrame parentFrame, DefaultTableModel model) {
        super(parentFrame, "Neuen Patienten hinzufügen", true);
        setSize(400, 400);
        setLayout(new GridLayout(6, 2));

        svnField = new JTextField();
        vornameField = new JTextField();
        nachnameField = new JTextField();
        geburtsdatumField = new JTextField(); // Format: YYYY-MM-DD
        stationField = new JTextField();

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

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    try {
                        int svn = Integer.parseInt(svnField.getText());
                        if (PatientDatabase.isSVNExists(svn)) {
                            JOptionPane.showMessageDialog(AddPatientDialog.this,
                                    "Ein Patient mit dieser SVN existiert bereits.",
                                    "Fehler",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Abbrechen, wenn SVN bereits vorhanden ist
                        }

                        String vorname = vornameField.getText();
                        String nachname = nachnameField.getText();
                        LocalDate geburtsdatum = LocalDate.parse(geburtsdatumField.getText());
                        int station = Integer.parseInt(stationField.getText());

                        Patient newPatient = new Patient(svn, vorname, nachname, geburtsdatum, station);

                        PatientDatabase.addPatient(newPatient); // Datenbank aktualisieren

                        model.addRow(new Object[]{svn, vorname, nachname, geburtsdatum, station});
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AddPatientDialog.this, "Fehler beim Hinzufügen des Patienten. Bitte überprüfen Sie die Eingaben.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private boolean validateInput() {
        String svn = svnField.getText().trim();
        String stationText = stationField.getText().trim();

        boolean isSVNValid = svn.length() == 4 && svn.matches("\\d+");
        boolean isStationValid;

        try {
            int station = Integer.parseInt(stationText);
            isStationValid = station >= 1 && station <= 4;
        } catch (NumberFormatException e) {
            isStationValid = false;
        }

        if (!isSVNValid) {
            JOptionPane.showMessageDialog(this, "Die SVN muss genau 4 Stellen haben und nur Zahlen enthalten.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isStationValid) {
            JOptionPane.showMessageDialog(this, "Die Station muss zwischen 1 und 4 sein :(.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
