package pl;

import bl.IBLFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryApp extends JFrame {
    private final IBLFacade facade;
    private JTable wordsTable;
    private JFrame mainDashboard;
    private DefaultTableModel tableModel;

    public DictionaryApp(IBLFacade facade, JFrame mainDashboard) {
        this.facade = facade;
        this.mainDashboard = mainDashboard;
        mainDashboard.setVisible(false);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Custom Dictionary");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel headingLabel = createHeadingLabel("Custom Dictionary");
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        wordsTable = createWordsTable();
        JScrollPane scrollPane = new JScrollPane(wordsTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Processed Words and Meanings"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton loadFileButton = createButton("Load and Process File", new Color(0, 51, 153));
        loadFileButton.addActionListener(e -> processFile());

        JButton backButton = createButton("Back", new Color(0, 51, 153));
        backButton.addActionListener(e -> {
            this.setVisible(false);
            mainDashboard.setVisible(true);
        });

        buttonPanel.add(loadFileButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private JTable createWordsTable() {
        String[] columnNames = {"Word", "Urdu Meaning", "Persian Meaning"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.setBackground(new Color(250, 250, 250));
        table.setForeground(Color.DARK_GRAY);
        return table;
    }

    private void processFile() {
        tableModel.setRowCount(0); // Clear previous data
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        try {
                            String[] meanings = facade.getMeaning1(word);
                            String urduMeaning = meanings != null && meanings.length > 0 ? meanings[0] : "Not found";
                            String persianMeaning = meanings != null && meanings.length > 1 ? meanings[1] : "Not found";

                            tableModel.addRow(new Object[]{word, urduMeaning, persianMeaning});
                        } catch (Exception e) {
                            tableModel.addRow(new Object[]{word, "Error retrieving meaning", "Error retrieving meaning"});
                        }
                    }
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ioException.getMessage());
            }
        }
    }

    private JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(new Color(25, 25, 112));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return label;
    }

    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker()),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
