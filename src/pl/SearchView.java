package pl;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import bl.WordBO;

public class SearchView extends JPanel {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<String> searchTypeComboBox;
	private JComboBox<String> languageComboBox;
	private WordBO wordBo;

	public SearchView(WordBO wordBo) {
		this.wordBo = wordBo;
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		String[] searchTypes = { "Key", "Value" };
		searchTypeComboBox = new JComboBox<>(searchTypes);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(searchTypeComboBox, gbc);
		JLabel searchLabel = new JLabel("Search Word:");
		searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		gbc.gridx = 1;
		add(searchLabel, gbc);
		searchField = new JTextField(15);
		gbc.gridx = 2;
		add(searchField, gbc);
		String[] languages = { "Arabic", "Urdu", "Persian" };
		languageComboBox = new JComboBox<>(languages);
		gbc.gridx = 3;
		add(languageComboBox, gbc);
		searchButton = new JButton("Search");
		gbc.gridx = 4;
		add(searchButton, gbc);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wordText = searchField.getText();
				String selectedLanguage = (String) languageComboBox.getSelectedItem();
				if (!wordText.isEmpty()) {
					String result = wordBo.getMeanings(wordText, selectedLanguage);
					if (!result.equals("Word not found.")) {
						JOptionPane.showMessageDialog(SearchView.this, "Search result:\n" + result);
					} else {
						JOptionPane.showMessageDialog(SearchView.this, "Word not found: " + wordText, "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(SearchView.this, "Please enter a word to search.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}