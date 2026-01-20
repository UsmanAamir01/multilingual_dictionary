package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import pl.ui.*;

/**
 * Modern Instructions View with premium design
 */
public class ModernInstructionsView extends JDialog {
    
    private JFrame parentFrame;
    private boolean isDarkMode;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;
    
    // Instruction data
    private final String[][] instructions = {
        {UIConstants.ICON_MENU, "Navigation", "Use the sidebar on the left to navigate between different sections. Click any menu item to access that feature."},
        {UIConstants.ICON_SEARCH, "Search Words", "Select a language from the dropdown, enter your search term, and click Search or press Enter. Results will show translations and meanings."},
        {UIConstants.ICON_ADD, "Add New Words", "Click 'Add Word' in the sidebar. Fill in the Arabic word and its Urdu and Persian translations, then save."},
        {UIConstants.ICON_STAR, "Favorites", "Click the star icon next to any word to save it to your favorites for quick access later."},
        {UIConstants.ICON_MOON, "Theme Toggle", "Click the sun/moon icon at the top right to switch between light and dark modes."},
        {UIConstants.ICON_IMPORT, "Import Words", "Use the 'Import' feature to bulk import words from external files."},
        {UIConstants.ICON_NORMALIZE, "Word Processing", "Use normalization and segmentation features to analyze Arabic word structures."},
        {UIConstants.ICON_HISTORY, "View History", "Access your recent searches through the History section in the sidebar."}
    };

    public ModernInstructionsView(JFrame parentFrame, boolean isDarkMode) {
        super(parentFrame, "Instructions", true);
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
        setSize(650, 620);
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
        
        // Instructions content
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
                    0, 0, UIConstants.getPrimary(isDarkMode),
                    getWidth(), getHeight(), new Color(33, 150, 243)
                );
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() + 20, 20, 20));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 90));
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JPanel titlePanel = new JPanel(new BorderLayout(0, 5));
        titlePanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(UIConstants.ICON_INFO);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("  How to Use");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        titleRow.add(iconLabel);
        titleRow.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Complete guide to using the Multilingual Dictionary");
        subtitleLabel.setFont(UIConstants.FONT_BODY);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(titleRow, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
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
        
        for (int i = 0; i < instructions.length; i++) {
            content.add(createInstructionItem(instructions[i][0], instructions[i][1], instructions[i][2]));
            if (i < instructions.length - 1) {
                content.add(Box.createVerticalStrut(10));
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
    
    private JPanel createInstructionItem(String icon, String title, String description) {
        JPanel item = new JPanel(new BorderLayout(15, 0)) {
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
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Icon
        JLabel iconLabel = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(UIConstants.getPrimary(isDarkMode).getRed(),
                                     UIConstants.getPrimary(isDarkMode).getGreen(),
                                     UIConstants.getPrimary(isDarkMode).getBlue(), 30));
                g2.fillOval(0, 0, 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(UIConstants.getPrimary(isDarkMode));
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Text panel
        JPanel textPanel = new JPanel(new BorderLayout(0, 4));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_BODY_BOLD);
        titleLabel.setForeground(textColor);
        
        JLabel descLabel = new JLabel("<html><body style='width: 420px'>" + description + "</body></html>");
        descLabel.setFont(UIConstants.FONT_CAPTION);
        descLabel.setForeground(textSecondary);
        
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.CENTER);
        
        item.add(iconLabel, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        
        return item;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setBackground(surfaceColor);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        ModernButton closeBtn = new ModernButton("Got It!", ModernButton.ButtonStyle.PRIMARY);
        closeBtn.setDarkMode(isDarkMode);
        closeBtn.setPreferredSize(new Dimension(130, 44));
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
