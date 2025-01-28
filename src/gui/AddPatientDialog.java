package gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import database.PatientDatabase;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.time.LocalDate;

public class AddPatientDialog extends JDialog {

    private JTextField svnField;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JDateChooser gebdatumPicker; // Datepicker von JCalendar
    private JSpinner stationSpinner; // Spinner für die Station

    public AddPatientDialog(JFrame parentFrame, DefaultTableModel model) {
        super(parentFrame, "Neuen Patienten hinzufügen", true);

        // Modernes Look-and-Feel setzen
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Dialog-Einstellungen
        setSize(300, 400);
        setLayout(new BorderLayout());

        // Hauptpanel mit GridBagLayout für flexibles Design
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Felder initialisieren
        svnField = new JTextField();
        vornameField = new JTextField();
        nachnameField = new JTextField();
        gebdatumPicker = new JDateChooser();
        gebdatumPicker.setDateFormatString("dd.mm.yyyy");
        // Station-Spinner initialisieren und Eingabe deaktivieren
        stationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
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
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                try {
                    int svn = Integer.parseInt(svnField.getText());
                    if (PatientDatabase.isSVNExists(svn)) {
                        JOptionPane.showMessageDialog(AddPatientDialog.this,
                                "Ein Patient mit dieser SVN existiert bereits.",
                                "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String vorname = vornameField.getText();
                    String nachname = nachnameField.getText();

                    // Datum aus dem Datepicker abrufen
                    Date date = gebdatumPicker.getDate();
                    if (date == null) {
                        JOptionPane.showMessageDialog(this,
                                "Bitte ein gültiges Datum auswählen.",
                                "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    LocalDate geburtsdatum = LocalDate.of(
                            date.getYear() + 1900, // Jahr korrigieren
                            date.getMonth() + 1,  // Monat von 0-basiert auf 1-basiert ändern
                            date.getDate()
                    );

                    int station = (int) stationSpinner.getValue();

                    Patient newPatient = new Patient(svn, vorname, nachname, geburtsdatum, station);

                    PatientDatabase.addPatient(newPatient); // Datenbank aktualisieren
                    model.addRow(new Object[]{svn, vorname, nachname, geburtsdatum, station});
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddPatientDialog.this,
                            "Fehler beim Hinzufügen des Patienten. Bitte überprüfen Sie die Eingaben.",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    private boolean validateInput() {
        String svn = svnField.getText().trim();
        boolean isSVNValid = svn.length() == 4 && svn.matches("\\d+");

        if (!isSVNValid) {
            JOptionPane.showMessageDialog(this, "Die SVN muss genau 4 Stellen haben und nur Zahlen enthalten.",
                    "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    //
}