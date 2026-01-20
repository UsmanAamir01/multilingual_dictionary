package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import pl.ui.*;

/**
 * Modern FAQs View with premium design
 */
public class ModernFAQsView extends JDialog {
    
    private JFrame parentFrame;
    private boolean isDarkMode;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;
    
    // FAQ data
    private final String[][] faqs = {
        {"What is this dictionary app?", "This app allows you to search for words in multiple languages, including Arabic, Persian, and Urdu. It provides translations, word stemming, and lemmatization features."},
        {"How can I add a word?", "Navigate to the 'Add Word' section from the sidebar. Enter the Arabic word along with its Urdu and Persian meanings, then click 'Add Word'."},
        {"How does word segmentation work?", "Word segmentation breaks down words into their base components using linguistic analysis. This helps improve search accuracy and provides insights into word structure."},
        {"Can I save words to favorites?", "Yes! Click the star icon next to any word in the word list to add it to your favorites. Access your favorites anytime from the sidebar."},
        {"How do I change the app theme?", "Click the moon/sun icon in the top right corner of the dashboard to toggle between light and dark modes. Your preference is saved automatically."},
        {"What languages are supported?", "The dictionary currently supports Arabic, Urdu, and Persian. You can search in any of these languages and get translations in the others."},
        {"How do I update or remove a word?", "Go to 'All Words', select the word you want to modify, then use the Update or Remove buttons at the bottom of the screen."}
    };

    public ModernFAQsView(JFrame parentFrame, boolean isDarkMode) {
        super(parentFrame, "FAQs", true);
        this.parentFrame = parentFrame;
        this.isDarkMode = isDarkMode;
        
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
        setSize(650, 600);
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
        
        // FAQ Content
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        // Footer
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
                    0, 0, new Color(0, 150, 136),
                    getWidth(), getHeight(), new Color(38, 166, 154)
                );
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() + 20, 20, 20));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JLabel iconLabel = new JLabel(UIConstants.ICON_FAQ);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Frequently Asked Questions");
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
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        
        for (int i = 0; i < faqs.length; i++) {
            content.add(createFAQItem(i + 1, faqs[i][0], faqs[i][1]));
            if (i < faqs.length - 1) {
                content.add(Box.createVerticalStrut(12));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(surfaceColor);
        scrollPane.getViewport().setBackground(surfaceColor);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createFAQItem(int number, String question, String answer) {
        JPanel item = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(borderColor);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Question with number badge
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        questionPanel.setOpaque(false);
        
        JLabel numberLabel = new JLabel(String.valueOf(number)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIConstants.getPrimary(isDarkMode));
                g2.fillOval(0, 0, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        numberLabel.setFont(UIConstants.FONT_BODY_BOLD);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setPreferredSize(new Dimension(24, 24));
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel questionLabel = new JLabel(question);
        questionLabel.setFont(UIConstants.FONT_BODY_BOLD);
        questionLabel.setForeground(textColor);
        
        questionPanel.add(numberLabel);
        questionPanel.add(questionLabel);
        
        // Answer
        JTextArea answerArea = new JTextArea(answer);
        answerArea.setFont(UIConstants.FONT_BODY);
        answerArea.setForeground(textSecondary);
        answerArea.setOpaque(false);
        answerArea.setEditable(false);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(BorderFactory.createEmptyBorder(0, 34, 0, 0));
        
        item.add(questionPanel, BorderLayout.NORTH);
        item.add(answerArea, BorderLayout.CENTER);
        
        return item;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setBackground(surfaceColor);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        ModernButton closeBtn = new ModernButton("Close", ModernButton.ButtonStyle.PRIMARY);
        closeBtn.setDarkMode(isDarkMode);
        closeBtn.setPreferredSize(new Dimension(120, 44));
        closeBtn.addActionListener(e -> dispose());
        
        footer.add(closeBtn);
        
        return footer;
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
