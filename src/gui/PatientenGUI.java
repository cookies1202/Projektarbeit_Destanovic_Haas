package gui;

import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class PatientenGUI {

    public static void createAndShowGUI(List<Patient> patients) {
        JFrame frame = new JFrame("Patientendaten");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        String[] columnNames = {"SVN", "Vorname", "Nachname", "Geburtsdatum", "SiteID", "Bearbeiten"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Hinzufügen der Patientendaten zur Tabelle
        for (Patient patient : patients) {
            model.addRow(new Object[]{
                    patient.getSVN(),
                    patient.getVorname(),
                    patient.getNachname(),
                    patient.getGebdatum(),
                    patient.getSiteid(),
                    "Bearbeiten" // Text für den Button
            });
        }

        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Tabellen-Renderer und Editor für die Bearbeiten-Spalte festlegen
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Suchfeld hinzufügen
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Suchen: ");
        JTextField searchField = new JTextField();
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.NORTH);

        // ActionListener für das Suchfeld
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

        // ActionListener für den Hinzufügen-Button
        JButton addPatientButton = new JButton("Patient hinzufügen");
        frame.add(addPatientButton, BorderLayout.SOUTH);

        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new gui.AddPatientDialog(frame, model).setVisible(true);
            }
        });

        frame.setVisible(true);
    }

    // Renderer für den Button in der Tabelle
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Bearbeiten");
            setPreferredSize(new Dimension(100, 25)); // Größe des Buttons
            setMargin(new Insets(0, 0, 0, 0)); // Entfernt unnötige Ränder
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Der "Bearbeiten"-Button wird in der Zelle angezeigt
            return this;
        }
    }

    // Editor für den Button in der Tabelle
    public static class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Bearbeiten");
            button.setPreferredSize(new Dimension(100, 25)); // Größe des Buttons
            button.setMargin(new Insets(0, 0, 0, 0)); // Entfernt unnötige Ränder

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) button.getParent();
                    int row = table.getSelectedRow();
                    int svn = (int) table.getValueAt(row, 0);
                    String vorname = (String) table.getValueAt(row, 1);
                    String nachname = (String) table.getValueAt(row, 2);
                    String geburtsdatum = (String) table.getValueAt(row, 3);
                    int siteid = (int) table.getValueAt(row, 4);

                    Patient patient = new Patient(svn, vorname, nachname, LocalDate.parse(geburtsdatum), siteid);

                    // Hier wird das Modell direkt aus der Tabelle extrahiert und an den EditDialog übergeben
                    DefaultTableModel model = (DefaultTableModel) table.getModel();

                    // JFrame des übergeordneten Fensters holen
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);

                    // Dialog öffnen, um die Patientendaten zu bearbeiten
                    new EditPatientDialog(parentFrame, patient, model, row).setVisible(true);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button; // Den Button als Editor zurückgeben
        }
    }

}
