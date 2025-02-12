package pl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import bl.IBLFacade;
import dto.Word;

public class AllWordView extends JFrame {
    private JTable table;
    private JLabel headingLabel, posLabel, stemLabel, lemmaLabel;
    private final IBLFacade facade;
    private JFrame previousWindow;
    private JButton backButton, updateButton, removeButton;

    public AllWordView(IBLFacade facade, JFrame previousWindow) {
        this.facade = facade;
        this.previousWindow = previousWindow;

        String[][] wordData = facade.getWordsWithMeanings();
        String[] columnNames = { "Arabic Word", "Urdu Meaning", "Persian Meaning", "Favourites" };
        DefaultTableModel model = new DefaultTableModel(wordData, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Icon.class : super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 1 && column != 2;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTableStyles();

        headingLabel = createLabel("Word and Meanings", new Font("Arial", Font.BOLD, 24), new Color(25, 25, 112),
                SwingConstants.CENTER);

        posLabel = createMeaningLabel("POS (Part Of Speech): ");
        stemLabel = createMeaningLabel("Stem Word: ");
        lemmaLabel = createMeaningLabel("Lemma: ");

        setLayout(new BorderLayout(10, 10));
        add(headingLabel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setTitle("Word and Meaning Viewer");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1)
					try {
						updateLabels(selectedRow);
					} catch (Exception e) {
						e.printStackTrace();
					}
            }
        });

        setFavoriteIcons();
    }

    private void setTableStyles() {
        table.setBackground(new Color(250, 250, 250));
        table.setForeground(new Color(40, 40, 40));
        table.setSelectionBackground(new Color(200, 230, 201));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(35);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(63, 81, 181));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(30, 136, 229)));
    }

    private void updateLabels(int selectedRow) throws Exception {
        String wordText = (String) table.getValueAt(selectedRow, 0);
        String result = facade.processWord(wordText);
        if (result != null) {
            String[] parts = result.split(",");
            String stem = parts[0];
            String lemma = parts[1];
            String pos = parts[2];

            posLabel.setText("Part Of Speech: " + pos);
            stemLabel.setText("Stem Word: " + stem);
            lemmaLabel.setText("Lemma: " + lemma);
        }
    }

    private JLabel createLabel(String text, Font font, Color color, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JLabel createMeaningLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(new Color(173, 216, 230));
        label.setForeground(Color.DARK_GRAY);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return label;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(248, 248, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        labelsPanel.setBackground(new Color(248, 248, 255));
        labelsPanel.add(posLabel);
        labelsPanel.add(stemLabel);
        labelsPanel.add(lemmaLabel);

        footerPanel.add(labelsPanel);

        backButton = createButton("Back", new Color(30, 136, 229), e -> {
            previousWindow.setVisible(true);
            dispose();
        });

        updateButton = createButton("Update Word", new Color(0, 128, 0), e -> updateWord());
        removeButton = createButton("Remove Word", new Color(255, 0, 0), e -> removeWord());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);

        footerPanel.add(buttonPanel);

        return footerPanel;
    }

    private JButton createButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 40));
        button.addActionListener(action);
        return button;
    }

    private void updateWord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String wordText = (String) table.getValueAt(selectedRow, 0);
            String newUrdu = JOptionPane.showInputDialog("Enter new Urdu meaning:");
            String newPersian = JOptionPane.showInputDialog("Enter new Persian meaning:");
            if (newUrdu != null && newPersian != null) {
                Word word = new Word(wordText, newUrdu, newPersian);
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        if (facade.updateWord(word)) {
                            JOptionPane.showMessageDialog(this, "Word updated successfully!");
                            table.setValueAt(newUrdu, selectedRow, 1);
                            table.setValueAt(newPersian, selectedRow, 2);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update the word.");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a word to update.");
        }
    }

    private void removeWord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String wordText = (String) table.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this word?", "Confirm Remove",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        if (facade.removeWord(wordText)) {
                            JOptionPane.showMessageDialog(this, "Word removed successfully!");
                            ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to remove the word.");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a word to remove.");
        }
    }

    private void setFavoriteIcons() {
        for (int row = 0; row < table.getRowCount(); row++) {
            String word = (String) table.getValueAt(row, 0);
            boolean isFavorite = facade.isWordFavorite(word);
            Icon icon = new ImageIcon(System.getProperty("user.dir") + 
                    (isFavorite ? "/images/icon_filledstar.png" : "/images/icon_hollowstar.png"));
            table.setValueAt(icon, row, 3);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedRow = table.rowAtPoint(e.getPoint());
                int clickedColumn = table.columnAtPoint(e.getPoint());

                if (clickedRow != -1) {
                    if (clickedColumn == 0 && e.getClickCount() == 2) {
                        String arabicWord = (String) table.getValueAt(clickedRow, clickedColumn);

                        AllWordView.this.setVisible(false);
                        ViewOnceWordView viewOnceWordView = new ViewOnceWordView(facade, AllWordView.this, arabicWord);
                        viewOnceWordView.setVisible(true);
                    }

                    if (clickedColumn == 3 && e.getClickCount() == 1) {
                        String word = (String) table.getValueAt(clickedRow, 0);
                        boolean isFavorite = facade.isWordFavorite(word);
                        boolean newStatus = !isFavorite;

                        facade.markWordAsFavorite(word, newStatus);

                        Icon newIcon = new ImageIcon(System.getProperty("user.dir") +
                                (newStatus ? "/images/icon_filledstar.png" : "/images/icon_hollowstar.png"));
                        table.setValueAt(newIcon, clickedRow, 3);
                    }
                }
            }
        });
    }


}