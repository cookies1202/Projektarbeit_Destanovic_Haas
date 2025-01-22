package gui;

import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class PatientenGUI {
    private JPanel panel1;

    public static void createAndShowGUI(List<Patient> patients) {
        JFrame frame = new JFrame("Patientendaten");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Vergrößerte Fenstergröße

        String[] columnNames = {"SVN", "Vorname", "Nachname", "Geburtsdatum", "Station"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Patient patient : patients) {
            model.addRow(new Object[]{
                    patient.getSVN(),
                    patient.getVorname(),
                    patient.getNachname(),
                    patient.getGebdatum(),
                    patient.getStation()
            });
        }

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Abstände hinzufügen
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Abstände hinzufügen
        JLabel searchLabel = new JLabel("Suchen: ");
        JTextField searchField = new JTextField();
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.NORTH);

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JButton addPatientButton = new JButton("Patient hinzufügen");
        JButton editPatientButton = new JButton("Patient bearbeiten");
        JButton deletePatientButton = new JButton("Patient löschen");
        JButton showBefundButton = new JButton("Befund anzeigen");
        buttonPanel.add(addPatientButton);
        buttonPanel.add(editPatientButton);
        buttonPanel.add(deletePatientButton);
        buttonPanel.add(showBefundButton);  // Hinzufügen des Buttons

        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPatientDialog(frame, model).setVisible(true);
            }
        });

        editPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int rowIndex = table.convertRowIndexToModel(selectedRow);
                    int svn = (int) model.getValueAt(rowIndex, 0);
                    String vorname = (String) model.getValueAt(rowIndex, 1);
                    String nachname = (String) model.getValueAt(rowIndex, 2);
                    LocalDate gebdatum = (LocalDate) model.getValueAt(rowIndex, 3);
                    int station = (int) model.getValueAt(rowIndex, 4);
                    Patient selectedPatient = new Patient(svn, vorname, nachname, gebdatum, station);
                    new EditPatientDialog(frame, selectedPatient, model, rowIndex);
                } else {
                    JOptionPane.showMessageDialog(frame, "Bitte wählen Sie einen Patienten zum Bearbeiten aus.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deletePatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int rowIndex = table.convertRowIndexToModel(selectedRow);
                    new DeletePatientDialog(frame, model, rowIndex);
                } else {
                    JOptionPane.showMessageDialog(frame, "Bitte wählen Sie einen Patienten zum Löschen aus.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Befund anzeigen
        showBefundButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int rowIndex = table.convertRowIndexToModel(selectedRow);
                int svn = (int) model.getValueAt(rowIndex, 0);

                // Direkt den Befund für den ausgewählten SVN anzeigen
                BefundViewer befundViewer = new BefundViewer();
                befundViewer.displayBefundBySVN(svn);
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie einen Patienten aus.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        frame.setVisible(true);
    }
}
