package gui;

import com.formdev.flatlaf.FlatLightLaf;
import database.DBAccess;
import model.Patient;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;

public class PatientenGUI {
    private JPanel panel1;

    public static void createAndShowGUI(List<Patient> patients) {
        // Modernes Look-and-Feel setzen
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Hauptfenster erstellen
        JFrame frame = new JFrame("Patientendaten");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Menüleiste erstellen und rechts ausrichten
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Rechtsbündige Ausrichtung
        menuBar.setBackground(Color.MAGENTA); // Magenta-Hintergrundfarbe

        JMenu menu = new JMenu("Menü");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        JMenuItem closeMenuItem = new JMenuItem("Schließen");

        // Menüeinträge hinzufügen
        menu.add(logoutMenuItem);
        menu.add(closeMenuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        // Tabelle mit Daten
        String[] columnNames = {"SVN", "Vorname", "Nachname", "Geburtsdatum", "Station"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                // Alternierende Zeilenfarben
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 248, 255) : Color.WHITE);
                } else {
                    c.setBackground(new Color(173, 216, 230)); // Markierte Zeile
                }
                return c;
            }
        };
        table.setRowHeight(30); // Größere Zeilenhöhe
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hinzufügen der Patientendaten zur Tabelle
        for (Patient patient : patients) {
            model.addRow(new Object[]{
                    patient.getSVN(),
                    patient.getVorname(),
                    patient.getNachname(),
                    patient.getGebdatum(),
                    patient.getStation()
            });
        }

        // Scrollpane für die Tabelle
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Suchleiste
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel searchLabel = new JLabel("\uD83D\uDD0D Suchen: ");
        JTextField searchField = new JTextField();
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.NORTH);

        // Suche implementieren
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

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

        // Button-Leiste unten
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addPatientButton = new JButton("➕Hinzufügen");
        JButton editPatientButton = new JButton("✏Bearbeiten");
        JButton deletePatientButton = new JButton("🗑Löschen");
        JButton showBefundButton = new JButton("\uD83D\uDCCB Befund");
        buttonPanel.add(addPatientButton);
        buttonPanel.add(editPatientButton);
        buttonPanel.add(deletePatientButton);
        buttonPanel.add(showBefundButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Listener für Buttons
        addPatientButton.addActionListener(e -> new AddPatientDialog(frame, model).setVisible(true));

        editPatientButton.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie einen Patienten zum Bearbeiten aus.",
                        "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deletePatientButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int rowIndex = table.convertRowIndexToModel(selectedRow);
                new DeletePatientDialog(frame, model, rowIndex);
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie einen Patienten zum Löschen aus.",
                        "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        showBefundButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int rowIndex = table.convertRowIndexToModel(selectedRow);
                int svn = (int) model.getValueAt(rowIndex, 0);

                // Befund anzeigen
                BefundViewer befundViewer = new BefundViewer();
                befundViewer.displayBefundBySVN(svn);
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie einen Patienten aus.",
                        "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Listener für Menüeinträge
        logoutMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Erfolgreich abgemeldet!", "Logout", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            DBAccess.closeConnection();// Hauptfenster schließen
            LoginGUI.createLoginForm(); // Zurück zum Login
        });

        closeMenuItem.addActionListener(e -> System.exit(0));



        // Fenster sichtbar machen
        frame.setVisible(true);
//Window Logik
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Fenster nicht automatisch schließen

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Möchten Sie das Programm wirklich beenden?",
                        "Programm beenden",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Datenbankverbindung schließen
                    DBAccess.closeConnection();
                    System.exit(0);
                }
            }
        });
    }

}
