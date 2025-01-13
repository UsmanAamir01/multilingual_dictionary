package pl;

import java.awt.*;
import javax.swing.*;

import bl.BLFacade;
import bl.IBLFacade;
import bl.UserBO;
import bl.WordBO;

public class WelcomeUI extends JFrame {

	public WelcomeUI(IBLFacade facade, UserBO userBo) {
		setTitle("Welcome");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setBackground(new Color(240, 248, 255));
		setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Home", createHomePanel(facade, userBo));
		tabbedPane.addTab("About", createAboutPanel());
		tabbedPane.addTab("Contact", createContactPanel());

		add(tabbedPane, BorderLayout.CENTER);
		setVisible(true);
	}

	private JPanel createHomePanel(IBLFacade facade, UserBO userBo) {
	    JPanel mainPanel = new JPanel(new GridBagLayout());
	    mainPanel.setBackground(new Color(240, 248, 255));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(10, 10, 10, 10);

	    JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon("images/dictionary_logo.jpeg").getImage()
	            .getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    mainPanel.add(imageLabel, gbc);

	    JLabel titleLabel = new JLabel("Welcome to Multilingual Dictionary", SwingConstants.CENTER);
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
	    titleLabel.setForeground(new Color(25, 25, 112));
	    gbc.gridy = 1;
	    gbc.gridwidth = 2;
	    mainPanel.add(titleLabel, gbc);

	    JLabel taglineLabel = new JLabel("Your one-stop solution for language learning!", SwingConstants.CENTER);
	    taglineLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
	    taglineLabel.setForeground(new Color(47, 79, 79));
	    gbc.gridy = 2;
	    mainPanel.add(taglineLabel, gbc);

	    gbc.gridy = 3;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;  
	    JButton loginButton = createButton("Login", new Color(0, 51, 153), Color.WHITE);
	    loginButton.addActionListener(event -> navigateToLogin(facade, userBo));
	    mainPanel.add(loginButton, gbc);

	    JLabel footerLabel = new JLabel("© 2025 Multilingual Dictionary, All Rights Reserved", SwingConstants.CENTER);
	    footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	    footerLabel.setForeground(Color.GRAY);
	    gbc.gridy = 4;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	    mainPanel.add(footerLabel, gbc);

	    return mainPanel;
	}


	private JPanel createAboutPanel() {
		JPanel aboutPanel = new JPanel(new BorderLayout());
		aboutPanel.setBackground(new Color(240, 248, 255));

		JLabel headerLabel = new JLabel("About Us", SwingConstants.CENTER);
		headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
		headerLabel.setForeground(new Color(25, 25, 112));
		aboutPanel.add(headerLabel, BorderLayout.NORTH);

		JTextArea aboutText = new JTextArea(
				"Multilingual Dictionary helps you learn and translate words in multiple languages.\n\n" + "Features:\n"
						+ "- Translate words in multiple languages.\n" + "- User-friendly interface.\n"
						+ "- Powerful search and learning tools.\n\n"
						+ "Built with passion for language enthusiasts worldwide.");
		aboutText.setFont(new Font("Arial", Font.PLAIN, 16));
		aboutText.setEditable(false);
		aboutText.setLineWrap(true);
		aboutText.setWrapStyleWord(true);
		aboutText.setBackground(new Color(240, 248, 255));

		JScrollPane scrollPane = new JScrollPane(aboutText);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		aboutPanel.add(scrollPane, BorderLayout.CENTER);

		JLabel footerLabel = new JLabel("Thank you for choosing Multilingual Dictionary!", SwingConstants.CENTER);
		footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
		footerLabel.setForeground(new Color(47, 79, 79));
		aboutPanel.add(footerLabel, BorderLayout.SOUTH);

		return aboutPanel;
	}

	private JPanel createContactPanel() {
		JPanel contactPanel = new JPanel(new BorderLayout());
		contactPanel.setBackground(new Color(240, 248, 255));

		JLabel headerLabel = new JLabel("Contact Us", SwingConstants.CENTER);
		headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
		headerLabel.setForeground(new Color(25, 25, 112));
		contactPanel.add(headerLabel, BorderLayout.NORTH);

		JTextPane contactText = new JTextPane();
		contactText.setContentType("text/html");
		contactText.setText("<html>" + "<body style='font-family:Arial, sans-serif; font-size:14px; color:#2F4F4F;'>"
				+ "<h3>Get in Touch</h3>"
				+ "<p>Email: <a href='mailto:support@multilingualdictionary.com'>support@multilingualdictionary.com</a></p>"
				+ "<p>Phone: +1 (800) 123-4567</p>"
				+ "<p>Website: <a href='http://www.multilingualdictionary.com'>www.multilingualdictionary.com</a></p>"
				+ "<h3>Follow Us</h3>"
				+ "<p>Facebook: <a href='https://fb.com/multilingualdictionary'>fb.com/multilingualdictionary</a></p>"
				+ "<p>Twitter: <a href='https://twitter.com/multilangdict'>@multilangdict</a></p>"
				+ "<p>Instagram: <a href='https://instagram.com/multilangdict'>@multilangdict</a></p>"
				+ "</body></html>");
		contactText.setEditable(false);
		contactText.setBackground(new Color(240, 248, 255));
		contactText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JScrollPane scrollPane = new JScrollPane(contactText);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		contactPanel.add(scrollPane, BorderLayout.CENTER);

		JLabel footerLabel = new JLabel("We are here to assist you!", SwingConstants.CENTER);
		footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
		footerLabel.setForeground(new Color(47, 79, 79));
		contactPanel.add(footerLabel, BorderLayout.SOUTH);

		return contactPanel;
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

	private void navigateToLogin(IBLFacade facade, UserBO userBo) {
		getContentPane().removeAll();
		add(new LoginUI(facade, userBo));
		revalidate();
		repaint();
	}

	public static void main(String[] args) {
		UserBO userBO = new UserBO();
		WordBO wordBO = new WordBO();
		IBLFacade facade = new BLFacade(wordBO, userBO);

		new WelcomeUI(facade, userBO);
	}
}
