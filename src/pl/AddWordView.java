package pl;

import java.awt.*;
import javax.swing.*;
import bl.WordBO;
import dto.Word;

public class AddWordView extends JFrame {
    private JTextField wordTextField, meaningTextField;
    private JButton addButton, backButton;
    private final WordBO wordBO;

    public AddWordView(WordBO wordBO, JFrame previousWindow) {
        this.wordBO = wordBO;
        setTitle("Add Word");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.white);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.white);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel wordLabel = new JLabel("Word:");
        wordTextField = new JTextField(20);
        JLabel meaningLabel = new JLabel("Meaning:");
        meaningTextField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(wordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(wordTextField, gbc);
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(meaningLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(meaningTextField, gbc);

        addButton = new JButton("Add Word");
        addButton.addActionListener(e -> addWord());
        backButton = new JButton("Back");
        backButton.addActionListener(e -> previousWindow.setVisible(true));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void addWord() {
        String wordText = wordTextField.getText().trim();
        String meaningText = meaningTextField.getText().trim();
        if (!wordText.isEmpty() && !meaningText.isEmpty()) {
            Word word = new Word(wordText, meaningText);
            if (wordBO.addWord(word)) {
                JOptionPane.showMessageDialog(this, "Word added successfully!");
                wordTextField.setText("");
                meaningTextField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add the word.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }
}