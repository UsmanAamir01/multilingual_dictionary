package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import bl.IBLFacade;
import dto.Word;
import pl.ui.*;

/**
 * Modern Favorites View with premium design
 */
public class ModernFavoritesView extends JDialog {
    
    private IBLFacade facade;
    private JList<String> favoritesList;
    private DefaultListModel<String> listModel;
    private JFrame previousWindow;
    private boolean isDarkMode;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;

    public ModernFavoritesView(JFrame previousWindow, IBLFacade facade, boolean isDarkMode) {
        super(previousWindow, "Favorites", true);
        this.previousWindow = previousWindow;
        this.facade = facade;
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
        setSize(550, 500);
        setLocationRelativeTo(previousWindow);
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
        
        // List
        mainPanel.add(createListPanel(), BorderLayout.CENTER);
        
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
        loadFavorites();
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 193, 7),
                    getWidth(), getHeight(), new Color(255, 160, 0)
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
        
        JLabel iconLabel = new JLabel(UIConstants.ICON_STAR);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("  My Favorites");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        titleRow.add(iconLabel);
        titleRow.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Words you've saved for quick access");
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
    
    private JPanel createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(surfaceColor);
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        
        // Create list
        listModel = new DefaultListModel<>();
        favoritesList = new JList<>(listModel);
        favoritesList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        favoritesList.setBackground(cardColor);
        favoritesList.setForeground(textColor);
        favoritesList.setSelectionBackground(UIConstants.getPrimary(isDarkMode));
        favoritesList.setSelectionForeground(Color.WHITE);
        favoritesList.setFixedCellHeight(50);
        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom cell renderer
        favoritesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                
                if (!isSelected) {
                    label.setBackground(index % 2 == 0 ? cardColor : 
                        (isDarkMode ? new Color(40, 40, 40) : new Color(248, 250, 252)));
                }
                
                // Add star icon
                label.setText(UIConstants.ICON_STAR + "  " + value.toString());
                
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(favoritesList);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        scrollPane.getViewport().setBackground(cardColor);
        
        // Empty state
        if (listModel.isEmpty()) {
            JLabel emptyLabel = new JLabel("No favorites yet. Add words to your favorites from the All Words view.");
            emptyLabel.setFont(UIConstants.FONT_BODY);
            emptyLabel.setForeground(textSecondary);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            listPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            listPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        return listPanel;
    }
    
    private void loadFavorites() {
        listModel.clear();
        List<Word> favoriteWords = facade.getFavoriteWords();
        
        if (favoriteWords != null && !favoriteWords.isEmpty()) {
            for (Word word : favoriteWords) {
                if (word.getArabicWord() != null) {
                    listModel.addElement(word.getArabicWord());
                }
            }
        }
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(surfaceColor);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        ModernButton viewBtn = new ModernButton("View Details", ModernButton.ButtonStyle.SUCCESS);
        viewBtn.setDarkMode(isDarkMode);
        viewBtn.setPreferredSize(new Dimension(130, 44));
        viewBtn.addActionListener(e -> viewSelectedWord());
        
        ModernButton removeBtn = new ModernButton("Remove", ModernButton.ButtonStyle.DANGER);
        removeBtn.setDarkMode(isDarkMode);
        removeBtn.setPreferredSize(new Dimension(110, 44));
        removeBtn.addActionListener(e -> removeSelectedFavorite());
        
        ModernButton closeBtn = new ModernButton("Close", ModernButton.ButtonStyle.PRIMARY);
        closeBtn.setDarkMode(isDarkMode);
        closeBtn.setPreferredSize(new Dimension(110, 44));
        closeBtn.addActionListener(e -> dispose());
        
        footer.add(viewBtn);
        footer.add(removeBtn);
        footer.add(closeBtn);
        
        return footer;
    }
    
    private void viewSelectedWord() {
        String selected = favoritesList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a word to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get word details
        String result = facade.getMeanings(selected, "Arabic");
        if (result != null) {
            JOptionPane.showMessageDialog(this, 
                "Word: " + selected + "\n\n" + result, 
                "Word Details", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void removeSelectedFavorite() {
        String selected = favoritesList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a word to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remove \"" + selected + "\" from favorites?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            facade.markWordAsFavorite(selected, false);
            listModel.removeElement(selected);
            JOptionPane.showMessageDialog(this, "Removed from favorites.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
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
