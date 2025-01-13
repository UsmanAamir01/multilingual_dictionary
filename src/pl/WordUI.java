package pl;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import org.apache.logging.log4j.*;

import bl.*;
import dto.Word;

public class WordUI extends JFrame {
    
    private static final Logger logger = LogManager.getLogger(WordUI.class);
    private IBLFacade facade;
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private JTextField searchField;
    private JComboBox<String> languageComboBox;
    private boolean isDarkMode = false;
    private JLabel themeToggleLabel;

    public WordUI(IBLFacade facade) {
        this.facade = facade;
        setTitle("Multilingual Dictionary");
        setSize(850, 650);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        showDashboard();
    }

    private void showDashboard() {
        getContentPane().removeAll();
        setTitle("Dashboard");

        sidebarPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        updateSidebarTheme();

        JPanel logoPanel = createLogoPanel("images/dictionary_logo.jpeg", 100, 100);
        sidebarPanel.add(logoPanel);

        themeToggleLabel = new JLabel();
        updateThemeToggleIcon();
        themeToggleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeToggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleTheme();
            }
        });

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setBackground(Color.WHITE);
        topRightPanel.add(themeToggleLabel);

        JButton addWordButton = createSidebarButton("Add Word");
        JButton viewAllButton = createSidebarButton("View All Words");
        JButton importFileButton = createSidebarButton("Import File");
        JButton arabicTaggerButton = createSidebarButton("Word Normalization");
        JButton viewFavoritesButton = createSidebarButton("View Favourites");
        JButton searchHistoryButton = createSidebarButton("Search History");
        JButton customDictionaryButton = createSidebarButton("Custom Dictionary");
        JButton closeButton = createSidebarButton("Close");

        SidebarButtonActionListener actionListener = new SidebarButtonActionListener();
        addWordButton.addActionListener(actionListener);
        viewAllButton.addActionListener(actionListener);
        importFileButton.addActionListener(actionListener);
        arabicTaggerButton.addActionListener(actionListener);
        viewFavoritesButton.addActionListener(actionListener);
        searchHistoryButton.addActionListener(actionListener);
        customDictionaryButton.addActionListener(actionListener);
        closeButton.addActionListener(actionListener);

        sidebarPanel.add(addWordButton);
        sidebarPanel.add(viewAllButton);
        sidebarPanel.add(importFileButton);
        sidebarPanel.add(arabicTaggerButton);
        sidebarPanel.add(viewFavoritesButton);
        sidebarPanel.add(searchHistoryButton);
        sidebarPanel.add(customDictionaryButton);
        sidebarPanel.add(closeButton);

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        updateMainContentTheme();
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Dictionary Dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(0, 51, 153));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContentPanel.add(welcomeLabel);

        mainContentPanel.add(Box.createVerticalStrut(20));

        
        JPanel searchLanguagePanel = new JPanel();
        searchLanguagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        searchLanguagePanel.setBackground(Color.WHITE);

        String[] languages = { "Arabic", "Persian", "Urdu" };
        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setPreferredSize(new Dimension(130, 30));  
        languageComboBox.setFont(new Font("Georgia", Font.PLAIN, 14));  

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(300, 30));  
        searchField.setFont(new Font("Georgia", Font.PLAIN, 14));

        JButton logoButton = new JButton();
        ImageIcon logoIcon = new ImageIcon("images/search-icon.png");
        logoIcon = new ImageIcon(logoIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        logoButton.setIcon(logoIcon);
        logoButton.setBorder(BorderFactory.createEmptyBorder());
        logoButton.setContentAreaFilled(false);
        logoButton.addActionListener(e -> triggerSearch());

        searchLanguagePanel.add(languageComboBox);
        searchLanguagePanel.add(searchField);
        searchLanguagePanel.add(logoButton);

        mainContentPanel.add(searchLanguagePanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, mainContentPanel);
        splitPane.setDividerSize(0);
        splitPane.setDividerLocation(220);

        getContentPane().setLayout(new BorderLayout());
        add(topRightPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }


    private JPanel createLogoPanel(String logoPath, int width, int height) {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(240, 240, 240));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(logoPath);

        if (logoIcon.getIconWidth() > 0 && logoIcon.getIconHeight() > 0) {
            Image scaledImage = logoIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            logger.warn("Logo not found at path: " + logoPath);
            logoLabel.setText("Logo Missing");
            logoLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
            logoLabel.setForeground(Color.RED);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setPreferredSize(new Dimension(width, height));
        }

        logoPanel.setLayout(new GridBagLayout());
        logoPanel.add(logoLabel);

        return logoPanel;
    }

    private class SidebarButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            logger.info("Button clicked: " + command);
            switch (command) {
                case "Add Word":
                    navigateTo(new AddWordView(facade, WordUI.this));
                    break;
                case "View All Words":
                    navigateTo(new AllWordView(facade, WordUI.this));
                    break;
                case "Import File":
                    navigateTo(new DictionaryUI(facade, WordUI.this));
                    break;
                case "Word Normalization":
                    navigateTo(new ArabicWordProcessingView(facade, WordUI.this));
                    break;
                case "View Favourites":
                    navigateTo(new ViewFavorites(WordUI.this, facade));
                    break;
                case "Search History":
                    navigateTo(new HistoryView(WordUI.this, facade));
                    break;
                case "Custom Dictionary":
                    DictionaryApp dictionaryApp = new DictionaryApp(facade, WordUI.this);
                    navigateTo(dictionaryApp);
                    break;
                case "Close":
                    System.exit(0);
                    break;
            }
        }
    }

    private JButton createSidebarButton(String text) {
        return createButton(text, new Color(0, 51, 153), Color.WHITE);
    }

    private void navigateTo(JFrame frame) {
        frame.setVisible(true);
        WordUI.this.setVisible(false);
    }

    private JButton createButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Georgia", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    private void triggerSearch() {
        String searchText = searchField.getText().trim();
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        logger.info("Search triggered with term: " + searchText + " in language: " + selectedLanguage);

        if (!searchText.isEmpty()) {
            String result = facade.getMeanings(searchText, selectedLanguage);

            if (!result.equals("Word not found.") && !result.equals("Error retrieving meanings.")) {
                JOptionPane.showMessageDialog(this,
                        "Searching for: " + searchText + " in " + selectedLanguage + "\n" + result, "Search Result",
                        JOptionPane.INFORMATION_MESSAGE);
                Word word = createWordBasedOnLanguage(searchText, selectedLanguage);
                facade.addSearchToHistory(word);
            } else {
                Object[] options = { "Scrape Data", "Segment Word" };
                int choice = JOptionPane.showOptionDialog(this,
                        "Word not found in the database. Would you like to scrape data for this word or segment it?",
                        "Word Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
                        options[0]);
                if (choice == JOptionPane.YES_OPTION) {
                    showScraperUI(selectedLanguage);
                } else if (choice == JOptionPane.NO_OPTION) {
                    showSegmentationUI();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a word to search.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        updateThemeToggleIcon();
        updateSidebarTheme();
        updateMainContentTheme();
        revalidate();
        repaint();
    }

    private void updateThemeToggleIcon() {
        String iconPath = isDarkMode ? "images/icon_darkmode.png" : "images/icon_lightmode.png";
        ImageIcon icon = new ImageIcon(iconPath);
        if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Image scaledImage = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            themeToggleLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            themeToggleLabel.setText(isDarkMode ? "Dark Mode" : "Light Mode");
        }
    }

    private void updateSidebarTheme() {
        sidebarPanel.setBackground(isDarkMode ? new Color(50, 50, 50) : new Color(240, 240, 240));

        for (Component component : sidebarPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(isDarkMode ? new Color(75, 75, 75) : new Color(0, 51, 153));  // Gray in dark mode, blue in light mode
                button.setForeground(isDarkMode ? Color.WHITE : Color.WHITE);
            }
        }
    }

    private void updateMainContentTheme() {
        mainContentPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);

        for (Component component : mainContentPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(isDarkMode ? Color.WHITE : new Color(0, 51, 153));  // White text in dark mode, dark blue in light mode
            }
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
                textField.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
            }
            if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                comboBox.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
                comboBox.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
            }
        }
    }

    private Word createWordBasedOnLanguage(String wordText, String selectedLanguage) {
        switch (selectedLanguage) {
        case "Arabic":
            return new Word(wordText, null, null);
        case "Persian":
            return new Word(null, wordText, null);
        case "Urdu":
            return new Word(null, null, wordText);
        default:
            return null;
        }
    }

    private void showSegmentationUI() {
        WordSegmentationUI segmentationUI = new WordSegmentationUI(facade, WordUI.this);
        segmentationUI.setVisible(true);
    }

    private void showScraperUI(String language) {
        DictionaryScraper scraper = new DictionaryScraper(facade, WordUI.this);
        scraper.setVisible(true);
        scraper.setTitle("Scraper for " + language);
    }

    public static void main(String[] args) {
        IBLFacade facade = new BLFacade(new WordBO(), new UserBO());
        WordUI wordUI = new WordUI(facade);
        wordUI.setVisible(true);
    }
}