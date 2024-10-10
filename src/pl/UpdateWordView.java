package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
    private JTextField wordTextField;
    private JTextField newMeaningTextField;
    private JButton updateButton;
    private WordBO wordBO;

    public UpdateWordView(WordBO wordBO) {
        this.wordBO = wordBO;

        // Set up the JFrame
        setTitle("Update Word");
        setSize(400, 250);
        setLocationRelativeTo(null); // Center the frame
        setResizable(false);

        // Create a main panel with padding and background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Form panel for inputs
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns with padding

        JLabel wordLabel = new JLabel("Word:");
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(wordLabel);

        wordTextField = new JTextField();
        wordTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(wordTextField);

        JLabel newMeaningLabel = new JLabel("New Meaning:");
        newMeaningLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(newMeaningLabel);

        newMeaningTextField = new JTextField();
        newMeaningTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(newMeaningTextField);

        // Button panel for the update button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(100, 30));
        updateButton.setBackground(new Color(66, 135, 245));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBorder(BorderFactory.createLineBorder(new Color(30, 144, 255)));

        buttonPanel.add(updateButton);

        // Add the form and button panels to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the JFrame
        add(mainPanel);

        // Add button action listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordText = wordTextField.getText();
                String newMeaning = newMeaningTextField.getText();

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
            }
        });
        this.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e)
        	{
        		dispose();
        	}
        });
        setVisible(true);
    }

    // Method to display the JFrame
    public void display() {
        setVisible(true);
    }
}
