package pl;

import bl.IBLFacade;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryApp extends JFrame {
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
        // Frame setup
        JFrame frame = new JFrame("Custom Dictionary");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);  // Center the window

        // Output area setup
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load file button setup
        JButton loadFileButton = new JButton("Load and Process File");
        loadFileButton.addActionListener(e -> processFile());
        frame.add(loadFileButton, BorderLayout.SOUTH);

        // Back button setup
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
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Process the selected file
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        // Fetch meanings from the facade for each word (assuming it returns a single string with both meanings)
                        String meanings = facade.getMeanings(word, word);  // Returns a single string (e.g., "Urdu: meaning1, Persian: meaning2")

                        // Process the fetched meanings (split them by a delimiter or custom format)
                        String urduMeaning = "Not found";
                        String persianMeaning = "Not found";
                        if (meanings != null && !meanings.isEmpty()) {
                            // Assuming the format is "Urdu: <Urdu Meaning>, Persian: <Persian Meaning>"
                            String[] meaningParts = meanings.split(",");  // Split by comma to separate meanings
                            for (String part : meaningParts) {
                                if (part.trim().startsWith("Urdu:")) {
                                    urduMeaning = part.replace("Urdu:", "").trim();
                                } else if (part.trim().startsWith("Persian:")) {
                                    persianMeaning = part.replace("Persian:", "").trim();
                                }
                            }
                        }

                        // Display word meanings in the output area
                        outputArea.append(word + ":\n");
                        outputArea.append("  Urdu: " + urduMeaning + "\n");
                        outputArea.append("  Persian: " + persianMeaning + "\n");
                        outputArea.append("\n");
                    }
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ioException.getMessage());
            }
        }
    }
}
