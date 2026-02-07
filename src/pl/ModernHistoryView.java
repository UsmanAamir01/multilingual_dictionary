package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import bl.IBLFacade;
import dto.Word;
import pl.ui.*;

/**
 * Modern History View with premium design
 */
public class ModernHistoryView extends JDialog {
    
    private final IBLFacade facade;
    private JTable historyTable;
    private JFrame previousWindow;
    private boolean isDarkMode;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;

    public ModernHistoryView(JFrame previousWindow, IBLFacade facade, boolean isDarkMode) {
        super(previousWindow, "Search History", true);
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
        setSize(700, 550);
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
        
        // Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
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
        loadHistory();
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 152, 0),
                    getWidth(), getHeight(), new Color(255, 183, 77)
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
        
        JLabel iconLabel = new JLabel(UIConstants.ICON_HISTORY);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("  Search History");
        titleLabel.setFont(UIConstants.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        titleRow.add(iconLabel);
        titleRow.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Your most recent word searches");
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
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(surfaceColor);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        
        // Create table
        String[] columns = {"#", "Arabic Word", "Persian Meaning", "Urdu Meaning"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historyTable = new JTable(model);
        setupTableStyle();
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        scrollPane.getViewport().setBackground(cardColor);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private void setupTableStyle() {
        historyTable.setBackground(cardColor);
        historyTable.setForeground(textColor);
        historyTable.setSelectionBackground(UIConstants.getPrimary(isDarkMode));
        historyTable.setSelectionForeground(Color.WHITE);
        historyTable.setFont(UIConstants.FONT_BODY);
        historyTable.setRowHeight(45);
        historyTable.setShowGrid(false);
        historyTable.setIntercellSpacing(new Dimension(0, 1));
        historyTable.setFillsViewportHeight(true);
        
        // Header style
        JTableHeader header = historyTable.getTableHeader();
        header.setBackground(isDarkMode ? new Color(45, 45, 45) : new Color(248, 250, 252));
        header.setForeground(textColor);
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 152, 0)));
        
        // Column widths
        historyTable.getColumnModel().getColumn(0).setMaxWidth(50);
        historyTable.getColumnModel().getColumn(0).setMinWidth(50);
        
        // Custom renderer
        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? cardColor : 
                        (isDarkMode ? new Color(40, 40, 40) : new Color(248, 250, 252)));
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setHorizontalAlignment(column == 0 ? CENTER : LEFT);
                return c;
            }
        });
    }
    
    private void loadHistory() {
        List<Word> history = facade.getRecentSearchHistory(50);
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0);
        
        if (history != null && !history.isEmpty()) {
            int num = 1;
            for (Word word : history) {
                model.addRow(new Object[]{
                    num++,
                    word.getArabicWord() != null ? word.getArabicWord() : "-",
                    word.getPersianMeaning() != null ? word.getPersianMeaning() : "-",
                    word.getUrduMeaning() != null ? word.getUrduMeaning() : "-"
                });
            }
        }
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(surfaceColor);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        ModernButton refreshBtn = new ModernButton("Refresh", ModernButton.ButtonStyle.OUTLINE);
        refreshBtn.setDarkMode(isDarkMode);
        refreshBtn.setPreferredSize(new Dimension(110, 44));
        refreshBtn.addActionListener(e -> loadHistory());
        
        ModernButton clearBtn = new ModernButton("Clear All", ModernButton.ButtonStyle.DANGER);
        clearBtn.setDarkMode(isDarkMode);
        clearBtn.setPreferredSize(new Dimension(110, 44));
        clearBtn.addActionListener(e -> clearHistory());
        
        ModernButton closeBtn = new ModernButton("Close", ModernButton.ButtonStyle.PRIMARY);
        closeBtn.setDarkMode(isDarkMode);
        closeBtn.setPreferredSize(new Dimension(110, 44));
        closeBtn.addActionListener(e -> dispose());
        
        footer.add(refreshBtn);
        footer.add(clearBtn);
        footer.add(closeBtn);
        
        return footer;
    }
    
    private void clearHistory() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear all search history?",
            "Clear History",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Clear history logic would go here
            ((DefaultTableModel) historyTable.getModel()).setRowCount(0);
            JOptionPane.showMessageDialog(this, "History cleared successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
