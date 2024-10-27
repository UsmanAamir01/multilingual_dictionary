package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bl.WordBO;
import dto.Word;

public class UpdateWordView extends JFrame {
    private JTextField wordTextField, newMeaningTextField;
    private JButton updateButton;
    private JButton backButton;
    private JFrame previousWindow;

    public UpdateWordView(JFrame previousWindow, WordBO wordBO) {
        this.previousWindow = previousWindow;
        setTitle("Update Word");
        setSize(500, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel wordLabel = new JLabel("Word:");
        wordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(wordLabel, gbc);

        wordTextField = new JTextField(20);
        wordTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(wordTextField, gbc);

        JLabel newMeaningLabel = new JLabel("New Meaning:");
        newMeaningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(newMeaningLabel, gbc);

        newMeaningTextField = new JTextField(20);
        newMeaningTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(newMeaningTextField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        updateButton = new JButton("Update Word");
        updateButton.setPreferredSize(new Dimension(140, 35));
        updateButton.setBackground(new Color(50,205,50));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonPanel.add(updateButton);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 35));
        backButton.setBackground(new Color(0, 123, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateButton.addActionListener(e -> {
            String wordText = wordTextField.getText().trim();
            String newMeaning = newMeaningTextField.getText().trim();

            if (!wordText.isEmpty() && !newMeaning.isEmpty()) {
                Word word = new Word(wordText, newMeaning);
                boolean success = wordBO.updateWord(word);

                if (success) {
                    JOptionPane.showMessageDialog(UpdateWordView.this, "Word updated successfully!");
                    wordTextField.setText("");
                    newMeaningTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(UpdateWordView.this, "Failed to update the word. It may not exist.");
                }
            } else {
                JOptionPane.showMessageDialog(UpdateWordView.this, "Please fill in all fields.");
            }
        });

        backButton.addActionListener(e -> {
            previousWindow.setVisible(true);
            dispose();
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public void display() {
        setVisible(true);
    }
}
