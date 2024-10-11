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

public class RemoveWordView extends JFrame {
    private JTextField wordTextField;
    private JButton removeButton;
    private WordBO wordBO;

    public RemoveWordView(WordBO wordBO) {
        this.wordBO = wordBO;

        
        setTitle("Remove Word");
        setSize(400, 200);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));

        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(1, 2, 10, 10)); 

        JLabel wordLabel = new JLabel("Word:");
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(wordLabel);

        wordTextField = new JTextField();
        wordTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(wordTextField);

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 30));
        removeButton.setBackground(new Color(245, 66, 66));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setBorder(BorderFactory.createLineBorder(new Color(205, 32, 32)));

        buttonPanel.add(removeButton);

        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        add(mainPanel);

        
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordText = wordTextField.getText();

                if (!wordText.isEmpty()) {
                    boolean success = wordBO.removeWord(wordText);

                    if (success) {
                        JOptionPane.showMessageDialog(RemoveWordView.this, "Word removed successfully!");
                        wordTextField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(RemoveWordView.this, "Failed to remove the word. It may not exist.");
                    }
                } else {
                    JOptionPane.showMessageDialog(RemoveWordView.this, "Please enter a word to remove.");
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
