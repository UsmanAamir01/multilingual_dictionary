package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import bl.WordBO;

public class ArabicTaggerUI extends JFrame {
	private WordBO wordBO;
	private JTextField arabicWordTextField;
	private JTextArea outputTextArea;
	private JButton tagButton;

	public ArabicTaggerUI() {
		wordBO = new WordBO();

		setTitle("Arabic POS Tagger");
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBackground(new Color(240, 240, 240));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel label = new JLabel("Enter Arabic Text:");
		label.setFont(new Font("Arial", Font.BOLD, 14));
		label.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 0;
		inputPanel.add(label, gbc);

		arabicWordTextField = new JTextField(25);
		gbc.gridx = 1;
		inputPanel.add(arabicWordTextField, gbc);

		tagButton = new JButton("Tag");
		tagButton.setBackground(new Color(50, 150, 250));
		tagButton.setForeground(Color.WHITE);
		tagButton.setFont(new Font("Arial", Font.BOLD, 14));
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		inputPanel.add(tagButton, gbc);

		outputTextArea = new JTextArea();
		outputTextArea.setEditable(false);
		outputTextArea.setLineWrap(true);
		outputTextArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(outputTextArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("POS Tagged Result"));

		add(inputPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		tagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String arabicText = arabicWordTextField.getText().trim();
				if (!arabicText.isEmpty()) {
					try {
						String posTaggedResult = wordBO.performPOSTagging(arabicText);
						outputTextArea.setText(posTaggedResult);
					} catch (Exception ex) {
						outputTextArea.setText("Error during POS tagging: " + ex.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(ArabicTaggerUI.this, "Please enter Arabic text.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public static void main(String[] args) {
			ArabicTaggerUI a = new ArabicTaggerUI();
			a.setVisible(true);
	}
}
