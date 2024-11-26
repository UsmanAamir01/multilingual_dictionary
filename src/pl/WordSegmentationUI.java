package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import bl.BLFacade;
import bl.IBLFacade;
import bl.UserBO;
import bl.WordBO;
import dto.Word;

public class WordSegmentationUI extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton segmentButton;
    private JButton backButton;
    private final IBLFacade facade;
    private JTextArea meaningArea;
    private JFrame previousWindow;

    public WordSegmentationUI(IBLFacade facade, JFrame previousWindow) {
        this.facade = facade;
        this.previousWindow = previousWindow;

        setTitle("Word Segmentation");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        inputField = new JTextField();
        outputArea = new JTextArea();
        segmentButton = new JButton("Segment Word");
        backButton = new JButton("Back");
        meaningArea = new JTextArea();

        outputArea.setEditable(false);
        meaningArea.setEditable(false);
        meaningArea.setLineWrap(true);
        meaningArea.setWrapStyleWord(true);

        inputField.setPreferredSize(new Dimension(300, 30));
        outputArea.setPreferredSize(new Dimension(600, 200));
        meaningArea.setPreferredSize(new Dimension(600, 150));

        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        meaningArea.setFont(new Font("Arial", Font.PLAIN, 14));
        segmentButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        segmentButton.setBackground(new Color(70, 130, 180));
        segmentButton.setForeground(Color.WHITE);
        segmentButton.setFocusPainted(false);

        backButton.setBackground(new Color(220, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Word"));
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(segmentButton, BorderLayout.EAST);
        add(inputPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Segmented Words"));
        outputPanel.setBackground(new Color(240, 240, 240));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(outputPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JPanel meaningPanel = new JPanel(new BorderLayout());
        meaningPanel.setBorder(BorderFactory.createTitledBorder("Word Meaning"));
        meaningPanel.setBackground(new Color(240, 240, 240));
        meaningPanel.add(new JScrollPane(meaningArea), BorderLayout.CENTER);
        add(meaningPanel, gbc);

        // Adding back button
        gbc.gridx = 0;
        gbc.gridy = 3;
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(new Color(240, 240, 240));
        backPanel.add(backButton, BorderLayout.CENTER);
        add(backPanel, gbc);

        segmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = inputField.getText().trim();
                if (!word.isEmpty()) {
                    try {
                        List<String> segmentedWords = getSegmentedWordsWithDiacritics(word);
                        if (segmentedWords != null) {
                            outputArea.setText(String.join("\n", segmentedWords));
                        } else {
                            outputArea.setText("Segmentation failed.");
                        }
                    } catch (Exception ex) {
                        outputArea.setText("Error: " + ex.getMessage());
                    }
                } else {
                    outputArea.setText("Please enter a valid word.");
                }
            }
        });

        outputArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int offset = outputArea.viewToModel(e.getPoint());
                    String clickedWord = getWordAtOffset(offset);
                    if (clickedWord != null) {
                        String meaning = getMeaningOfWord(clickedWord);
                        meaningArea.setText(meaning);
                    }
                } catch (Exception ex) {
                    meaningArea.setText("Error fetching meaning.");
                }
            }
        });

        // Back button action to go back to the previous window
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousWindow.setVisible(true);
                dispose();
            }
        });
    }

    public List<String> getSegmentedWordsWithDiacritics(String word) {
        try {
            return facade.getSegmentedWordsWithDiacritics(word);
        } catch (Exception e) {
            System.err.println("Error during word segmentation: " + e.getMessage());
            return null;
        }
    }

    private String getWordAtOffset(int offset) {
        String text = outputArea.getText();
        String[] words = text.split("\\s+");
        int position = 0;
        for (String word : words) {
            position += word.length() + 1;
            if (offset < position) {
                return word;
            }
        }
        return null;
    }

    private String getMeaningOfWord(String word) {
        return facade.searchWord(word);
    }

    public static void main(String[] args) {
        try {
        	WordBO wordBO = new WordBO();
        	UserBO userBO = new UserBO();
            IBLFacade facade = new BLFacade(wordBO, userBO);
            JFrame previousWindow = new JFrame(); 
            WordSegmentationUI ui = new WordSegmentationUI(facade, previousWindow);
            ui.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
