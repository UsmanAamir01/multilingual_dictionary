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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import bl.IBLFacade;

public class RemoveWordView extends JFrame {
    private JTextField wordTextField;
    private JButton removeButton;
    private JButton backButton;
    private JFrame previousWindow;
    private final IBLFacade facade;

    public RemoveWordView(JFrame previousWindow, IBLFacade facade) {
        this.previousWindow = previousWindow;
        this.facade = facade;
        setTitle("Remove Word");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
        wordLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(wordLabel, gbc);

        wordTextField = new JTextField(20);
        wordTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(wordTextField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        removeButton = new JButton("Remove Word");
        removeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.setPreferredSize(new Dimension(140, 35));
        removeButton.setBorder(BorderFactory.createLineBorder(new Color(205, 32, 32)));

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(0, 123, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(140, 35));

        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        removeButton.addActionListener(actionEvent -> handleRemoveWord());
        backButton.addActionListener(actionEvent -> handleBackAction());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private void handleRemoveWord() {
        String wordText = wordTextField.getText().trim();
        if (wordText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a word to remove.");
            return;
        }
        try {
            boolean success = facade.removeWord(wordText);
            if (success) {
                JOptionPane.showMessageDialog(this, "Word removed successfully!");
                wordTextField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "The word does not exist in the dictionary.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.");
        }
    }

    private void handleBackAction() {
        previousWindow.setVisible(true);
        dispose();
    }

    public void display() {
        setVisible(true);
    }
}
