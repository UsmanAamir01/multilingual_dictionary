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

public class AddWordView extends JFrame {
    private JTextField wordTextField;
    private JTextField meaningTextField;
    private JButton addButton;
    private WordBO wordBO;

    public AddWordView(WordBO wordBO) {
        this.wordBO = wordBO;

        setTitle("Add Word");
        setSize(400, 250);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));

        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10)); 

        JLabel wordLabel = new JLabel("Word:");
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(wordLabel);

        wordTextField = new JTextField();
        wordTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(wordTextField);

        JLabel meaningLabel = new JLabel("Meaning:");
        meaningLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(meaningLabel);

        meaningTextField = new JTextField();
        meaningTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(meaningTextField);

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 30));
        addButton.setBackground(new Color(66, 135, 245));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createLineBorder(new Color(30, 144, 255)));

        buttonPanel.add(addButton);

        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        add(mainPanel);

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordText = wordTextField.getText();
                String meaning = meaningTextField.getText();

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

    
    public void display() {
        setVisible(true);
    }
}
