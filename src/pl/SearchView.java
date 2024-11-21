package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bl.IBLFacade;
import dto.Word;

public class SearchView extends JPanel {
    private final IBLFacade facade;
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JComboBox<String> languageComboBox;
    private JTextArea resultTextArea;
    private JButton searchButton;
    private JButton backButton;
    private JFrame mainDashboard;

    public SearchView(IBLFacade facade, JFrame mainDashboard) {
        this.facade = facade;
        this.mainDashboard = mainDashboard;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back Button
        backButton = createStyledButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(backButton, gbc);

        // Search Type ComboBox
        searchTypeComboBox = new JComboBox<>(new String[]{"Key", "Value"});
        gbc.gridx = 1;
        inputPanel.add(searchTypeComboBox, gbc);

        // Search Label
        JLabel searchLabel = new JLabel("Search Word:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 2;
        inputPanel.add(searchLabel, gbc);

        // Search Field
        searchField = new JTextField(15);
        gbc.gridx = 3;
        inputPanel.add(searchField, gbc);

        // Language ComboBox
        languageComboBox = new JComboBox<>(new String[]{"Arabic", "Urdu", "Persian"});
        gbc.gridx = 4;
        inputPanel.add(languageComboBox, gbc);

        // Search Button
        searchButton = createStyledButton("Search");
        gbc.gridx = 5;
        inputPanel.add(searchButton, gbc);

        // Result Area
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Result"));

        // Add Panels
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        searchButton.addActionListener(e -> handleSearchAction());
        backButton.addActionListener(e -> handleBackAction());
    }

    private void handleSearchAction() {

        String wordText = searchField.getText().trim();
        String selectedLanguage = (String) languageComboBox.getSelectedItem();

        if (wordText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a word to search.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = facade.getMeanings(wordText, selectedLanguage);

        if (!"Word not found.".equals(result)) {
            resultTextArea.setText(result);
            Word word = new Word(
                "Arabic".equals(selectedLanguage) ? wordText : null,
                "Persian".equals(selectedLanguage) ? result : null,
                "Urdu".equals(selectedLanguage) ? result : null
            );
            facade.addSearchToHistory(word);
        } else {
            int choice = JOptionPane.showConfirmDialog(this,
                "Word not found in the database. Would you like to scrape data for this word?",
                "Scrape Data", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                showScraperUI(selectedLanguage);
            }
        }
    }

    private void handleBackAction() {
        setVisible(false);
        mainDashboard.setVisible(true);
    }

    private void showScraperUI(String language) {
        DictionaryScraper scraper = new DictionaryScraper(facade, mainDashboard);
        scraper.setTitle("Scraper for " + language);
        scraper.setVisible(true);
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
}
