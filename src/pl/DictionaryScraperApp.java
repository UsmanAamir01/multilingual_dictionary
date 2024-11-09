package pl;

import bl.BLFacade;
import bl.IBLFacade;
import bl.UserBO;
import bl.WordBO;
import dto.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DictionaryScraperApp extends JFrame {
    private final IBLFacade facade;
    private final JTextArea outputArea;
    private final JTextField urduFilePathField;
    private final JTextField farsiFilePathField;
    private final JLabel statusLabel;

    public DictionaryScraperApp(IBLFacade facade) {
        this.facade = facade;

        setTitle("Dictionary Scraper");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        outputArea = createOutputArea();
        urduFilePathField = new JTextField();
        farsiFilePathField = new JTextField();
        statusLabel = new JLabel("Ready");

        JPanel inputPanel = createInputPanel();
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextArea createOutputArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Urdu File Path:"));
        panel.add(urduFilePathField);
        JButton scrapeUrduButton = new JButton("Scrape Urdu");
        scrapeUrduButton.addActionListener(this::scrapeUrduAction);
        panel.add(scrapeUrduButton);

        panel.add(new JLabel("Farsi File Path:"));
        panel.add(farsiFilePathField);
        JButton scrapeFarsiButton = new JButton("Scrape Farsi");
        scrapeFarsiButton.addActionListener(this::scrapeFarsiAction);
        panel.add(scrapeFarsiButton);

        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> outputArea.setText(""));
        panel.add(clearButton);

        return panel;
    }

    private void scrapeUrduAction(ActionEvent e) {
        String urduFilePath = urduFilePathField.getText().trim();
        if (urduFilePath.isEmpty()) {
            updateStatus("Please provide a valid Urdu file path.");
            return;
        }
        
        updateStatus("Scraping Urdu file...");
        new SwingWorker<List<Word>, Void>() {
            @Override
            protected List<Word> doInBackground() {
                return facade.importDataFromFile(urduFilePath);
            }

            @Override
            protected void done() {
                try {
                    List<Word> words = get();
                    if (!words.isEmpty()) {
                        outputArea.append("Scraped Urdu: " + words.get(0).getArabicWord() + ", " + words.get(0).getUrduMeaning() + "\n");
                    } else {
                        outputArea.append("No data scraped for Urdu file.\n");
                    }
                } catch (Exception ex) {
                    outputArea.append("Error during Urdu scraping: " + ex.getMessage() + "\n");
                }
                updateStatus("Urdu scraping completed.");
            }
        }.execute();
    }

    private void scrapeFarsiAction(ActionEvent e) {
        String farsiFilePath = farsiFilePathField.getText().trim();
        if (farsiFilePath.isEmpty()) {
            updateStatus("Please provide a valid Farsi file path.");
            return;
        }

        String wordToUpdate = JOptionPane.showInputDialog(this, "Enter the word to update Farsi meaning:");
        if (wordToUpdate == null || wordToUpdate.trim().isEmpty()) {
            outputArea.append("Word cannot be empty.\n");
            return;
        }

        updateStatus("Retrieving Farsi meaning for '" + wordToUpdate + "'...");
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                String farsiMeaning = facade.getMeanings(wordToUpdate, "farsi");
                if (farsiMeaning == null || farsiMeaning.isEmpty()) {
                    farsiMeaning = facade.scrapeFarsiMeaning(farsiFilePath);
                    if (farsiMeaning != null) {
                        facade.updateFarsiMeaning(wordToUpdate, farsiMeaning);
                    }
                }
                return farsiMeaning;
            }

            @Override
            protected void done() {
                try {
                    String farsiMeaning = get();
                    if (farsiMeaning != null) {
                        outputArea.append("Farsi Meaning for '" + wordToUpdate + "': " + farsiMeaning + "\n");
                    } else {
                        outputArea.append("No Farsi meaning found for '" + wordToUpdate + "'.\n");
                    }
                } catch (Exception ex) {
                    outputArea.append("Error during Farsi scraping: " + ex.getMessage() + "\n");
                }
                updateStatus("Farsi scraping completed.");
            }
        }.execute();
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        WordBO wordBO = new WordBO();
        UserBO userBO = new UserBO();
        IBLFacade facade = new BLFacade(wordBO, userBO);

        SwingUtilities.invokeLater(() -> new DictionaryScraperApp(facade));
    }
}
