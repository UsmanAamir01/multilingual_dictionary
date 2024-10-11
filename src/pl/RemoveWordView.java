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

public class RemoveWordView extends JFrame {
	private JTextField wordTextField;
	private JButton removeButton;
	private WordBO wordBO;

	public RemoveWordView(WordBO wordBO) {
		this.wordBO = wordBO;

		setTitle("Remove Word");
		setSize(420, 240);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
		removeButton.setBackground(Color.red);
		removeButton.setForeground(Color.WHITE);
		removeButton.setBorderPainted(false);
		removeButton.setFocusPainted(false);
		removeButton.setPreferredSize(new Dimension(140, 35));
		removeButton.setBorder(BorderFactory.createLineBorder(new Color(205, 32, 32)));

		buttonPanel.add(removeButton);

		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wordText = wordTextField.getText().trim();

				if (!wordText.isEmpty()) {
					boolean success = wordBO.removeWord(wordText);

					if (success) {
						JOptionPane.showMessageDialog(RemoveWordView.this, "Word removed successfully!");
						wordTextField.setText("");
					} else {
						JOptionPane.showMessageDialog(RemoveWordView.this,
								"Failed to remove the word. It may not exist.");
					}
				} else {
					JOptionPane.showMessageDialog(RemoveWordView.this, "Please enter a word to remove.");
				}
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		add(mainPanel);
		setVisible(true);
	}

	public void display() {
		setVisible(true);
	}
}
