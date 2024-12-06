package pl;

import java.awt.*;
import javax.swing.*;
import bl.IBLFacade;
import dto.Word;

public class ViewOnceWordView extends JFrame {
	private IBLFacade facade;
	private JLabel arabicWordLabel, urduMeaningLabel, persianMeaningLabel, favoriteLabel;
	private JFrame parentFrame;
	private JButton favoriteButton;

	public ViewOnceWordView(IBLFacade facade, JFrame parentFrame, String arabicWord) {
		this.facade = facade;
		this.parentFrame = parentFrame;

		setTitle("Word Details");
		setSize(700, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(new Color(245, 245, 245));
		setLayout(new BorderLayout(10, 10));

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		mainPanel.setBackground(Color.WHITE);

		JLabel headingLabel = createHeadingLabel("Word Details");
		mainPanel.add(headingLabel, BorderLayout.NORTH);

		JPanel detailsPanel = createDetailsPanel();
		mainPanel.add(detailsPanel, BorderLayout.CENTER);

		JPanel footerPanel = createFooterPanel();
		mainPanel.add(footerPanel, BorderLayout.SOUTH);

		add(mainPanel);
		displayWordDetails(arabicWord);
	}

	private JPanel createDetailsPanel() {
		JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		detailsPanel.setBackground(Color.WHITE);

		arabicWordLabel = createMeaningLabel("Arabic Word: ");
		urduMeaningLabel = createMeaningLabel("Urdu Meaning: ");
		persianMeaningLabel = createMeaningLabel("Persian Meaning: ");
		favoriteLabel = new JLabel();
		favoriteLabel.setOpaque(true);
		favoriteLabel.setBackground(new Color(240, 248, 255));

		detailsPanel.add(arabicWordLabel);
		detailsPanel.add(urduMeaningLabel);
		detailsPanel.add(persianMeaningLabel);
		detailsPanel.add(favoriteLabel);

		return detailsPanel;
	}

	private JPanel createFooterPanel() {
		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		footerPanel.setBackground(new Color(245, 245, 245));

		JButton backButton = createButton("Back");
		backButton.setToolTipText("Return to the previous screen");

		backButton.addActionListener(e -> {
			dispose();
			parentFrame.setVisible(true);
		});

		favoriteButton = createFavoriteButton();
		favoriteButton.addActionListener(e -> markAsFavorite());

		footerPanel.add(backButton);
		footerPanel.add(favoriteButton);
		return footerPanel;
	}

	private void displayWordDetails(String arabicWord) {
		Word word = facade.viewOnceWord(arabicWord);

		if (word != null) {
			arabicWordLabel.setText("Arabic Word: " + word.getArabicWord());
			urduMeaningLabel.setText("Urdu Meaning: " + word.getUrduMeaning());
			persianMeaningLabel.setText("Persian Meaning: " + word.getPersianMeaning());
			updateLabelBackgrounds(new Color(224, 247, 218));

			boolean isFavorite = facade.isWordFavorite(word.getArabicWord());
			updateFavoriteIcon(isFavorite);
		} else {
			arabicWordLabel.setText("Arabic Word: Word not found.");
			urduMeaningLabel.setText("Urdu Meaning: ");
			persianMeaningLabel.setText("Persian Meaning: ");
			updateLabelBackgrounds(new Color(255, 228, 225));
		}
	}

	private void updateFavoriteIcon(boolean isFavorite) {
		if (isFavorite) {
			favoriteLabel.setIcon(new ImageIcon(getClass().getResource("/images/icon_filledstar.png")));
		} else {
			favoriteLabel.setIcon(new ImageIcon(getClass().getResource("/images/icon_hollowstar.png")));
		}
		favoriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void markAsFavorite() {
		String arabicWord = arabicWordLabel.getText().replace("Arabic Word: ", "").trim();
		boolean isFavorite = facade.isWordFavorite(arabicWord);
		boolean newStatus = !isFavorite; // Toggle the favorite status
		facade.markWordAsFavorite(arabicWord, newStatus);

		updateFavoriteIcon(newStatus);
	}

	private void updateLabelBackgrounds(Color color) {
		arabicWordLabel.setBackground(color);
		urduMeaningLabel.setBackground(color);
		persianMeaningLabel.setBackground(color);
	}

	private JLabel createHeadingLabel(String text) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 26));
		label.setForeground(new Color(70, 130, 180));
		label.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		return label;
	}

	private JLabel createMeaningLabel(String text) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(new Color(240, 248, 255));
		label.setForeground(new Color(25, 25, 112));
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)),
				BorderFactory.createEmptyBorder(12, 15, 12, 15)));
		return label;
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(new Color(30, 144, 255));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)),
				BorderFactory.createEmptyBorder(10, 25, 10, 25)));
		button.setOpaque(true);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private JButton createFavoriteButton() {
		JButton button = new JButton("Add to Favorites");
		button.setBackground(new Color(255, 165, 0)); // Enhanced color
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 140, 0)),
				BorderFactory.createEmptyBorder(10, 25, 10, 25)));
		button.setOpaque(true);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}
}
