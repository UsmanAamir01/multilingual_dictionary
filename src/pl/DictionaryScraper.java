package pl;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import bl.IBLFacade;

public class DictionaryScraper extends JFrame {
    private final IBLFacade facade;
    private JTextField urduFilePathField;
    private JTextField farsiFilePathField;
    private JTextArea outputArea;
    private JFrame mainDashboard;

    public DictionaryScraper(IBLFacade facade, JFrame mainDashboard) {
        this.facade = facade;
        this.mainDashboard = mainDashboard;
        mainDashboard.setVisible(false);
        initializeUI();
    }


    private void initializeUI() {
        setTitle("Dictionary Scraper");
        setSize(600, 450);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel urduLabel = new JLabel("Urdu File Path:");
        urduLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(urduLabel, gbc);

        urduFilePathField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(urduFilePathField, gbc);

        JButton scrapeUrduButton = new JButton("Scrape Urdu");
        scrapeUrduButton.setFont(new Font("Arial", Font.PLAIN, 14));
        scrapeUrduButton.setBackground(new Color(100, 149, 237));
        scrapeUrduButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(scrapeUrduButton, gbc);

        JLabel farsiLabel = new JLabel("Persian File Path:");
        farsiLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(farsiLabel, gbc);

        farsiFilePathField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(farsiFilePathField, gbc);

        JButton scrapeFarsiButton = new JButton("Scrape Persian");
        scrapeFarsiButton.setFont(new Font("Arial", Font.PLAIN, 14));
        scrapeFarsiButton.setBackground(new Color(100, 149, 237));
        scrapeFarsiButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(scrapeFarsiButton, gbc);

        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(255, 99, 71));
        backButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(backButton, gbc);

        scrapeUrduButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urduFilePath = urduFilePathField.getText().trim();
                if (urduFilePath.isEmpty()) {
                    outputArea.append("Please provide a valid Urdu file path.\n");
                    return;
                }

                String[] result = facade.saveWordAndUrduMeaning(urduFilePath);
                if (result != null) {
                    outputArea.append("Scraped Word: " + result[0] + ", Urdu Meaning: " + result[1] + "\n");
                }
            }
        });

        scrapeFarsiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String farsiFilePath = farsiFilePathField.getText().trim();
                if (farsiFilePath.isEmpty()) {
                    outputArea.append("Please provide a valid Persian file path.\n");
                    return;
                }

                String wordToUpdate = JOptionPane.showInputDialog("Enter the word to update Persian meaning:");
                if (wordToUpdate == null || wordToUpdate.isEmpty()) {
                    outputArea.append("Word cannot be empty.\n");
                    return;
                }

                facade.saveFarsiMeaning(wordToUpdate, farsiFilePath);
                String farsiMeaning = facade.getFarsiMeaning(wordToUpdate);
                if (farsiMeaning != null) {
                    outputArea.append("Farsi Meaning for '" + wordToUpdate + "': " + farsiMeaning + "\n");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DictionaryScraper.this.setVisible(false);
                mainDashboard.setVisible(true);
            }
        });
    }
}
