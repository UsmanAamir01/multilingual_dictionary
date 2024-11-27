package pl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import bl.IBLFacade;
import dto.Word;

public class ArabicWordProcessingView extends JFrame {
	private JTextArea outputArea;
	private JButton fetchAndProcessButton, backButton, searchButton;
	private JTextField searchField;
	private JComboBox<String> languageComboBox;
	private JFrame previousWindow;
	private IBLFacade facade;
	private Word word;

	public ArabicWordProcessingView(IBLFacade facade, JFrame previousWindow) {
		this.facade = facade;
		this.previousWindow = previousWindow;

		setTitle("Word Normalization");
		setSize(900, 680);
		setLocationRelativeTo(null);
		setBackground(new Color(240, 240, 240));

		JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
		mainPanel.setBackground(new Color(255, 255, 255));

		JPanel searchPanel = new JPanel(new GridBagLayout());
		searchPanel.setBackground(new Color(245, 245, 245));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		backButton = createStyledButton("Back");
		backButton.setPreferredSize(new Dimension(130, 45));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		searchPanel.add(backButton, gbc);

		JLabel searchLabel = new JLabel("Enter Word:");
		searchLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 1;
		searchPanel.add(searchLabel, gbc);

		searchField = new JTextField(20);
		searchField.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		searchPanel.add(searchField, gbc);

		languageComboBox = new JComboBox<>(new String[] { "Arabic", "Urdu", "Persian" });
		languageComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		searchPanel.add(languageComboBox, gbc);

		searchButton = createStyledButton("Search");
		gbc.gridx = 5;
		searchPanel.add(searchButton, gbc);

		mainPanel.add(searchPanel, BorderLayout.NORTH);

		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputArea.setFont(new Font("Arial", Font.PLAIN, 16));
		outputArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Search Result"));
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(255, 255, 255));

		fetchAndProcessButton = createStyledButton("Normalize");
		fetchAndProcessButton.setPreferredSize(new Dimension(150, 45));
		buttonPanel.add(fetchAndProcessButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
		setVisible(true);

		searchButton.addActionListener(e -> handleSearchAction());
		fetchAndProcessButton.addActionListener(e -> processWord());
		backButton.addActionListener(e -> handleBackAction());
	}

	private void handleSearchAction() {
		String wordText = searchField.getText().trim();
		String selectedLanguage = (String) languageComboBox.getSelectedItem();

		if (wordText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a word to search.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String result = facade.getMeanings(wordText, selectedLanguage);

		if (!"Word not found.".equals(result)) {
			outputArea.setText(result);
			Word word = new Word("Arabic".equals(selectedLanguage) ? wordText : null,
					"Persian".equals(selectedLanguage) ? result : null,
					"Urdu".equals(selectedLanguage) ? result : null);

			fetchAndProcessButton.setEnabled(true);
		} else {
			int choice = JOptionPane.showConfirmDialog(this,
					"Word not found in the database. Would you like to scrape data for this word?", "Scrape Data",
					JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				showScraperUI(selectedLanguage);
			}
		}

	}

	private void handleBackAction() {
		previousWindow.setVisible(true);
		dispose();
	}

	private void showScraperUI(String language) {
		DictionaryScraper scraper = new DictionaryScraper(facade, previousWindow);
		scraper.setTitle("Scraper for " + language);
		scraper.setVisible(true);
	}

	private void processWord() {
		String wordText = searchField.getText().trim();
		if (wordText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a word to process.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		outputArea.append("Processing: " + wordText + "\n");

		try {
			Word wordToProcess = new Word(wordText, null, null);
			String result = facade.processWord(wordText);

			if (result != null) {
				String[] parts = result.split(",");
				String stem = parts[0];
				String root = parts[1];
				String pos = parts[2];

				facade.saveResults(wordText, stem, root, pos);

				outputArea.append("Result:\n");
				outputArea.append("  Stem: " + stem + "\n");
				outputArea.append("  Root (Lemma): " + root + "\n");
				outputArea.append("  POS (Part Of Speech): " + pos + "\n");
			}
		} catch (Exception ex) {
			outputArea.append("Error occurred while processing the word.\n");
			ex.printStackTrace();
		}
	}

	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 15));
		button.setBackground(new Color(28, 144, 255));
		setForeground(new Color(0, 51, 153));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
		button.setPreferredSize(new Dimension(150, 45));
		return button;
	}
}
