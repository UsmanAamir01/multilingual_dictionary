package pl;

import bl.WordBO;
import dto.Word;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

public class DictionaryUI extends JFrame {
    private JTextField filePathField;
    private JTable dataTable;
    private JButton importButton;
    private JButton backButton;
    private WordBO wordBO;
    private JFrame previousWindow;

    public DictionaryUI(WordBO wordBO, JFrame previousWindow) {
        this.wordBO = wordBO;
        this.previousWindow = previousWindow;
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        setTitle("Import Dictionary Data");

        importButton = new JButton("Import Data");
        backButton = new JButton("Back");
        filePathField = new JTextField(20);
        dataTable = new JTable();
        
        String[] columnNames = {"Arabic Word", "Urdu Meaning", "Persian Meaning"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        dataTable.setModel(tableModel);

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(DictionaryUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    filePathField.setText(filePath);
                    List<Word> importedWords = wordBO.importDataFromFile(filePath);
                    if (!importedWords.isEmpty() && wordBO.insertImportedData(importedWords)) {
                        DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel();
                        tableModel.setRowCount(0);
                        for (Word word : importedWords) {
                            tableModel.addRow(new Object[]{word.getArabicWord(), word.getUrduMeaning(), word.getPersianMeaning()});
                        }
                        JOptionPane.showMessageDialog(DictionaryUI.this, "Data imported successfully!");
                    } else {
                        JOptionPane.showMessageDialog(DictionaryUI.this, "No valid data found in the file or import failed.");
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                previousWindow.setVisible(true);
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("File Path:"), gbc);

        gbc.gridx = 1;
        panel.add(filePathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(importButton, gbc);

        gbc.gridx = 1;
        panel.add(backButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(dataTable), gbc);

        add(panel);
        setSize(500, 500);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }
}
