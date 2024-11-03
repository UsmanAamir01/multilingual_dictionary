package pl;

import bl.BLFacade;
import dto.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DictionaryScraperApp extends JFrame { // Extend JFrame
    private BLFacade facade;
    private JTextArea outputArea;
    private JTextField urduFilePathField;
    private JTextField farsiFilePathField;

    public DictionaryScraperApp() {
        facade = new BLFacade(); // Instantiate your BLFacade
        createAndShowGUI();

        setTitle("Dictionary Scraper");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this frame only
    }

    private void createAndShowGUI() {
        setLayout(new BorderLayout());

        // Text area for output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for file input and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Urdu file input
        panel.add(new JLabel("Urdu File Path:"));
        urduFilePathField = new JTextField();
        panel.add(urduFilePathField);

        JButton scrapeUrduButton = new JButton("Scrape Urdu");
        panel.add(scrapeUrduButton);

        // Farsi file input
        panel.add(new JLabel("Farsi File Path:"));
        farsiFilePathField = new JTextField();
        panel.add(farsiFilePathField);

        JButton scrapeFarsiButton = new JButton("Scrape Farsi");
        panel.add(scrapeFarsiButton);

        add(panel, BorderLayout.NORTH);

        // Add action listeners for buttons
        scrapeUrduButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urduFilePath = urduFilePathField.getText().trim();
                if (urduFilePath.isEmpty()) {
                    outputArea.append("Please provide a valid Urdu file path.\n");
                    return;
                }

                new SwingWorker<String, String>() {
                    @Override
                    protected String doInBackground() {
                        List<Word> words = facade.importDataFromFile(urduFilePath);
                        return (words.isEmpty() ? null : words.get(0).getArabicWord() + ", " + words.get(0).getUrduMeaning());
                    }

                    @Override
                    protected void done() {
                        try {
                            String result = get();
                            if (result != null) {
                                outputArea.append("Scraped: " + result + "\n");
                            } else {
                                outputArea.append("No data scraped for Urdu file.\n");
                            }
                        } catch (Exception ex) {
                            outputArea.append("Error during Urdu scraping: " + ex.getMessage() + "\n");
                        }
                    }
                }.execute();
            }
        });

        scrapeFarsiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String farsiFilePath = farsiFilePathField.getText().trim();
                if (farsiFilePath.isEmpty()) {
                    outputArea.append("Please provide a valid Farsi file path.\n");
                    return;
                }

                String wordToUpdate = JOptionPane.showInputDialog("Enter the word to update Farsi meaning:");
                if (wordToUpdate == null || wordToUpdate.isEmpty()) {
                    outputArea.append("Word cannot be empty.\n");
                    return;
                }

                new SwingWorker<String, String>() {
                    @Override
                    protected String doInBackground() {
                        return facade.getMeanings(wordToUpdate, "farsi");
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
                    }
                }.execute();
            }
        });

        setVisible(true); // Set visibility here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DictionaryScraperApp());
    }
}
