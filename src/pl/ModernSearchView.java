package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import bl.IBLFacade;
import dto.Word;
import pl.ui.*;

/**
 * Modern Search View with premium design
 */
public class ModernSearchView extends JPanel {
    private final IBLFacade facade;
    private ModernTextField searchField;
    private JComboBox<String> languageComboBox;
    private JTextArea resultTextArea;
    private ModernButton searchButton;
    private JFrame mainDashboard;
    private boolean isDarkMode = false;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;

    public ModernSearchView(IBLFacade facade, JFrame mainDashboard, boolean isDarkMode) {
        this.facade = facade;
        this.mainDashboard = mainDashboard;
        this.isDarkMode = isDarkMode;
        
        updateColors();
        initializeUI();
    }
    
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        updateColors();
        refreshUI();
    }
    
    private void updateColors() {
        bgColor = UIConstants.getBackground(isDarkMode);
        surfaceColor = UIConstants.getSurface(isDarkMode);
        cardColor = UIConstants.getCard(isDarkMode);
        textColor = UIConstants.getTextPrimary(isDarkMode);
        textSecondary = UIConstants.getTextSecondary(isDarkMode);
        borderColor = UIConstants.getBorder(isDarkMode);
    }
    
    private void refreshUI() {
        setBackground(bgColor);
        revalidate();
        repaint();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header with search
        add(createSearchHeader(), BorderLayout.NORTH);
        
        // Results area
        add(createResultsPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createSearchHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 20));
        header.setOpaque(false);
        
        // Title
        JPanel titlePanel = new JPanel(new BorderLayout(0, 5));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(UIConstants.ICON_SEARCH + "  Search Dictionary");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(textColor);
        
        JLabel subtitleLabel = new JLabel("Find words and their meanings across languages");
        subtitleLabel.setFont(UIConstants.FONT_BODY);
        subtitleLabel.setForeground(textSecondary);
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Search card
        CardPanel searchCard = new CardPanel(new BorderLayout(15, 0));
        searchCard.setDarkMode(isDarkMode);
        searchCard.setElevation(3);
        searchCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Search field
        searchField = new ModernTextField("Enter a word to search...", 25);
        searchField.setDarkMode(isDarkMode);
        searchField.setPreferredSize(new Dimension(300, 48));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSearch();
                }
            }
        });
        
        // Language selector
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        langPanel.setOpaque(false);
        
        JLabel langLabel = new JLabel("Language:");
        langLabel.setFont(UIConstants.FONT_BODY);
        langLabel.setForeground(textColor);
        
        languageComboBox = new JComboBox<>(new String[]{"Arabic", "Urdu", "Persian"});
        languageComboBox.setFont(UIConstants.FONT_BODY);
        languageComboBox.setPreferredSize(new Dimension(120, 40));
        languageComboBox.setBackground(surfaceColor);
        languageComboBox.setForeground(textColor);
        
        langPanel.add(langLabel);
        langPanel.add(languageComboBox);
        
        // Search button
        searchButton = new ModernButton(UIConstants.ICON_SEARCH, "Search", ModernButton.ButtonStyle.PRIMARY);
        searchButton.setDarkMode(isDarkMode);
        searchButton.setPreferredSize(new Dimension(120, 48));
        searchButton.addActionListener(e -> handleSearch());
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(searchField);
        inputPanel.add(langPanel);
        inputPanel.add(searchButton);
        
        searchCard.add(inputPanel, BorderLayout.CENTER);
        
        header.add(titlePanel, BorderLayout.NORTH);
        header.add(searchCard, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel createResultsPanel() {
        CardPanel resultsCard = new CardPanel(new BorderLayout(0, 15));
        resultsCard.setDarkMode(isDarkMode);
        resultsCard.setElevation(2);
        
        // Results header
        JLabel resultsLabel = new JLabel("Search Results");
        resultsLabel.setFont(UIConstants.FONT_BODY_BOLD);
        resultsLabel.setForeground(textColor);
        
        // Results text area
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        resultTextArea.setBackground(cardColor);
        resultTextArea.setForeground(textColor);
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        resultTextArea.setText("Enter a word above to search for its meanings.\n\nTips:\n• Select the language of the word you're searching\n• Press Enter or click Search to find results\n• Double-click results to view more details");
        
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        scrollPane.getViewport().setBackground(cardColor);
        
        resultsCard.add(resultsLabel, BorderLayout.NORTH);
        resultsCard.add(scrollPane, BorderLayout.CENTER);
        
        return resultsCard;
    }
    
    private void handleSearch() {
        String wordText = searchField.getText().trim();
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        
        if (wordText.isEmpty()) {
            showResult("⚠️ Please enter a word to search.", true);
            return;
        }
        
        // Show loading state
        resultTextArea.setText("🔍 Searching...");
        searchButton.setEnabled(false);
        
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return facade.getMeanings(wordText, selectedLanguage);
            }
            
            @Override
            protected void done() {
                try {
                    String result = get();
                    searchButton.setEnabled(true);
                    
                    if (result != null && !"Word not found.".equals(result)) {
                        showResult("✅ Results for \"" + wordText + "\" (" + selectedLanguage + "):\n\n" + result, false);
                        addToHistory(wordText, selectedLanguage);
                    } else {
                        showNotFoundDialog(wordText, selectedLanguage);
                    }
                } catch (Exception e) {
                    searchButton.setEnabled(true);
                    showResult("❌ Error occurred while searching. Please try again.", true);
                }
            }
        };
        worker.execute();
    }
    
    private void showResult(String text, boolean isError) {
        resultTextArea.setText(text);
        if (isError) {
            resultTextArea.setForeground(UIConstants.ERROR);
        } else {
            resultTextArea.setForeground(textColor);
        }
    }
    
    private void addToHistory(String wordText, String language) {
        Word word = null;
        if ("Arabic".equals(language)) {
            word = new Word(wordText, null, null);
        } else if ("Persian".equals(language)) {
            word = new Word(null, wordText, null);
        } else if ("Urdu".equals(language)) {
            word = new Word(null, null, wordText);
        }
        if (word != null) {
            facade.addSearchToHistory(word);
        }
    }
    
    private void showNotFoundDialog(String wordText, String language) {
        int choice = JOptionPane.showConfirmDialog(this,
            "\"" + wordText + "\" was not found in the database.\n\nWould you like to scrape data for this word?",
            "Word Not Found",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            DictionaryScraper scraper = new DictionaryScraper(facade, mainDashboard);
            scraper.setTitle("Scraper for " + language);
            scraper.setVisible(true);
        } else {
            showResult("❌ Word \"" + wordText + "\" was not found.\n\nSuggestions:\n• Check the spelling\n• Try a different language\n• Use the scraper to add new words", false);
        }
    }
}
