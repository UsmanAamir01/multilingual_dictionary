
package pl;

import bl.WordBO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DictionaryScraperApp extends JFrame {
    private WordBO service;
    private JTextField urduFilePathField;
    private JTextField farsiFilePathField;
    private JTextArea outputArea;
    private JButton scrapeUrduButton;
    private JButton scrapeFarsiButton;

    public DictionaryScraperApp() {
        service = new WordBO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dictionary Scraper");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Urdu File Path Input
        JLabel urduLabel = new JLabel("Urdu File Path:");
        urduLabel.setBounds(20, 20, 100, 30);
        add(urduLabel);

        urduFilePathField = new JTextField();
        urduFilePathField.setBounds(130, 20, 300, 30);
        add(urduFilePathField);

        scrapeUrduButton = new JButton("Scrape Urdu");
        scrapeUrduButton.setBounds(20, 60, 150, 30);
        add(scrapeUrduButton);

        // Farsi File Path Input
        JLabel farsiLabel = new JLabel("Farsi File Path:");
        farsiLabel.setBounds(20, 100, 100, 30);
        add(farsiLabel);

        farsiFilePathField = new JTextField();
        farsiFilePathField.setBounds(130, 100, 300, 30);
        add(farsiFilePathField);

        scrapeFarsiButton = new JButton("Scrape Farsi");
        scrapeFarsiButton.setBounds(20, 140, 150, 30);
        add(scrapeFarsiButton);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setBounds(20, 180, 440, 150);
        outputArea.setEditable(false);
        add(outputArea);

        // Action Listener for Scraping Urdu Data
        scrapeUrduButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urduFilePath = urduFilePathField.getText().trim();
                if (urduFilePath.isEmpty()) {
                    outputArea.append("Please provide a valid Urdu file path.\n");
                    return;
                }

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        String[] result = service.saveWordAndUrduMeaning(urduFilePath);
                        if (result != null) {
                            outputArea.append("Scraped Word: " + result[0] + ", Urdu Meaning: " + result[1] + "\n");
                        }
                        return null;
                    }
                }.execute();
            }
        });

        // Action Listener for Scraping Farsi Data
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

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        service.saveFarsiMeaning(wordToUpdate, farsiFilePath);
                        String farsiMeaning = service.getFarsiMeaning(wordToUpdate);
                        if (farsiMeaning != null) {
                            outputArea.append("Farsi Meaning for '" + wordToUpdate + "': " + farsiMeaning + "\n");
                        }
                        return null;
                    }
                }.execute();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DictionaryScraperApp app = new DictionaryScraperApp();
            app.setVisible(true);
        });
    }
}
