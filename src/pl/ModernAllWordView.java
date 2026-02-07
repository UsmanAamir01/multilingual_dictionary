package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.table.*;
import bl.IBLFacade;
import dto.Word;
import pl.ui.*;

/**
 * Modern All Words View with premium table design
 */
public class ModernAllWordView extends JFrame {
    private JTable table;
    private JLabel posLabel, stemLabel, lemmaLabel;
    private final IBLFacade facade;
    private JFrame previousWindow;
    private ModernButton backButton, updateButton, removeButton;
    private ModernTextField searchField;
    private boolean isDarkMode = false;
    
    // Theme colors
    private Color bgColor, surfaceColor, cardColor, textColor, textSecondary, borderColor;

    public ModernAllWordView(IBLFacade facade, JFrame previousWindow, boolean isDarkMode) {
        this.facade = facade;
        this.previousWindow = previousWindow;
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
        setTitle("All Words - Multilingual Dictionary");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(bgColor);
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Content with table
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        // Footer with info and buttons
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        loadTableData();
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
                    getWidth(), 0, new Color(103, 58, 183)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Left side - title
        JPanel titlePanel = new JPanel(new BorderLayout(0, 5));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(UIConstants.ICON_BOOK + "  All Words");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Browse and manage your dictionary");
        subtitleLabel.setFont(UIConstants.FONT_BODY);
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Right side - search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);
        
        searchField = new ModernTextField("Search words...", 20);
        searchField.setDarkMode(false); // Keep light in header
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });
        
        searchPanel.add(searchField);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Table card
        CardPanel tableCard = new CardPanel(new BorderLayout());
        tableCard.setDarkMode(isDarkMode);
        tableCard.setElevation(3);
        
        // Create table
        String[] columnNames = {"Arabic Word", "Urdu Meaning", "Persian Meaning", "Favorite"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class; // Use String for all columns including star
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        setupTableStyle();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(cardColor);
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);
        
        return content;
    }
    
    private void setupTableStyle() {
        table.setBackground(cardColor);
        table.setForeground(textColor);
        table.setSelectionBackground(UIConstants.getPrimary(isDarkMode));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(UIConstants.FONT_BODY);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Header style
        JTableHeader header = table.getTableHeader();
        header.setBackground(isDarkMode ? new Color(45, 45, 45) : new Color(248, 250, 252));
        header.setForeground(textColor);
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UIConstants.getPrimary(isDarkMode)));
        
        // Custom renderer for alternating rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? cardColor : 
                        (isDarkMode ? new Color(40, 40, 40) : new Color(248, 250, 252)));
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                
                // Special styling for star column
                if (column == 3) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel) c).setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
                    ((JLabel) c).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                return c;
            }
        });
        
        // Row selection listener
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    updateWordInfo(selectedRow);
                }
            }
        });
        
        // Double-click to view word details OR single-click on favorite column to toggle
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int viewRow = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                if (viewRow == -1) return;
                
                // Convert view row to model row (important when table is sorted/filtered)
                int modelRow = viewRow;
                if (table.getRowSorter() != null) {
                    modelRow = table.convertRowIndexToModel(viewRow);
                }
                
                // Single click on Favorite column (column 3) to toggle favorite
                if (col == 3) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String word = (String) model.getValueAt(modelRow, 0);
                    boolean currentlyFavorite = facade.isWordFavorite(word);
                    boolean newStatus = !currentlyFavorite;
                    
                    // Update database
                    facade.markWordAsFavorite(word, newStatus);
                    
                    // Update the star icon in the table model
                    model.setValueAt(getStarLabel(newStatus), modelRow, 3);
                    
                    // Show feedback
                    System.out.println("Toggled favorite for: " + word + " -> " + newStatus);
                }
                
                // Double-click on first column to view word details
                if (e.getClickCount() == 2 && col == 0) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String word = (String) model.getValueAt(modelRow, 0);
                    // Open word detail view
                }
            }
        });
        
        // Change cursor when hovering over star column
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col == 3) {
                    table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout(20, 15));
        footer.setBackground(bgColor);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        
        // Word info card
        CardPanel infoCard = new CardPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        infoCard.setDarkMode(isDarkMode);
        infoCard.setElevation(2);
        infoCard.setPreferredSize(new Dimension(0, 70));
        
        posLabel = createInfoLabel("Part of Speech: --");
        stemLabel = createInfoLabel("Stem: --");
        lemmaLabel = createInfoLabel("Lemma: --");
        
        infoCard.add(posLabel);
        infoCard.add(createSeparator());
        infoCard.add(stemLabel);
        infoCard.add(createSeparator());
        infoCard.add(lemmaLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        backButton = new ModernButton(UIConstants.ICON_BACK, "Back", ModernButton.ButtonStyle.PRIMARY);
        backButton.setDarkMode(isDarkMode);
        backButton.addActionListener(e -> handleBack());
        
        updateButton = new ModernButton("Update", ModernButton.ButtonStyle.SUCCESS);
        updateButton.setDarkMode(isDarkMode);
        updateButton.addActionListener(e -> handleUpdate());
        
        removeButton = new ModernButton("Remove", ModernButton.ButtonStyle.DANGER);
        removeButton.setDarkMode(isDarkMode);
        removeButton.addActionListener(e -> handleRemove());
        
        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        
        footer.add(infoCard, BorderLayout.CENTER);
        footer.add(buttonPanel, BorderLayout.SOUTH);
        
        return footer;
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_BODY);
        label.setForeground(textColor);
        label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return label;
    }
    
    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 30));
        sep.setForeground(borderColor);
        return sep;
    }
    
    private void loadTableData() {
        String[][] wordData = facade.getWordsWithMeanings();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        if (wordData != null) {
            for (String[] row : wordData) {
                model.addRow(new Object[]{row[0], row[1], row[2], getStarIcon(row[0])});
            }
        }
    }
    
    private String getStarLabel(boolean isFavorite) {
        return isFavorite ? "⭐" : "☆";
    }
    
    private String getStarIcon(String word) {
        boolean isFavorite = facade.isWordFavorite(word);
        return getStarLabel(isFavorite);
    }
    
    private void filterTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }
    
    private void updateWordInfo(int row) {
        try {
            String word = (String) table.getValueAt(row, 0);
            String result = facade.processWord(word);
            if (result != null) {
                String[] parts = result.split(",");
                stemLabel.setText("Stem: " + (parts.length > 0 ? parts[0] : "--"));
                lemmaLabel.setText("Lemma: " + (parts.length > 1 ? parts[1] : "--"));
                posLabel.setText("Part of Speech: " + (parts.length > 2 ? parts[2] : "--"));
            }
        } catch (Exception e) {
            // Handle gracefully
        }
    }
    
    private void handleBack() {
        dispose();
        previousWindow.setVisible(true);
    }
    
    private void handleUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a word to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String word = (String) table.getValueAt(selectedRow, 0);
        String newUrdu = JOptionPane.showInputDialog(this, "Enter new Urdu meaning:", table.getValueAt(selectedRow, 1));
        if (newUrdu == null) return;
        
        String newPersian = JOptionPane.showInputDialog(this, "Enter new Persian meaning:", table.getValueAt(selectedRow, 2));
        if (newPersian == null) return;
        
        Word updatedWord = new Word(word, newUrdu, newPersian);
        if (facade.updateWord(updatedWord)) {
            table.setValueAt(newUrdu, selectedRow, 1);
            table.setValueAt(newPersian, selectedRow, 2);
            JOptionPane.showMessageDialog(this, "Word updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update word.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRemove() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a word to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String word = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove \"" + word + "\"?", 
            "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.removeWord(word)) {
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Word removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove word.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
