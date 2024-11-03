package pl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import bl.UserBO;
import bl.WordBO;

public class WordUI extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private WordBO wordBo;
	private JPanel sidebarPanel;
	private JPanel mainContentPanel;
	private JTextField searchField;
	private JComboBox<String> languageComboBox;

	public WordUI(WordBO wordBo, UserBO userBo) {
		this.wordBo = wordBo;
		setTitle("Multilingual Dictionary");
		setSize(800, 600);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(245, 245, 245));
		JPanel loginPanel = createLoginPanel(userBo);
		add(loginPanel);
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				if (userBo.validateUser(username, password)) {
					JOptionPane.showMessageDialog(WordUI.this, "Login successful!");
					showDashboard();
				} else {
					JOptionPane.showMessageDialog(WordUI.this, "Invalid credentials, please try again.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private JPanel createLoginPanel(UserBO userBo) {
		JPanel loginPanel = new JPanel(new GridBagLayout());
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Multilingual Dictionary", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(0, 51, 153));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		loginPanel.add(titleLabel, gbc);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		usernameLabel.setForeground(Color.DARK_GRAY);
		usernameField = new JTextField(15);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		loginPanel.add(usernameLabel, gbc);
		gbc.gridx = 1;
		loginPanel.add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		passwordLabel.setForeground(Color.DARK_GRAY);
		passwordField = new JPasswordField(15);
		gbc.gridx = 0;
		gbc.gridy = 2;
		loginPanel.add(passwordLabel, gbc);
		gbc.gridx = 1;
		loginPanel.add(passwordField, gbc);

		loginButton = createButton("Login", new Color(0, 51, 153), Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		loginPanel.add(loginButton, gbc);
		return loginPanel;
	}

	private JButton createButton(String text, Color backgroundColor, Color textColor) {
		JButton button = new JButton(text);
		button.setBackground(backgroundColor);
		button.setForeground(textColor);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2));
		button.setPreferredSize(new Dimension(150, 40));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setOpaque(true);
		return button;
	}

	private void showDashboard() {
		getContentPane().removeAll();
		setTitle("Dashboard");

		sidebarPanel = new JPanel(new GridLayout(8, 1, 10, 10));
		sidebarPanel.setPreferredSize(new Dimension(200, 600));
		sidebarPanel.setBackground(new Color(240, 240, 240));

		JButton addWordButton = createSidebarButton("Add Word");
		JButton viewAllButton = createSidebarButton("View All Words");
		JButton importFileButton = createSidebarButton("Import File");
		JButton viewOnceButton = createSidebarButton("View Word");
		JButton arabicTaggerButton = createSidebarButton("Arabic Tagger/Stemmer");
		JButton closeButton = createSidebarButton("Close");

		SidebarButtonActionListener actionListener = new SidebarButtonActionListener();
		addWordButton.addActionListener(actionListener);
		viewAllButton.addActionListener(actionListener);
		importFileButton.addActionListener(actionListener);
		viewOnceButton.addActionListener(actionListener);
		arabicTaggerButton.addActionListener(actionListener);
		closeButton.addActionListener(actionListener);

		sidebarPanel.add(addWordButton);
		sidebarPanel.add(viewAllButton);
		sidebarPanel.add(importFileButton);
		sidebarPanel.add(viewOnceButton);
		sidebarPanel.add(arabicTaggerButton);
		sidebarPanel.add(closeButton);

		mainContentPanel = new JPanel();
		mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
		mainContentPanel.setBackground(Color.WHITE);
		mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel welcomeLabel = new JLabel("Welcome to the Dashboard!", SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
		welcomeLabel.setForeground(new Color(0, 51, 153));
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainContentPanel.add(welcomeLabel);
		mainContentPanel.add(Box.createVerticalStrut(20));

		JPanel searchLanguagePanel = new JPanel();
		searchLanguagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		searchLanguagePanel.setBackground(Color.WHITE);

		String[] languages = { "Arabic", "Persian", "Urdu" };
		languageComboBox = new JComboBox<>(languages);
		languageComboBox.setPreferredSize(new Dimension(150, 30));

		searchField = new JTextField(15);
		JButton logoButton = new JButton();
		ImageIcon logoIcon = new ImageIcon("src/images/search-icon.png");
		logoIcon = new ImageIcon(logoIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		logoButton.setIcon(logoIcon);
		logoButton.setBorder(BorderFactory.createEmptyBorder());
		logoButton.setContentAreaFilled(false);
		logoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerSearch();
			}
		});

		searchLanguagePanel.add(languageComboBox);
		searchLanguagePanel.add(searchField);
		searchLanguagePanel.add(logoButton);
		mainContentPanel.add(searchLanguagePanel);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, mainContentPanel);
		splitPane.setDividerSize(0);
		splitPane.setDividerLocation(200);
		add(splitPane);

		revalidate();
		repaint();
	}

	private class SidebarButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = ((JButton) e.getSource()).getText();
			switch (command) {
			case "Add Word":
				navigateTo(new AddWordView(wordBo, WordUI.this));
				break;
			case "View All Words":
				navigateTo(new AllWordView(WordUI.this));
				break;
			case "Import File":
				navigateTo(new DictionaryUI(wordBo, WordUI.this));
				break;
			case "View Word":
				navigateTo(new ViewOnceWordView(wordBo, WordUI.this));
				break;
			case "Arabic Tagger/Stemmer":
				navigateTo(new ArabicTaggerUI(WordUI.this));
				break;
			case "Close":
				System.exit(0);
				break;
			}
		}
	}

	private void triggerSearch() {
		String searchText = searchField.getText();
		String selectedLanguage = (String) languageComboBox.getSelectedItem();
		if (!searchText.isEmpty()) {
			String result = wordBo.getMeanings(searchText, selectedLanguage);
			if (!result.equals("Word not found.") && !result.equals("Error retrieving meanings.")) {
				JOptionPane.showMessageDialog(this,
						"Searching for: " + searchText + " in " + selectedLanguage + "\n" + result);
			} else {
				JOptionPane.showMessageDialog(this, result);
			}
			searchField.setText("");
		} else {
			JOptionPane.showMessageDialog(this, "Please enter a word to search.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JButton createSidebarButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(new Color(0, 102, 204));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setOpaque(true);
		button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return button;
	}

	private void navigateTo(JFrame frame) {
		frame.setVisible(true);
		this.dispose();
	}

	public static void main(String[] args) {
		WordBO wordBo = new WordBO();
		UserBO userBo = new UserBO();
		new WordUI(wordBo, userBo).setVisible(true);
	}
}