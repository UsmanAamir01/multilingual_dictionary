package pl;

import bl.IBLFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DictionaryApp extends JFrame {
	private final IBLFacade facade;
	private JTable wordsTable;
	private JFrame mainDashboard;
	private DefaultTableModel tableModel;

	public DictionaryApp(IBLFacade facade, JFrame mainDashboard) {
		this.facade = facade;
		this.mainDashboard = mainDashboard;
		mainDashboard.setVisible(false);
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Custom Dictionary");
		setSize(750, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		mainPanel.setBackground(new Color(245, 245, 245));

		JLabel headingLabel = createHeadingLabel("Custom Dictionary");
		mainPanel.add(headingLabel, BorderLayout.NORTH);

		wordsTable = createWordsTable();
		JScrollPane scrollPane = new JScrollPane(wordsTable);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Processed Words and Meanings"));
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = createButtonPanel();
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setBackground(new Color(245, 245, 245));

		JButton loadFileButton = createButton("Load File", new Color(0, 51, 153));
		loadFileButton.addActionListener(e -> processFile());

		JButton backButton = createButton("Back", new Color(0, 123, 255));
		backButton.addActionListener(e -> {
			this.setVisible(false);
			mainDashboard.setVisible(true);
		});

		buttonPanel.add(loadFileButton);
		buttonPanel.add(backButton);

		return buttonPanel;
	}

	private JTable createWordsTable() {
		String[] columnNames = { "Word", "Urdu Meaning", "Persian Meaning" };
		tableModel = new DefaultTableModel(columnNames, 0);
		JTable table = new JTable(tableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(60, 60, 60));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setBackground(new Color(250, 250, 250));
		table.setForeground(Color.DARK_GRAY);

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (row % 2 == 0) {
					c.setBackground(new Color(235, 235, 235));
				} else {
					c.setBackground(Color.WHITE);
				}
				return c;
			}
		});

		return table;
	}

	private void processFile() {
		tableModel.setRowCount(0);
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(this);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();

			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line;
				ArrayList<String> wordList = new ArrayList<>();
				while ((line = br.readLine()) != null) {
					String[] words = line.split("\\s+");
					Collections.addAll(wordList, words);
				}

				String[] wordsArray = wordList.toArray(new String[0]);
				WordProcessingThread processingThread = new WordProcessingThread(wordsArray, tableModel, facade);
				processingThread.start();

			} catch (IOException ioException) {
				JOptionPane.showMessageDialog(this, "Error reading file: " + ioException.getMessage());
			}
		}
	}

	private JLabel createHeadingLabel(String text) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(new Font("Segoe UI", Font.BOLD, 24));
		label.setForeground(new Color(25, 25, 112));
		label.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		return label;
	}

	private JButton createButton(String text, Color backgroundColor) {
		JButton button = new JButton(text);
		button.setBackground(backgroundColor);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(200, 40));
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2),
				BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		button.setOpaque(true);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(backgroundColor.brighter());
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(backgroundColor);
			}
		});

		return button;
	}

}
