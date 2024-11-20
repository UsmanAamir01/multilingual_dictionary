package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import bl.IBLFacade;

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

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back button
        backButton = createStyledButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(backButton, gbc);

        // Search type combo box
        searchTypeComboBox = new JComboBox<>(new String[] { "Key", "Value" });
        gbc.gridx = 1;
        inputPanel.add(searchTypeComboBox, gbc);

        // Search label
        JLabel searchLabel = new JLabel("Search Word:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 2;
        inputPanel.add(searchLabel, gbc);

        // Search field
        searchField = new JTextField(15);
        gbc.gridx = 3;
        inputPanel.add(searchField, gbc);

        // Language combo box
        languageComboBox = new JComboBox<>(new String[] { "Arabic", "Urdu", "Persian" });
        gbc.gridx = 4;
        inputPanel.add(languageComboBox, gbc);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(30, 144, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 5;
        inputPanel.add(searchButton, gbc);

        // Result text area
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Result"));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordText = searchField.getText().trim();
                String selectedLanguage = (String) languageComboBox.getSelectedItem();

                if (!wordText.isEmpty()) {
                    String result = facade.getMeanings(wordText, selectedLanguage);

                    if (!result.equals("Word not found.")) {
                        resultTextArea.setText(result);
                    } else {
                        int choice = JOptionPane.showConfirmDialog(SearchView.this,
                                "Word not found in the database. Would you like to scrape data for this word?",
                                "Scrape Data",
                                JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            showScraperUI(selectedLanguage);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(SearchView.this,
                            "Please enter a word to search.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchView.this.setVisible(false);
                mainDashboard.setVisible(true);
            }
        });
    }

    private void showScraperUI(String language) {
        DictionaryScraper scraper = new DictionaryScraper(facade, mainDashboard);
        scraper.setVisible(true);
        scraper.setTitle("Scraper for " + language);
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
