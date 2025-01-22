package gui;

import database.DBAccess;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;

public class BefundViewer {

    public void displayBefundBySVN(int svn) {
        String query = "SELECT BefundBlob FROM Befunde WHERE SVN = ?";

        try (Connection connection = DBAccess.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, svn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Blob befundBlob = resultSet.getBlob("BefundBlob");

                    // Speichern des Blobs in einer temporären Datei
                    File tempFile = File.createTempFile("Befund_SVN_" + svn, ".pdf");
                    try (InputStream inputStream = befundBlob.getBinaryStream();
                         FileOutputStream outputStream = new FileOutputStream(tempFile)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        // Datei öffnen
                        Desktop.getDesktop().open(tempFile);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Kein Befund für diesen SVN gefunden.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Anzeigen des Befunds: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
