package pl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import bl.WordBO;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AllWordView extends JFrame {
    JTable table;
    JLabel headingLabel, wordLabel, meaningLabel;
    private WordBO word;
    private JFrame previousWindow;
    private JButton backButton;

    public AllWordView(JFrame previousWindow) {
        this.previousWindow = previousWindow;
        word = new WordBO();
        String[][] wordData = word.getWordsWithMeanings();
        String[] columnNames = { "Word", "Meaning" };
        DefaultTableModel model = new DefaultTableModel(wordData, columnNames);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(new Color(224, 247, 218));
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(165, 214, 167));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(187, 222, 251));
        tableHeader.setForeground(Color.BLACK);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16));

        headingLabel = new JLabel("Selected Word and Meaning", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headingLabel.setForeground(Color.BLACK);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        wordLabel = new JLabel("Word: ");
        meaningLabel = new JLabel("Meaning: ");
        wordLabel.setOpaque(true);
        wordLabel.setBackground(new Color(135, 206, 250));
        wordLabel.setForeground(Color.BLACK);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        wordLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        meaningLabel.setOpaque(true);
        meaningLabel.setBackground(new Color(135, 206, 250));
        meaningLabel.setForeground(Color.BLACK);
        meaningLabel.setFont(new Font("Arial", Font.BOLD, 16));
        meaningLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String word = (String) table.getValueAt(selectedRow, 0);
                        String meaning = (String) table.getValueAt(selectedRow, 1);
                        wordLabel.setText("Word: " + word);
                        meaningLabel.setText("Meaning: " + meaning);
                    }
                }
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel spacerLabel = new JLabel();
        spacerLabel.setPreferredSize(new Dimension(0, 20));
        footerPanel.add(spacerLabel);

        footerPanel.add(headingLabel);
        footerPanel.add(wordLabel);
        footerPanel.add(meaningLabel);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(255, 99, 71));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);

        setTitle("Word and Meaning Viewer");
        setBackground(new Color(245, 245, 245));
        setSize(500, 500);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);

        backButton.addActionListener(e -> {
            previousWindow.setVisible(true);
            dispose();
        });
    }
}
