package pl;

import javax.swing.*;
import java.awt.*;

public class FAQsView extends JFrame {

	public FAQsView(JFrame parentFrame) {
		setTitle("Frequently Asked Questions");
		setSize(600, 450);
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		JTextArea faqsTextArea = new JTextArea();
		faqsTextArea.setEditable(false);
		faqsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
		faqsTextArea.setText("1. What is this dictionary app?\n"
				+ "   - This app allows you to search for words in multiple languages, including Arabic, Persian, and Urdu.\n\n"
				+ "2. How can I add a word?\n"
				+ "   - You can add a word by navigating to the 'Add Word' section from the dashboard.\n\n"
				+ "3. How does word segmentation work?\n"
				+ "   - Word segmentation breaks down words into their base components to improve search accuracy.\n\n"
				+ "4. Can I save words to favorites?\n"
				+ "   - Yes, you can add words to your favorites for quick access later.\n\n"
				+ "5. How do I change the app theme?\n"
				+ "   - You can toggle between light and dark mode by clicking the theme icon in the top right.\n");

		faqsTextArea.setWrapStyleWord(true);
		faqsTextArea.setLineWrap(true);
		faqsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JScrollPane scrollPane = new JScrollPane(faqsTextArea);
		add(scrollPane, BorderLayout.CENTER);

		JButton backButton = new JButton("Dashboard");
		backButton.setFont(new Font("Arial", Font.BOLD, 14));
		backButton.setBackground(new Color(0, 51, 153));
		backButton.setForeground(Color.WHITE);
		backButton.setPreferredSize(new Dimension(150, 40));
		backButton.setFocusPainted(false);
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.addActionListener(e -> {
			dispose();
			parentFrame.setVisible(true);
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.add(backButton);

		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

}
