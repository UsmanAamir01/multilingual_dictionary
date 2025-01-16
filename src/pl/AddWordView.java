package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import bl.IBLFacade;
import dto.Word;

public class AddWordView extends JFrame {
	private JTextField arabicWordTextField, urduMeaningTextField, persianMeaningTextField;
	private JButton addButton, backButton;
	private JFrame previousWindow;
	private final IBLFacade facade;

	public AddWordView(IBLFacade facade, JFrame previousWindow) {
		this.facade = facade;
		this.previousWindow = previousWindow;
		setTitle("Add Word");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setBackground(Color.white);
		setResizable(false);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(15, 15));
		mainPanel.setBackground(new Color(245, 245, 245));

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.white);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel arabicWordLabel = new JLabel("Arabic Word:");
		arabicWordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		arabicWordLabel.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(arabicWordLabel, gbc);

		arabicWordTextField = new JTextField(20);
		arabicWordTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		arabicWordTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		arabicWordTextField.setBackground(Color.white);
		arabicWordTextField.setPreferredSize(new Dimension(250, 30));
		gbc.gridx = 1;
		formPanel.add(arabicWordTextField, gbc);

		JLabel urduMeaningLabel = new JLabel("Urdu Meaning:");
		urduMeaningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		urduMeaningLabel.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(urduMeaningLabel, gbc);

		urduMeaningTextField = new JTextField(20);
		urduMeaningTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		urduMeaningTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		urduMeaningTextField.setBackground(Color.white);
		urduMeaningTextField.setPreferredSize(new Dimension(250, 30));
		gbc.gridx = 1;
		formPanel.add(urduMeaningTextField, gbc);

		JLabel persianMeaningLabel = new JLabel("Persian Meaning:");
		persianMeaningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		persianMeaningLabel.setForeground(Color.DARK_GRAY);
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(persianMeaningLabel, gbc);

		persianMeaningTextField = new JTextField(20);
		persianMeaningTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		persianMeaningTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		persianMeaningTextField.setBackground(Color.white);
		persianMeaningTextField.setPreferredSize(new Dimension(250, 30));
		gbc.gridx = 1;
		formPanel.add(persianMeaningTextField, gbc);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(245, 245, 245));

		addButton = new JButton("Add Word");
		addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		addButton.setBackground(new Color(0, 123, 255));
		addButton.setPreferredSize(new Dimension(140, 40));
		addButton.setForeground(Color.WHITE);
		addButton.setFocusPainted(false);
		addButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addButton.setToolTipText("Click to add the word");
		addButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
		backButton = new JButton("Back");
		backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		backButton.setBackground(new Color(255, 69, 0));
		backButton.setPreferredSize(new Dimension(140, 40));
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.setToolTipText("Click to go back");
		backButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

		buttonPanel.add(addButton);
		buttonPanel.add(backButton);

		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String arabicWordText = arabicWordTextField.getText().trim();
				String urduMeaning = urduMeaningTextField.getText().trim();
				String persianMeaning = persianMeaningTextField.getText().trim();

				if (!arabicWordText.isEmpty() && !urduMeaning.isEmpty() && !persianMeaning.isEmpty()) {
					Word word = new Word(arabicWordText, urduMeaning, persianMeaning);
					boolean success = facade.addWord(word);

					if (success) {
						JOptionPane.showMessageDialog(AddWordView.this, "Word added successfully!");
						arabicWordTextField.setText("");
						urduMeaningTextField.setText("");
						persianMeaningTextField.setText("");
					} else {
						JOptionPane.showMessageDialog(AddWordView.this, "Failed to add the word.");
					}
				} else {
					JOptionPane.showMessageDialog(AddWordView.this, "Please fill in all fields.");
				}
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				previousWindow.setVisible(true);
				dispose();
			}
		});

		add(mainPanel);
		setVisible(true);
	}

}
