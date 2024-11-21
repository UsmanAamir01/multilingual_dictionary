package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import bl.IBLFacade;
import dto.Word;

public class ViewFavorites extends JFrame {
	private IBLFacade facade;
	private JList<String> favoritesList;
	private JButton closeButton;
	private JButton backButton;
	private JFrame previousWindow;

	public ViewFavorites(JFrame previousWindow, IBLFacade facade) {
		this.previousWindow = previousWindow;
		this.facade = facade;
		setTitle("Favourites");
		setSize(700, 500);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(0, 123, 255));
		headerPanel.setPreferredSize(new Dimension(700, 70));
		JLabel titleLabel = new JLabel("My Favourites", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		headerPanel.add(titleLabel, BorderLayout.CENTER);
		add(headerPanel, BorderLayout.NORTH);

		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		List<Word> favoriteWords = facade.getFavoriteWords();
		DefaultListModel<String> listModel = new DefaultListModel<>();

		for (Word word : favoriteWords) {
			listModel.addElement(word.getArabicWord());
		}

		favoritesList = new JList<>(listModel);
		favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		favoritesList.setFont(new Font("Arial", Font.PLAIN, 16));
		favoritesList.setBackground(new Color(248, 248, 255));
		favoritesList.setForeground(new Color(33, 37, 41));
		favoritesList.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 1));
		JScrollPane scrollPane = new JScrollPane(favoritesList);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 1),
				"Favourite Words", 0, 0, new Font("Arial", Font.BOLD, 14), new Color(0, 51, 153)));
		listPanel.add(scrollPane, BorderLayout.CENTER);

		closeButton = createButton("Close", new Color(220, 53, 69), new Color(255, 255, 255));
		closeButton.addActionListener(e -> dispose());

		backButton = createButton("Back", new Color(40, 167, 69), new Color(255, 255, 255));
		backButton.addActionListener(e -> {
			previousWindow.setVisible(true);
			dispose();
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		buttonPanel.add(backButton);
		buttonPanel.add(closeButton);

		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(listPanel, BorderLayout.CENTER);

		getContentPane().setBackground(Color.WHITE);
		setVisible(true);
	}

	private JButton createButton(String text, Color bgColor, Color fgColor) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setBackground(bgColor);
		button.setForeground(fgColor);
		button.setPreferredSize(new Dimension(150, 50));
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
		button.setToolTipText(text + " the favourites window");
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(bgColor.brighter());
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(bgColor);
			}
		});
		return button;
	}
}
