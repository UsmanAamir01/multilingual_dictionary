package pl;

import bl.WordBO;
import dto.Word;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class DictionaryUI {
    private JFrame frame;
    private JTextField filePathField;
    private JTable dataTable;
    private JButton viewButton;
    private WordBO wordBO;

    // Constructor to accept the WordBO instance
    public DictionaryUI(WordBO wordBO) {
        this.wordBO = wordBO; // Initialize the WordBO
    }

    public void createAndShowGUI() {
        frame = new JFrame("Import Dictionary Data");
        JButton importButton = new JButton("Import Data");
        filePathField = new JTextField(20);
        dataTable = new JTable();
        viewButton = new JButton("View Data");

        // Set up action for the import button
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    filePathField.setText(filePath);

                    // Use the passed wordBO to import data
                    List<Word> importedWords = wordBO.importDataFromFile(filePath);
                    if (!importedWords.isEmpty() && wordBO.insertImportedData(importedWords)) {
                        // Populate the table and show a success message
                        DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel();
                        tableModel.setRowCount(0); // Clear existing data
                        for (Word word : importedWords) {
                            tableModel.addRow(new Object[]{word.getWord(), word.getMeaning1(), word.getMeaning2()});
                        }
                        JOptionPane.showMessageDialog(frame, "Data imported successfully!");

                        // Enable the view button
                        viewButton.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(frame, "No valid data found in the file or import failed.");
                    }
                }
            }
        });

        // Action for the view button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Viewing imported data...");
                // Additional functionality can be implemented here
            }
        });

        // Create a table model with column headers
        String[] columnNames = {"Word", "Meaning 1", "Meaning 2"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        dataTable.setModel(tableModel);

        // Set up the layout
        JPanel panel = new JPanel();
        panel.add(new JLabel("File Path:"));
        panel.add(filePathField);
        panel.add(importButton);
        panel.add(viewButton);
        panel.add(new JScrollPane(dataTable));

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Initially disable the view button
        viewButton.setEnabled(false);
    }

    public static void main(String[] args) {
        // Create an instance of WordBO and DictionaryUI to run the application
        WordBO wordBO = new WordBO();
        DictionaryUI dictionaryUI = new DictionaryUI(wordBO);
        SwingUtilities.invokeLater(dictionaryUI::createAndShowGUI);
    }
}
