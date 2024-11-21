package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;

public class HistoryView extends JFrame {
    private final IBLFacade facade;
    private JTable historyTable;
    private JFrame previousWindow;

    public HistoryView(JFrame previousWindow, IBLFacade facade) {
        this.previousWindow = previousWindow;
        this.facade = facade;

        setTitle("Search History");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JButton backButton = createStyledButton("Back", new Color(0, 123, 255));
        backButton.addActionListener(e -> handleBackAction());
        topPanel.add(backButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        JLabel tableTitle = new JLabel("Recent Search History");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
        tableTitle.setForeground(new Color(34, 34, 34));

        historyTable = new JTable();
        historyTable.setFillsViewportHeight(true);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        refreshHistory();
    }

    private void refreshHistory() {

        List<Word> history = facade.getRecentSearchHistory(10);
        String[] columns = {"Arabic Word", "Persian Meaning", "Urdu Meaning"};
        String[][] data = new String[history.size()][3];

        for (int i = 0; i < history.size(); i++) {
            Word word = history.get(i);
            data[i][0] = word.getArabicWord();
            data[i][1] = word.getPersianMeaning();
            data[i][2] = word.getUrduMeaning();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable.setModel(model);
    }

    private void handleBackAction() {
        this.dispose();
        previousWindow.setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
