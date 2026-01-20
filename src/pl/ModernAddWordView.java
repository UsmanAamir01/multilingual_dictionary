package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import bl.IBLFacade;
import dto.Word;
import pl.ui.*;

/**
 * Modern Add Word View with premium design
 */
public class ModernAddWordView extends JDialog {
    private ModernTextField arabicWordTextField, urduMeaningTextField, persianMeaningTextField;
    private ModernButton addButton, clearButton, cancelButton;
    private JFrame previousWindow;
    private final IBLFacade facade;
    private boolean isDarkMode = false;
    
    // Theme colors
    private Color bgColor, surfaceColor, textColor, textSecondary;

    public ModernAddWordView(IBLFacade facade, JFrame previousWindow, boolean isDarkMode) {
        super(previousWindow, "Add New Word", true);
        this.facade = facade;
        this.previousWindow = previousWindow;
        this.isDarkMode = isDarkMode;
        
        updateColors();
        initializeUI();
    }
    
    private void updateColors() {
        bgColor = UIConstants.getBackground(isDarkMode);
        surfaceColor = UIConstants.getSurface(isDarkMode);
        textColor = UIConstants.getTextPrimary(isDarkMode);
        textSecondary = UIConstants.getTextSecondary(isDarkMode);
    }

    private void initializeUI() {
        setSize(500, 520);
        setLocationRelativeTo(previousWindow);
        setResizable(false);
        setUndecorated(true);
        
        // Main panel with rounded corners
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Header with gradient
        JPanel headerPanel = createHeaderPanel();
        
        // Form content
        JPanel contentPanel = createContentPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add shadow border
        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setBackground(new Color(0, 0, 0, 0));
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 15));
        shadowPanel.setOpaque(false);
        shadowPanel.add(mainPanel);
        
        setContentPane(shadowPanel);
        setBackground(new Color(0, 0, 0, 0));
        
        // Add window drag support
        addWindowDragSupport(headerPanel);
        
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIConstants.getPrimary(isDarkMode),
                    getWidth(), getHeight(), UIConstants.LIGHT_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() + 20, 20, 20));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        // Icon and title
        JLabel iconLabel = new JLabel(UIConstants.ICON_ADD);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Add New Word");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        
        // Close button
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> handleClose());
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(new Color(255, 200, 200));
            }
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(Color.WHITE);
            }
        });
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(surfaceColor);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        
        // Arabic Word field
        content.add(createFieldPanel("Arabic Word", "Enter Arabic word...", 
            arabicWordTextField = createStyledTextField("Enter Arabic word...")));
        content.add(Box.createVerticalStrut(20));
        
        // Urdu Meaning field
        content.add(createFieldPanel("Urdu Meaning", "Enter Urdu meaning...", 
            urduMeaningTextField = createStyledTextField("Enter Urdu meaning...")));
        content.add(Box.createVerticalStrut(20));
        
        // Persian Meaning field
        content.add(createFieldPanel("Persian Meaning", "Enter Persian meaning...", 
            persianMeaningTextField = createStyledTextField("Enter Persian meaning...")));
        
        return content;
    }
    
    private JPanel createFieldPanel(String label, String placeholder, ModernTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(UIConstants.FONT_BODY_BOLD);
        fieldLabel.setForeground(textColor);
        
        panel.add(fieldLabel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }
    
    private ModernTextField createStyledTextField(String placeholder) {
        ModernTextField field = new ModernTextField(placeholder);
        field.setDarkMode(isDarkMode);
        field.setPreferredSize(new Dimension(0, 48));
        return field;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(surfaceColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 25, 30));
        
        addButton = new ModernButton(UIConstants.ICON_CHECK, "Add Word", ModernButton.ButtonStyle.SUCCESS);
        addButton.setDarkMode(isDarkMode);
        addButton.setPreferredSize(new Dimension(140, 44));
        addButton.addActionListener(e -> handleAddWord());
        
        clearButton = new ModernButton("Clear", ModernButton.ButtonStyle.OUTLINE);
        clearButton.setDarkMode(isDarkMode);
        clearButton.setPreferredSize(new Dimension(100, 44));
        clearButton.addActionListener(e -> handleClear());
        
        cancelButton = new ModernButton("Cancel", ModernButton.ButtonStyle.TEXT);
        cancelButton.setDarkMode(isDarkMode);
        cancelButton.setPreferredSize(new Dimension(100, 44));
        cancelButton.addActionListener(e -> handleClose());
        
        panel.add(addButton);
        panel.add(clearButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void handleAddWord() {
        String arabicWord = arabicWordTextField.getText().trim();
        String urduMeaning = urduMeaningTextField.getText().trim();
        String persianMeaning = persianMeaningTextField.getText().trim();
        
        if (arabicWord.isEmpty() || urduMeaning.isEmpty() || persianMeaning.isEmpty()) {
            showMessage("Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Word word = new Word(arabicWord, urduMeaning, persianMeaning);
        boolean success = facade.addWord(word);
        
        if (success) {
            showMessage("Word added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            handleClear();
        } else {
            showMessage("Failed to add the word. It may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleClear() {
        arabicWordTextField.setText("");
        urduMeaningTextField.setText("");
        persianMeaningTextField.setText("");
        arabicWordTextField.requestFocus();
    }
    
    private void handleClose() {
        dispose();
        if (previousWindow != null) {
            previousWindow.setVisible(true);
        }
    }
    
    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    
    private void addWindowDragSupport(JPanel panel) {
        final Point[] dragPoint = {null};
        
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });
        
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point location = getLocation();
                setLocation(location.x + e.getX() - dragPoint[0].x,
                           location.y + e.getY() - dragPoint[0].y);
            }
        });
    }
}
