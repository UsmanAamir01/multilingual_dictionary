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
import javax.swing.border.EmptyBorder;

import bl.IBLFacade;
import dto.Word;

public class UpdateWordView extends JFrame {
	private JTextField wordTextField;
	private JTextField newMeaningTextField;
	private JButton updateButton;
	private JButton backButton;
	private JFrame previousWindow;
	private final IBLFacade facade;

	public UpdateWordView(JFrame previousWindow, IBLFacade facade) {
		this.previousWindow = previousWindow;
		this.facade = facade;
		setTitle("Update Word");
		setSize(500, 400);
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
		formPanel.add(wordTextField, gbc);

		JLabel newMeaningLabel = new JLabel("New Meaning:");
		newMeaningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(newMeaningLabel, gbc);

		newMeaningTextField = new JTextField(20);
		newMeaningTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		gbc.gridx = 1;
		formPanel.add(newMeaningTextField, gbc);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);

		updateButton = new JButton("Update Word");
		updateButton.setPreferredSize(new Dimension(140, 35));
		updateButton.setBackground(new Color(50, 205, 50));
		updateButton.setForeground(Color.WHITE);
		updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

		backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(120, 35));
		backButton.setBackground(new Color(0, 123, 255));
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

		buttonPanel.add(updateButton);
		buttonPanel.add(backButton);

		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		updateButton.addActionListener(actionEvent -> showLanguageSelectionDialog());
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

	private void showLanguageSelectionDialog() {
		String[] options = { "Persian", "Urdu" };
		int choice = JOptionPane.showOptionDialog(this, "Select the language to update the meaning:",
				"Language Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);

		if (choice == JOptionPane.CLOSED_OPTION) {
			return;
		}

		String selectedLanguage = options[choice];
		handleUpdateWord(selectedLanguage);
	}

	private void handleUpdateWord(String language) {
		String arabicWord = wordTextField.getText().trim();
		String newMeaning = newMeaningTextField.getText().trim();

		if (arabicWord.isEmpty() || newMeaning.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Word word = new Word(arabicWord, language.equals("Urdu") ? newMeaning : null,
				language.equals("Persian") ? newMeaning : null);
		boolean success = facade.updateWord(word); // Delegation to facade instead of wordBO
		if (success) {
			JOptionPane.showMessageDialog(this, "Word updated successfully!");
			clearFields();
		} else {
			JOptionPane.showMessageDialog(this, "Failed to update the word.");
		}
	}

	private void clearFields() {
		wordTextField.setText("");
		newMeaningTextField.setText("");
	}

	private void handleBackAction() {
		this.dispose();
		previousWindow.setVisible(true);
	}
}
