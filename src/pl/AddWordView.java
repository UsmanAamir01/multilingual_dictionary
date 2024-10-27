package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bl.WordBO;
import dto.Word;

public class AddWordView extends JFrame {
    private JTextField wordTextField, meaningTextField;
    private JButton addButton, backButton;
    private JFrame previousWindow;

    public AddWordView(WordBO wordBO, JFrame previousWindow) {
        this.previousWindow = previousWindow;
        setTitle("Add Word");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setBackground(Color.white);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.white);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel wordLabel = new JLabel("Word:");
        wordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(wordLabel, gbc);

        wordTextField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(wordTextField, gbc);

        JLabel meaningLabel = new JLabel("Meaning:");
        meaningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(meaningLabel, gbc);

        meaningTextField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(meaningTextField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        addButton = new JButton("Add Word");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(255, 156, 0));
        addButton.setPreferredSize(new Dimension(140, 35));
        addButton.setForeground(Color.WHITE);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(0, 123, 255));
        backButton.setPreferredSize(new Dimension(140, 35));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);

        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        addButton.addActionListener(e -> {
            String wordText = wordTextField.getText().trim();
            String meaning = meaningTextField.getText().trim();

            if (!wordText.isEmpty() && !meaning.isEmpty()) {
                Word word = new Word(wordText, meaning);
                boolean success = wordBO.addWord(word);

                if (success) {
                    JOptionPane.showMessageDialog(AddWordView.this, "Word added successfully!");
                    wordTextField.setText("");
                    meaningTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(AddWordView.this, "Failed to add the word.");
                }
            } else {
                JOptionPane.showMessageDialog(AddWordView.this, "Please fill in all fields.");
            }
        });

        backButton.addActionListener(e -> {
            previousWindow.setVisible(true);
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }
}
