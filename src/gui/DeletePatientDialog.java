package gui;

import database.PatientDatabase;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeletePatientDialog extends JDialog {

    private DefaultTableModel model;
    private int rowIndex;

    public DeletePatientDialog(JFrame parentFrame, DefaultTableModel model, int rowIndex) {
        super(parentFrame, "Patient löschen", true);
        this.model = model;
        this.rowIndex = rowIndex;

        createAndShowGUI();
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private void createAndShowGUI() {
        setSize(300, 150);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Möchten Sie diesen Patienten wirklich löschen?");
        add(label);

        JButton yesButton = new JButton("Ja");
        JButton noButton = new JButton("Nein");

        add(yesButton);
        add(noButton);

        yesButton.addActionListener(e -> handleDelete());
        noButton.addActionListener(e -> dispose());
    }

    private void handleDelete() {
        // Annahme: Die SVN ist in der ersten Spalte des Modells gespeichert
        int svn = (int) model.getValueAt(rowIndex, 0);

        // Löschen des Patienten aus der Datenbank
        boolean success = PatientDatabase.deletePatient(svn);

        if (success) {
            // Löschen des Patienten aus dem Modell
            model.removeRow(rowIndex);

            // Erfolgsanzeige mit Bild und Text
            JPanel successPanel = new JPanel(new BorderLayout());

            // Bild hinzufügen
            ImageIcon successIcon = new ImageIcon("/Users/haas.manuel/Desktop/Gesundheits Informatik/Software/Erfolgreiche_Anmeldung.png");
            JLabel imageLabel = new JLabel(successIcon);
            successPanel.add(imageLabel, BorderLayout.CENTER);

            // Textnachricht hinzufügen
            JLabel successMessage = new JLabel("Patient erfolgreich gelöscht!");
            successMessage.setHorizontalAlignment(SwingConstants.CENTER);
            successMessage.setForeground(Color.BLACK); // Schriftfarbe auf Schwarz setzen
            successPanel.add(successMessage, BorderLayout.SOUTH);

            // Zeige das Panel in einem JOptionPane
            JOptionPane.showMessageDialog(this, successPanel, "Erfolg", JOptionPane.PLAIN_MESSAGE);
        } else {
            // Fehlermeldung anzeigen
            JOptionPane.showMessageDialog(this,
                    "Fehler beim Löschen des Patienten.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }


}

//