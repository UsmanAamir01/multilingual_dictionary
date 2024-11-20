package pl;

import bl.IBLFacade;
import dto.Word;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryView extends JFrame {
    private final IBLFacade facade;
    private JTable historyTable;
    private JButton backButton;
    private JFrame previousWindow;

    public HistoryView(JFrame previousWindow, IBLFacade facade) {
        this.previousWindow = previousWindow;
        this.facade = facade;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Search History");
        setSize(800, 600); // matching WordUI's size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Create the components of the page
        historyTable = new JTable();
        JButton backButton = createButton("Back", new Color(0, 51, 153), Color.WHITE);

        backButton.addActionListener(actionEvent -> handleBackAction());

        // Layout for the components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // Refresh the search history table
        refreshHistory();
    }

    private void refreshHistory() {
        List<Word> history = facade.getRecentSearchHistory(10);

        List<String> arabicWords = new ArrayList<>();
        List<String> persianMeanings = new ArrayList<>();
        List<String> urduMeanings = new ArrayList<>();

        for (Word word : history) {
            arabicWords.add(word.getArabicWord());
            persianMeanings.add(word.getPersianMeaning());
            urduMeanings.add(word.getUrduMeaning());
        }

        String[] columns = {"Arabic Word", "Persian Meaning", "Urdu Meaning"};
        String[][] data = new String[history.size()][3];

        for (int i = 0; i < history.size(); i++) {
            data[i][0] = arabicWords.get(i);
            data[i][1] = persianMeanings.get(i);
            data[i][2] = urduMeanings.get(i);
        }

        historyTable.setModel(new DefaultTableModel(data, columns));
    }

    private void handleBackAction() {
        this.dispose();
        previousWindow.setVisible(true);
    }

    private JButton createButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    
}
