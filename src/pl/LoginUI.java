package pl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import bl.BLFacade;
import bl.IBLFacade;
import bl.UserBO;
import bl.WordBO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginUI extends JPanel {

	private static final Logger logger = LogManager.getLogger(LoginUI.class);
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private IBLFacade facade;

	public LoginUI(IBLFacade facade, UserBO userBo) {
		this.facade = facade;
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Multilingual Dictionary", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(0, 51, 153));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(titleLabel, gbc);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		usernameLabel.setForeground(Color.DARK_GRAY);
		usernameField = new JTextField(15);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(usernameLabel, gbc);
		gbc.gridx = 1;
		add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		passwordLabel.setForeground(Color.DARK_GRAY);
		passwordField = new JPasswordField(15);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(passwordLabel, gbc);
		gbc.gridx = 1;
		add(passwordField, gbc);

		loginButton = createButton("Login", new Color(0, 51, 153), Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		add(loginButton, gbc);

		loginButton.addActionListener(event -> handleLogin(userBo));
	}

	private void handleLogin(UserBO userBo) {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		if (userBo.validateUser(username, password)) {
			JOptionPane.showMessageDialog(this, "Login successful!");
			logger.info("Login successful for user: " + username);
			navigateToWordUI();
		} else {
			JOptionPane.showMessageDialog(this, "Invalid credentials, please try again.", "Error",
					JOptionPane.ERROR_MESSAGE);
			logger.warn("Failed login attempt for user: " + username);
		}
	}

	private void navigateToWordUI() {
	    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
	    loginFrame.setVisible(false);

	    WordUI wordUI = new WordUI(facade);
	    wordUI.setVisible(true);  
	    loginFrame.dispose();
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

	public static void main(String[] args) {
		UserBO userBO = new UserBO();
		WordBO wordBO = new WordBO();
		IBLFacade facade = new BLFacade(wordBO, userBO);

		JFrame frame = new JFrame("Login");
		LoginUI loginUI = new LoginUI(facade, userBO);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 550);
		frame.setLocationRelativeTo(null);
		frame.add(loginUI);
		frame.setVisible(true);
	}
}
