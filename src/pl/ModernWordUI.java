package pl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.*;

import bl.*;
import dto.Word;
import pl.ui.*;

/**
 * Modern Word UI Dashboard with sidebar navigation
 */
public class ModernWordUI extends JFrame implements ThemeManager.ThemeListener {

    private static final Logger logger = LogManager.getLogger(ModernWordUI.class);
    private IBLFacade facade;
    private boolean isDarkMode = ThemeManager.isDarkMode();
    
    private SidebarPanel sidebar;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private ModernTextField searchField;
    private ModernComboBox<String> languageComboBox;
    private JLabel themeToggle;
    private CardPanel searchCard;

    public ModernWordUI(IBLFacade facade) {
        this.facade = facade;
        ThemeManager.getInstance().addListener(this);
        ComponentFactory.setDarkMode(isDarkMode);
        
        initializeFrame();
        createUI();
    }

    private void initializeFrame() {
        setTitle("Multilingual Dictionary");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(UIConstants.getBackground(isDarkMode));
        
        // Create sidebar
        createSidebar();
        mainContainer.add(sidebar, BorderLayout.WEST);
        
        // Create main content area
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UIConstants.getBackground(isDarkMode));
        
        // Header with search
        createHeader();
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.getBackground(isDarkMode));
        contentPanel.setBorder(new EmptyBorder(UIConstants.SPACING_LG, UIConstants.SPACING_LG, 
                                               UIConstants.SPACING_LG, UIConstants.SPACING_LG));
        
        // Show welcome content
        showWelcomeContent();
        
        rightPanel.add(contentPanel, BorderLayout.CENTER);
        mainContainer.add(rightPanel, BorderLayout.CENTER);
        
        setContentPane(mainContainer);
    }

    private void createSidebar() {
        sidebar = new SidebarPanel();
        sidebar.setDarkMode(isDarkMode);
        
        // Main navigation items
        sidebar.addItem(UIConstants.ICON_ADD, "Add Word");
        sidebar.addItem("📋", "View All Words");
        sidebar.addItem(UIConstants.ICON_IMPORT, "Import File");
        sidebar.addItem(UIConstants.ICON_NORMALIZE, "Word Normalization");
        sidebar.addItem(UIConstants.ICON_STAR, "Favorites");
        sidebar.addItem(UIConstants.ICON_HISTORY, "Search History");
        sidebar.addItem(UIConstants.ICON_BOOK, "Custom Dictionary");
        
        sidebar.addDivider();
        
        // Bottom items
        sidebar.addBottomItem(UIConstants.ICON_FAQ, "FAQs");
        sidebar.addBottomItem(UIConstants.ICON_INFO, "Instructions");
        sidebar.addBottomItem(UIConstants.ICON_SETTINGS, "Settings");
        
        sidebar.setClickListener(itemName -> {
            logger.info("Sidebar item clicked: " + itemName);
            handleNavigation(itemName);
        });
        
        sidebar.setActiveItemByName("Add Word");
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.getSurface(isDarkMode));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.getBorder(isDarkMode)),
            new EmptyBorder(UIConstants.SPACING_MD, UIConstants.SPACING_LG, 
                           UIConstants.SPACING_MD, UIConstants.SPACING_LG)
        ));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        
        // Search section (left)
        JPanel searchSection = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.SPACING_MD, 0));
        searchSection.setOpaque(false);
        
        // Language selector
        String[] languages = {"Arabic", "Persian", "Urdu"};
        languageComboBox = new ModernComboBox<>(languages);
        languageComboBox.setDarkMode(isDarkMode);
        languageComboBox.setPreferredSize(new Dimension(120, UIConstants.INPUT_HEIGHT));
        searchSection.add(languageComboBox);
        
        // Search field
        searchField = new ModernTextField("Search for words...", 30);
        searchField.setDarkMode(isDarkMode);
        searchField.setPrefixIcon(UIConstants.ICON_SEARCH);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
        searchSection.add(searchField);
        
        // Search button
        ModernButton searchButton = new ModernButton("Search", ModernButton.ButtonStyle.PRIMARY);
        searchButton.setDarkMode(isDarkMode);
        searchButton.setPreferredSize(new Dimension(100, UIConstants.INPUT_HEIGHT));
        searchButton.addActionListener(e -> performSearch());
        searchSection.add(searchButton);
        
        headerPanel.add(searchSection, BorderLayout.WEST);
        
        // Right section (theme toggle)
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.SPACING_MD, 0));
        rightSection.setOpaque(false);
        
        themeToggle = new JLabel(isDarkMode ? UIConstants.ICON_SUN : UIConstants.ICON_MOON);
        themeToggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        themeToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeToggle.setToolTipText("Toggle Dark Mode");
        themeToggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ThemeManager.toggleDarkMode();
            }
        });
        rightSection.add(themeToggle);
        
        headerPanel.add(rightSection, BorderLayout.EAST);
    }

    private void showWelcomeContent() {
        contentPanel.removeAll();
        
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(new EmptyBorder(UIConstants.SPACING_XL, UIConstants.SPACING_XL, 
                                               UIConstants.SPACING_XL, UIConstants.SPACING_XL));
        
        // Hero Section with gradient card
        CardPanel heroCard = new CardPanel();
        heroCard.setDarkMode(isDarkMode);
        heroCard.setCornerRadius(UIConstants.RADIUS_LG);
        heroCard.setElevation(3);
        heroCard.setLayout(new BorderLayout());
        heroCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        heroCard.setPreferredSize(new Dimension(800, 120));
        
        JPanel heroContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Premium gradient background using enhanced colors
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIConstants.getGradientStart(isDarkMode), 
                    getWidth(), getHeight(), UIConstants.getGradientEnd(isDarkMode)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIConstants.RADIUS_LG, UIConstants.RADIUS_LG);
                
                // Decorative circles with subtle opacity
                g2.setColor(new Color(255, 255, 255, 12));
                for (int i = 0; i < 6; i++) {
                    int size = 80 + (i * 20);
                    g2.fillOval(getWidth() - 180 + (i * 40), -40 + (i * 15), size, size);
                }
                
                // Subtle line accents
                g2.setColor(new Color(255, 255, 255, 8));
                for (int i = 0; i < 3; i++) {
                    g2.fillRect(0, 30 + (i * 50), getWidth(), 1);
                }
                
                g2.dispose();
            }
        };
        heroContent.setLayout(new BoxLayout(heroContent, BoxLayout.Y_AXIS));
        heroContent.setBorder(new EmptyBorder(UIConstants.SPACING_MD, UIConstants.SPACING_LG, 
                                              UIConstants.SPACING_MD, UIConstants.SPACING_LG));
        heroContent.setOpaque(false);
        
        // Welcome icon
        JLabel welcomeIcon = new JLabel("📖");
        welcomeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        welcomeIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroContent.add(welcomeIcon);
        
        heroContent.add(Box.createVerticalStrut(UIConstants.SPACING_XS));
        
        JLabel titleLabel = new JLabel("Welcome to Multilingual Dictionary");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroContent.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Your premium solution for Arabic, Persian, and Urdu translations");
        subtitleLabel.setFont(UIConstants.FONT_SUBTITLE);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroContent.add(subtitleLabel);
        
        heroCard.add(heroContent, BorderLayout.CENTER);
        welcomePanel.add(heroCard);
        
        welcomePanel.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Section title
        JLabel quickActionsTitle = new JLabel("Quick Actions");
        quickActionsTitle.setFont(UIConstants.FONT_HEADING);
        quickActionsTitle.setForeground(UIConstants.getTextPrimary(isDarkMode));
        quickActionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.add(quickActionsTitle);
        
        welcomePanel.add(Box.createVerticalStrut(UIConstants.SPACING_SM));
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, UIConstants.SPACING_SM, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cardsPanel.add(createPremiumActionCard("+", "Add Word", "Add new entries", "Add Word", 
                       UIConstants.SUCCESS, UIConstants.SUCCESS_DARK));
        cardsPanel.add(createPremiumActionCard("📋", "View All", "Browse dictionary", "View All Words",
                       UIConstants.GRADIENT_BLUE_START, UIConstants.GRADIENT_BLUE_END));
        cardsPanel.add(createPremiumActionCard("⭐", "Favorites", "Saved words", "Favorites",
                       UIConstants.GRADIENT_AMBER_START, UIConstants.GRADIENT_AMBER_END));
        cardsPanel.add(createPremiumActionCard("🕐", "History", "Recent searches", "Search History",
                       UIConstants.GRADIENT_PURPLE_START, UIConstants.GRADIENT_PURPLE_END));
        
        welcomePanel.add(cardsPanel);
        
        welcomePanel.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Statistics Section
        JLabel statsTitle = new JLabel("Statistics");
        statsTitle.setFont(UIConstants.FONT_HEADING);
        statsTitle.setForeground(UIConstants.getTextPrimary(isDarkMode));
        statsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.add(statsTitle);
        
        welcomePanel.add(Box.createVerticalStrut(UIConstants.SPACING_SM));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, UIConstants.SPACING_SM, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        statsPanel.add(createStatCardPremium("📚", "1,000+", "Total Words", UIConstants.getPrimary(isDarkMode)));
        statsPanel.add(createStatCardPremium("🌍", "3", "Languages", UIConstants.getSecondary(isDarkMode)));
        statsPanel.add(createStatCardPremium("⭐", "50+", "Favorites", UIConstants.getAccent(isDarkMode)));
        
        welcomePanel.add(statsPanel);
        
        // Add content directly without scroll pane for scroll-free layout
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private CardPanel createPremiumActionCard(String icon, String title, String description, 
                                               String action, Color gradientStart, Color gradientEnd) {
        CardPanel card = new CardPanel() {
            private boolean isHovered = false;
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int padding = 4;
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, isHovered ? 30 : 20));
                g2.fillRoundRect(padding, padding + 2, width - padding * 2, height - padding * 2, 
                                UIConstants.RADIUS_MD, UIConstants.RADIUS_MD);
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(0, 0, 
                    isHovered ? gradientStart.brighter() : (isDarkMode ? UIConstants.DARK_CARD : UIConstants.LIGHT_CARD),
                    0, height, 
                    isHovered ? gradientEnd.brighter() : (isDarkMode ? UIConstants.DARK_CARD : UIConstants.LIGHT_CARD));
                g2.setPaint(gradient);
                g2.fillRoundRect(padding, padding, width - padding * 2, height - padding * 2 - 2, 
                                UIConstants.RADIUS_MD, UIConstants.RADIUS_MD);
                
                // Draw top accent bar when hovered
                if (isHovered) {
                    g2.setColor(gradientStart);
                    g2.fillRoundRect(padding, padding, width - padding * 2, 4, 4, 4);
                }
                
                // Draw border
                g2.setColor(isHovered ? gradientStart : UIConstants.getBorder(isDarkMode));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(padding, padding, width - padding * 2 - 1, height - padding * 2 - 3, 
                                UIConstants.RADIUS_MD, UIConstants.RADIUS_MD);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        card.setDarkMode(isDarkMode);
        card.setPreferredSize(new Dimension(180, 110));
        card.setElevation(0);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(UIConstants.SPACING_MD, UIConstants.SPACING_SM, 
                                       UIConstants.SPACING_SM, UIConstants.SPACING_SM));
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNavigation(action);
                sidebar.setActiveItemByName(action);
            }
        });
        
        // Icon with colored background
        JPanel iconContainer = new JPanel();
        iconContainer.setOpaque(false);
        iconContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconContainer.add(iconLabel);
        card.add(iconContainer);
        
        card.add(Box.createVerticalStrut(UIConstants.SPACING_SM));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(UIConstants.FONT_BODY_BOLD);
        titleLbl.setForeground(UIConstants.getTextPrimary(isDarkMode));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLbl);
        
        card.add(Box.createVerticalStrut(UIConstants.SPACING_XS));
        
        JLabel descLbl = new JLabel(description);
        descLbl.setFont(UIConstants.FONT_CAPTION);
        descLbl.setForeground(UIConstants.getTextSecondary(isDarkMode));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLbl);
        
        return card;
    }

    private CardPanel createStatCardPremium(String icon, String value, String label, Color accentColor) {
        CardPanel card = new CardPanel();
        card.setDarkMode(isDarkMode);
        card.setElevation(1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(UIConstants.SPACING_MD, UIConstants.SPACING_MD, 
                                       UIConstants.SPACING_MD, UIConstants.SPACING_MD));
        
        // Left accent bar
        JPanel accentBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 4, getHeight(), 2, 2);
                g2.dispose();
            }
        };
        accentBar.setOpaque(false);
        accentBar.setPreferredSize(new Dimension(4, 0));
        card.add(accentBar, BorderLayout.WEST);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, UIConstants.SPACING_MD, 0, 0));
        
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.SPACING_SM, 0));
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        topRow.add(iconLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        topRow.add(valueLabel);
        
        content.add(topRow);
        
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(UIConstants.FONT_CAPTION);
        labelLbl.setForeground(UIConstants.getTextSecondary(isDarkMode));
        labelLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(labelLbl);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }

    private void handleNavigation(String itemName) {
        logger.info("Navigating to: " + itemName);
        
        switch (itemName) {
            case "Add Word":
                new ModernAddWordView(facade, this, isDarkMode);
                break;
            case "View All Words":
                this.setVisible(false);
                new ModernAllWordView(facade, this, isDarkMode);
                break;
            case "Import File":
                navigateTo(new DictionaryUI(facade, this));
                break;
            case "Word Normalization":
                navigateTo(new ArabicWordProcessingView(facade, this));
                break;
            case "Favorites":
                new ModernFavoritesView(this, facade, isDarkMode);
                break;
            case "Search History":
                new ModernHistoryView(this, facade, isDarkMode);
                break;
            case "Custom Dictionary":
                navigateTo(new DictionaryApp(facade, this));
                break;
            case "FAQs":
                new ModernFAQsView(this, isDarkMode);
                break;
            case "Instructions":
                new ModernInstructionsView(this, isDarkMode);
                break;
            case "Settings":
                new ModernSettingsView(this, isDarkMode, () -> {
                    ThemeManager.toggleDarkMode();
                });
                break;
        }
    }

    private void navigateTo(JFrame frame) {
        frame.setVisible(true);
        this.setVisible(false);
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a word to search.", 
                                         "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        logger.info("Searching for: " + searchText + " in " + selectedLanguage);
        
        String result = facade.getMeanings(searchText, selectedLanguage);
        
        if (result != null && !result.equals("Word not found.") && !result.equals("Error retrieving meanings.")) {
            showSearchResult(searchText, selectedLanguage, result);
            
            Word word = createWordBasedOnLanguage(searchText, selectedLanguage);
            if (word != null) {
                facade.addSearchToHistory(word);
            }
        } else {
            int choice = JOptionPane.showConfirmDialog(this,
                "Word not found in the database. Would you like to scrape data for this word?",
                "Word Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                DictionaryScraper scraper = new DictionaryScraper(facade, this);
                scraper.setTitle("Scraper for " + selectedLanguage);
                scraper.setVisible(true);
            }
        }
    }

    private void showSearchResult(String word, String language, String result) {
        contentPanel.removeAll();
        
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setOpaque(false);
        
        // Back button
        ModernButton backButton = new ModernButton(UIConstants.ICON_BACK, "Back to Dashboard", ModernButton.ButtonStyle.TEXT);
        backButton.setDarkMode(isDarkMode);
        backButton.addActionListener(e -> showWelcomeContent());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(backButton);
        resultPanel.add(topPanel, BorderLayout.NORTH);
        
        // Result card
        CardPanel resultCard = new CardPanel();
        resultCard.setDarkMode(isDarkMode);
        resultCard.setElevation(2);
        resultCard.setLayout(new BorderLayout());
        resultCard.setBorder(new EmptyBorder(UIConstants.SPACING_LG, UIConstants.SPACING_LG, 
                                             UIConstants.SPACING_LG, UIConstants.SPACING_LG));
        
        JPanel cardContent = new JPanel();
        cardContent.setLayout(new BoxLayout(cardContent, BoxLayout.Y_AXIS));
        cardContent.setOpaque(false);
        
        // Word title
        JLabel wordLabel = new JLabel(word);
        wordLabel.setFont(UIConstants.FONT_ARABIC_TITLE);
        wordLabel.setForeground(UIConstants.getPrimary(isDarkMode));
        cardContent.add(wordLabel);
        
        // Language badge
        JLabel langBadge = new JLabel(language);
        langBadge.setFont(UIConstants.FONT_CAPTION);
        langBadge.setForeground(UIConstants.LIGHT_SECONDARY);
        cardContent.add(langBadge);
        
        cardContent.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Divider
        JSeparator divider = new JSeparator();
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setForeground(UIConstants.getBorder(isDarkMode));
        cardContent.add(divider);
        
        cardContent.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Meaning
        JLabel meaningTitle = new JLabel("Meanings:");
        meaningTitle.setFont(UIConstants.FONT_BODY_BOLD);
        meaningTitle.setForeground(UIConstants.getTextPrimary(isDarkMode));
        cardContent.add(meaningTitle);
        
        cardContent.add(Box.createVerticalStrut(UIConstants.SPACING_SM));
        
        JTextArea meaningArea = new JTextArea(result);
        meaningArea.setFont(UIConstants.FONT_BODY);
        meaningArea.setForeground(UIConstants.getTextPrimary(isDarkMode));
        meaningArea.setBackground(UIConstants.getCard(isDarkMode));
        meaningArea.setEditable(false);
        meaningArea.setLineWrap(true);
        meaningArea.setWrapStyleWord(true);
        meaningArea.setBorder(null);
        cardContent.add(meaningArea);
        
        cardContent.add(Box.createVerticalStrut(UIConstants.SPACING_LG));
        
        // Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.SPACING_SM, 0));
        actionsPanel.setOpaque(false);
        
        ModernButton favoriteBtn = new ModernButton(UIConstants.ICON_STAR_OUTLINE, "Add to Favorites", ModernButton.ButtonStyle.OUTLINE);
        favoriteBtn.setDarkMode(isDarkMode);
        favoriteBtn.addActionListener(e -> {
            facade.markWordAsFavorite(word, true);
            JOptionPane.showMessageDialog(this, "Added to favorites!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        actionsPanel.add(favoriteBtn);
        
        cardContent.add(actionsPanel);
        
        resultCard.add(cardContent, BorderLayout.CENTER);
        resultPanel.add(resultCard, BorderLayout.CENTER);
        
        contentPanel.add(resultPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
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

    @Override
    public void themeChanged(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        ComponentFactory.setDarkMode(isDarkMode);
        
        // Update components
        themeToggle.setText(isDarkMode ? UIConstants.ICON_SUN : UIConstants.ICON_MOON);
        sidebar.setDarkMode(isDarkMode);
        searchField.setDarkMode(isDarkMode);
        languageComboBox.setDarkMode(isDarkMode);
        
        // Update backgrounds
        getContentPane().setBackground(UIConstants.getBackground(isDarkMode));
        headerPanel.setBackground(UIConstants.getSurface(isDarkMode));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.getBorder(isDarkMode)),
            new EmptyBorder(UIConstants.SPACING_MD, UIConstants.SPACING_LG, 
                           UIConstants.SPACING_MD, UIConstants.SPACING_LG)
        ));
        contentPanel.setBackground(UIConstants.getBackground(isDarkMode));
        
        // Refresh content
        showWelcomeContent();
        
        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }

    @Override
    public void dispose() {
        ThemeManager.getInstance().removeListener(this);
        super.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            IBLFacade facade = new BLFacade(new WordBO(), new UserBO());
            ModernWordUI wordUI = new ModernWordUI(facade);
            wordUI.setVisible(true);
        });
    }
}
