package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import pl.ui.*;

/**
 * Modern Settings View with premium design and proper dark mode support
 */
public class ModernSettingsView extends JDialog {
    
    private JComboBox<String> languageComboBox;
    private ModernButton themeToggleButton;
    private JCheckBox notificationsCheckBox;
    private JFrame parentFrame;
    private boolean isDarkMode;
    private Runnable onThemeChange;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;
    
    private String defaultLanguage = "English";
    private boolean defaultNotifications = true;

    public ModernSettingsView(JFrame parentFrame, boolean isDarkMode, Runnable onThemeChange) {
        super(parentFrame, "Settings", true);
        this.parentFrame = parentFrame;
        this.isDarkMode = isDarkMode;
        this.onThemeChange = onThemeChange;
        
        updateColors();
        initializeUI();
    }
    
    private void updateColors() {
        bgColor = UIConstants.getBackground(isDarkMode);
        surfaceColor = UIConstants.getSurface(isDarkMode);
        cardColor = UIConstants.getCard(isDarkMode);
        textColor = UIConstants.getTextPrimary(isDarkMode);
        textSecondary = UIConstants.getTextSecondary(isDarkMode);
        borderColor = UIConstants.getBorder(isDarkMode);
    }

    private void initializeUI() {
        setSize(450, 550);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(surfaceColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Content
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        // Footer buttons
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        // Shadow wrapper
        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setOpaque(false);
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 15));
        shadowPanel.add(mainPanel);
        
        setContentPane(shadowPanel);
        setBackground(new Color(0, 0, 0, 0));
        
        addWindowDragSupport(mainPanel);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(103, 58, 183),
                    getWidth(), getHeight(), new Color(63, 81, 181)
                );
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() + 20, 20, 20));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JLabel iconLabel = new JLabel(UIConstants.ICON_SETTINGS);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        
        // Button panel with back and close
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        
        JButton backBtn = createHeaderButton(UIConstants.ICON_BACK);
        backBtn.setToolTipText("Back to Dashboard");
        backBtn.addActionListener(e -> dispose());
        
        JButton closeBtn = createCloseButton();
        
        buttonPanel.add(backBtn);
        buttonPanel.add(closeBtn);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JButton createHeaderButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(new Color(255, 255, 200)); }
            public void mouseExited(MouseEvent e) { btn.setForeground(Color.WHITE); }
        });
        return btn;
    }
    
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { closeBtn.setForeground(new Color(255, 200, 200)); }
            public void mouseExited(MouseEvent e) { closeBtn.setForeground(Color.WHITE); }
        });
        return closeBtn;
    }
    
    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(surfaceColor);
        content.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        
        // Language Setting
        content.add(createSettingCard("Language", "Select your preferred language", createLanguagePanel()));
        content.add(Box.createVerticalStrut(20));
        
        // Theme Setting
        content.add(createSettingCard("Appearance", "Toggle between light and dark mode", createThemePanel()));
        content.add(Box.createVerticalStrut(20));
        
        // Notifications Setting
        content.add(createSettingCard("Notifications", "Enable or disable notifications", createNotificationPanel()));
        
        return content;
    }
    
    private JPanel createSettingCard(String title, String description, JPanel controlPanel) {
        JPanel card = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(borderColor);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        JPanel textPanel = new JPanel(new BorderLayout(0, 4));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_BODY_BOLD);
        titleLabel.setForeground(textColor);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(UIConstants.FONT_CAPTION);
        descLabel.setForeground(textSecondary);
        
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.SOUTH);
        
        card.add(textPanel, BorderLayout.CENTER);
        card.add(controlPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createLanguagePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setOpaque(false);
        
        languageComboBox = new JComboBox<>(new String[]{"English", "Arabic", "Persian", "Urdu"});
        languageComboBox.setFont(UIConstants.FONT_BODY);
        languageComboBox.setPreferredSize(new Dimension(130, 36));
        languageComboBox.setSelectedItem(defaultLanguage);
        
        // Style the combo box
        languageComboBox.setBackground(cardColor);
        languageComboBox.setForeground(textColor);
        languageComboBox.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        
        panel.add(languageComboBox);
        return panel;
    }
    
    private JPanel createThemePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setOpaque(false);
        
        String buttonText = isDarkMode ? UIConstants.ICON_SUN + " Light" : UIConstants.ICON_MOON + " Dark";
        themeToggleButton = new ModernButton(buttonText, ModernButton.ButtonStyle.OUTLINE);
        themeToggleButton.setDarkMode(isDarkMode);
        themeToggleButton.setPreferredSize(new Dimension(110, 36));
        themeToggleButton.addActionListener(e -> toggleTheme());
        
        panel.add(themeToggleButton);
        return panel;
    }
    
    private JPanel createNotificationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setOpaque(false);
        
        // Custom toggle switch
        notificationsCheckBox = new JCheckBox();
        notificationsCheckBox.setSelected(defaultNotifications);
        notificationsCheckBox.setOpaque(false);
        notificationsCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Use a custom toggle button instead
        ModernButton toggleBtn = new ModernButton(defaultNotifications ? "ON" : "OFF", 
            defaultNotifications ? ModernButton.ButtonStyle.SUCCESS : ModernButton.ButtonStyle.OUTLINE);
        toggleBtn.setDarkMode(isDarkMode);
        toggleBtn.setPreferredSize(new Dimension(70, 36));
        toggleBtn.addActionListener(e -> {
            boolean newState = !notificationsCheckBox.isSelected();
            notificationsCheckBox.setSelected(newState);
            toggleBtn.setText(newState ? "ON" : "OFF");
            toggleBtn.setStyle(newState ? ModernButton.ButtonStyle.SUCCESS : ModernButton.ButtonStyle.OUTLINE);
        });
        
        panel.add(toggleBtn);
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(surfaceColor);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 25, 30));
        
        ModernButton saveBtn = new ModernButton(UIConstants.ICON_CHECK, "Save", ModernButton.ButtonStyle.SUCCESS);
        saveBtn.setDarkMode(isDarkMode);
        saveBtn.setPreferredSize(new Dimension(120, 44));
        saveBtn.addActionListener(e -> saveSettings());
        
        ModernButton resetBtn = new ModernButton("Reset", ModernButton.ButtonStyle.DANGER);
        resetBtn.setDarkMode(isDarkMode);
        resetBtn.setPreferredSize(new Dimension(100, 44));
        resetBtn.addActionListener(e -> resetSettings());
        
        ModernButton cancelBtn = new ModernButton("Close", ModernButton.ButtonStyle.TEXT);
        cancelBtn.setDarkMode(isDarkMode);
        cancelBtn.setPreferredSize(new Dimension(100, 44));
        cancelBtn.addActionListener(e -> dispose());
        
        footer.add(saveBtn);
        footer.add(resetBtn);
        footer.add(cancelBtn);
        
        return footer;
    }
    
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        if (onThemeChange != null) {
            onThemeChange.run();
        }
        
        // Update button text
        String buttonText = isDarkMode ? UIConstants.ICON_SUN + " Light" : UIConstants.ICON_MOON + " Dark";
        themeToggleButton.setText(buttonText);
        themeToggleButton.setDarkMode(isDarkMode);
        
        // Refresh the dialog
        updateColors();
        dispose();
        new ModernSettingsView(parentFrame, isDarkMode, onThemeChange);
    }
    
    private void saveSettings() {
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        boolean notificationsEnabled = notificationsCheckBox.isSelected();
        
        JOptionPane.showMessageDialog(this,
            "Settings Saved:\n• Language: " + selectedLanguage + "\n• Notifications: " + 
            (notificationsEnabled ? "Enabled" : "Disabled") + "\n• Theme: " + 
            (isDarkMode ? "Dark" : "Light"),
            "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetSettings() {
        languageComboBox.setSelectedItem(defaultLanguage);
        notificationsCheckBox.setSelected(defaultNotifications);
        JOptionPane.showMessageDialog(this, "Settings have been reset to defaults.",
            "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addWindowDragSupport(JPanel panel) {
        final Point[] dragPoint = {null};
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { dragPoint[0] = e.getPoint(); }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point location = getLocation();
                setLocation(location.x + e.getX() - dragPoint[0].x, location.y + e.getY() - dragPoint[0].y);
            }
        });
    }
}
