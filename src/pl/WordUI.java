package pl;

import bl.WordBO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WordUI extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private WordBO wordBO;

	private JTable table;
	private JLabel wordLabel, meaningLabel;

	public WordUI() {
		wordBO = new WordBO();
		setTitle("Multilingual Dictionary");
		setSize(350, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.WHITE);

		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(5, 2, 10, 10));
		loginPanel.setBackground(new Color(173, 216, 230));

		JLabel titleLabel = new JLabel("Multilingual Dictionary", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		loginPanel.add(titleLabel);
		loginPanel.add(new JLabel());

		JLabel usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(15);
		loginPanel.add(usernameLabel);
		loginPanel.add(usernameField);

		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(15);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordField);

		loginButton = new JButton("Login");
		loginButton.setBackground(new Color(0, 123, 255));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.setOpaque(true);
		loginPanel.add(new JLabel());
		loginPanel.add(loginButton);

		add(loginPanel);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (wordBO.validateWord(username, password)) {
					JOptionPane.showMessageDialog(WordUI.this, "Login successful!");
				} else {
					JOptionPane.showMessageDialog(WordUI.this, "Invalid credentials, please try again.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public static void main(String[] args) {
		WordUI login = new WordUI();
		login.setVisible(true);

	}
}