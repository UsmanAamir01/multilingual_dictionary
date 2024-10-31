package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import bl.WordBO;

public class AllWordView extends JFrame {
    private JTable table;
    private JLabel headingLabel, wordLabel, urduLabel, persianLabel;
    private WordBO word;
    private JFrame previousWindow;
    private JButton backButton;

    public AllWordView(JFrame previousWindow) {
        this.previousWindow = previousWindow;
        word = new WordBO();

        String[][] wordData = word.getWordsWithMeanings();
        String[] columnNames = {"Arabic Word", "Urdu Meaning", "Persian Meaning"};
        DefaultTableModel model = new DefaultTableModel(wordData, columnNames);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setTableStyles();

        headingLabel = createHeadingLabel("Word and Meanings");

        wordLabel = createMeaningLabel("Arabic Word: ");
        urduLabel = createMeaningLabel("Urdu Meaning: ");
        persianLabel = createMeaningLabel("Persian Meaning: ");

        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setTitle("Word and Meaning Viewer");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                dispose();
            }
        });

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        updateLabels(selectedRow);
                    }
                }
            }
        });
    }

    private void setTableStyles() {
        table.setBackground(new Color(240, 248, 255));
        table.setForeground(Color.DARK_GRAY);
        table.setSelectionBackground(new Color(180, 225, 205));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(100, 149, 237));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16));
        tableHeader.setBorder(BorderFactory.createEtchedBorder());
    }

    private void updateLabels(int selectedRow) {
        String arabicWord = (String) table.getValueAt(selectedRow, 0);
        String urduMeaning = (String) table.getValueAt(selectedRow, 1);
        String persianMeaning = (String) table.getValueAt(selectedRow, 2);
        wordLabel.setText("Arabic Word: " + arabicWord);
        urduLabel.setText("Urdu Meaning: " + urduMeaning);
        persianLabel.setText("Persian Meaning: " + persianMeaning);
    }

    private JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(25, 25, 112));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return label;
    }

    private JLabel createMeaningLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(new Color(173, 216, 230));
        label.setForeground(Color.DARK_GRAY);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return label;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(248, 248, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        footerPanel.add(headingLabel);
        footerPanel.add(wordLabel);
        footerPanel.add(urduLabel);
        footerPanel.add(persianLabel);

        backButton = createButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                previousWindow.setVisible(true);
                dispose();
            }
        });

        footerPanel.add(Box.createVerticalStrut(10));
        footerPanel.add(backButton);
        return footerPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
