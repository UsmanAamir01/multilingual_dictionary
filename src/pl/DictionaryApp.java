package pl;


import bl.IBLFacade;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryApp extends JFrame{
    private final IBLFacade facade;
    private JTextArea outputArea;
    private JFrame mainDashboard;

    public DictionaryApp(IBLFacade facade, JFrame mainDashboard) {
        this.facade = facade;
        this.mainDashboard = mainDashboard;
        mainDashboard.setVisible(false); // Hide the main dashboard when this window is shown
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Custom Dictionary");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(600, 500);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton loadFileButton = new JButton("Load and Process File");
        loadFileButton.addActionListener(e -> processFile());
        frame.add(loadFileButton, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            frame.setVisible(false);  // Close the DictionaryApp window
            mainDashboard.setVisible(true);  // Show the main dashboard again
        });
        frame.add(backButton, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    // This method is triggered when the user clicks the "Load and Process File" button.
    private void processFile() {
        outputArea.setText(""); // Clear previous results

        // Open file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Process the selected file
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        // Fetch meanings from the facade
                        String[] meanings = facade.getMeaning(word);
                        String urduMeaning = meanings[0];
                        String persianMeaning = meanings[1];

                        outputArea.append(word + ":\n");
                        if (urduMeaning != null) {
                            outputArea.append("  Urdu: " + urduMeaning + "\n");
                        } else {
                            outputArea.append("  Urdu: Not found\n");
                        }
                        if (persianMeaning != null) {
                            outputArea.append("  Persian: " + persianMeaning + "\n");
                        } else {
                            outputArea.append("  Persian: Not found\n");
                        }
                        outputArea.append("\n");
                    }
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Error reading file: " + ioException.getMessage());
            }
        }
    }
}
