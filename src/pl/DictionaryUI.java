package pl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import bl.IBLFacade;
import dto.Word;

public class DictionaryUI extends JFrame {

    private JTextField filePathField;
    private JTable dataTable;
    private JButton importButton;
    private JButton backButton;
    private final IBLFacade facade;
    private JFrame previousWindow;
    private JLabel wordCountLabel;
    private JLabel commonStartingWordLabel;
    private JLabel avgCharLengthLabel;

    public DictionaryUI(IBLFacade facade, JFrame previousWindow) {
        this.facade = facade;
        this.previousWindow = previousWindow;
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        setTitle("Import Dictionary Data");

        importButton = createStyledButton("Import Data");
        backButton = createStyledButton("Back");
        filePathField = new JTextField(30);
        filePathField.setFont(new Font("Arial", Font.PLAIN, 14));
        dataTable = new JTable();

        String[] columnNames = {"Arabic Word", "Urdu Meaning", "Persian Meaning"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        dataTable.setModel(tableModel);
        dataTable.setRowHeight(25);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 14));
        dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        dataTable.getTableHeader().setBackground(new Color(230, 230, 230));
        dataTable.setGridColor(Color.LIGHT_GRAY);
        dataTable.setShowGrid(true);

        wordCountLabel = new JLabel("Word Count: 0");
        commonStartingWordLabel = new JLabel("Common Starting Word: N/A");
        avgCharLengthLabel = new JLabel("Average Word Length: 0");

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(DictionaryUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    filePathField.setText(filePath);

                    List<Word> importedWords = facade.importDataFromFile(filePath);
                    if (!importedWords.isEmpty() && facade.insertImportedData(importedWords)) {
                        DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel();
                        tableModel.setRowCount(0); 
                        for (Word word : importedWords) {
                            tableModel.addRow(new Object[]{
                                word.getArabicWord(),
                                word.getUrduMeaning(),
                                word.getPersianMeaning()
                            });
                        }
                        updateStatistics(importedWords);
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
        gbc.gridwidth = 1;
        panel.add(new JLabel("File Path:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(filePathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(importButton, gbc);

        gbc.gridx = 1;
        panel.add(backButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setPreferredSize(new Dimension(450, 300));
        panel.add(tableScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(wordCountLabel, gbc);

        gbc.gridy = 4;
        panel.add(commonStartingWordLabel, gbc);

        gbc.gridy = 5;
        panel.add(avgCharLengthLabel, gbc);

        add(panel);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void updateStatistics(List<Word> words) {
        int wordCount = words.size();
        wordCountLabel.setText("Word Count: " + wordCount);

        Map<Character, Integer> startLetterCount = new HashMap<>();
        for (Word word : words) {
            char firstChar = word.getArabicWord().charAt(0);
            startLetterCount.put(firstChar, startLetterCount.getOrDefault(firstChar, 0) + 1);
        }

        char commonStartingLetter = ' ';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : startLetterCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                commonStartingLetter = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        commonStartingWordLabel.setText("Common Starting Word: " + commonStartingLetter);

        int totalLength = 0;
        for (Word word : words) {
            totalLength += word.getArabicWord().length();
        }
        double avgLength = (double) totalLength / wordCount;
        avgCharLengthLabel.setText("Average Word Length: " + String.format("%.2f", avgLength));
    }

    public static Runnable getDictionaryUIRunnable(IBLFacade facade, JFrame previousWindow) {
        return new Runnable() {
            @Override
            public void run() {
                new DictionaryUI(facade, previousWindow);
            }
        };
    }

    public static void runDictionaryUI(IBLFacade facade, JFrame previousWindow) {
        Thread uiThread = new Thread(getDictionaryUIRunnable(facade, previousWindow));
        uiThread.start();
    }
}
