package gui;

import database.PatientDatabase;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class EditPatientDialog extends JDialog {

    private JTextField svnField;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JTextField geburtsdatumField;
    private JTextField siteidField;

    private Patient originalPatient;
    private DefaultTableModel model;
    private int rowIndex;

    public EditPatientDialog(JFrame parentFrame, Patient patient, DefaultTableModel model, int rowIndex) {
        super(parentFrame, "Patientendaten bearbeiten", true);
        this.originalPatient = patient;
        this.model = model;
        this.rowIndex = rowIndex;

        setSize(300, 400);
        setLayout(new GridLayout(6, 2));

        svnField = new JTextField(String.valueOf(patient.getSVN()));
        vornameField = new JTextField(patient.getVorname());
        nachnameField = new JTextField(patient.getNachname());
        geburtsdatumField = new JTextField(patient.getGebdatum().toString()); // Format: YYYY-MM-DD
        siteidField = new JTextField(String.valueOf(patient.getSiteid()));

        add(new JLabel("SVN:"));
        add(svnField);
        add(new JLabel("Vorname:"));
        add(vornameField);
        add(new JLabel("Nachname:"));
        add(nachnameField);
        add(new JLabel("Geburtsdatum (YYYY-MM-DD):"));
        add(geburtsdatumField);
        add(new JLabel("SiteID:"));
        add(siteidField);

        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int svn = Integer.parseInt(svnField.getText());
                    String vorname = vornameField.getText();
                    String nachname = nachnameField.getText();
                    LocalDate geburtsdatum = LocalDate.parse(geburtsdatumField.getText());
                    int siteid = Integer.parseInt(siteidField.getText());

                    // Validierung der SiteID
                    if (siteid < 1 || siteid > 4) {
                        throw new IllegalArgumentException("SiteID muss zwischen 1 und 4 sein.");
                    }

                    // Erstellen des neuen Patientenobjekts
                    Patient updatedPatient = new Patient(svn, vorname, nachname, geburtsdatum, siteid);

                    // Aktualisieren der Patientendaten in der Datenbank
                    PatientDatabase.updatePatient(updatedPatient);

                    // Aktualisieren der Tabelle mit den neuen Daten
                    model.setValueAt(svn, rowIndex, 0);
                    model.setValueAt(vorname, rowIndex, 1);
                    model.setValueAt(nachname, rowIndex, 2);
                    model.setValueAt(geburtsdatum, rowIndex, 3);
                    model.setValueAt(siteid, rowIndex, 4);

                    // Dialog schließen
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditPatientDialog.this, "Fehler beim Aktualisieren des Patienten. Bitte überprüfen Sie die Eingaben.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Schließt den Dialog ohne Änderungen
            }
        });

        // Sichtbar machen des Dialogs
        setLocationRelativeTo(parentFrame);  // Dialog in der Mitte des Elternfensters platzieren
        setVisible(true);  // Dialog sichtbar machen
    }
}
