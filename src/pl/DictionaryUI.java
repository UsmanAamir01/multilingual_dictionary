package pl;

import bl.WordBO;
import dto.Word;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

public class DictionaryUI extends JFrame {
	private JTextField filePathField;
	private JTable dataTable;
	private JButton viewButton;
	private WordBO wordBO;
	private JFrame previousWindow;

	public DictionaryUI(WordBO wordBO, JFrame previousWindow) {
		this.wordBO = wordBO;
		this.previousWindow = previousWindow;
		createAndShowGUI();
	}

	public void createAndShowGUI() {
		setTitle("Import Dictionary Data");
		JButton importButton = new JButton("Import Data");
		JButton backButton = new JButton("Back");
		filePathField = new JTextField(20);
		dataTable = new JTable();
		viewButton = new JButton("View Data");

		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(DictionaryUI.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String filePath = selectedFile.getAbsolutePath();
					filePathField.setText(filePath);
					List<Word> importedWords = wordBO.importDataFromFile(filePath);
					if (!importedWords.isEmpty() && wordBO.insertImportedData(importedWords)) {
						DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel();
						tableModel.setRowCount(0);
						for (Word word : importedWords) {
							tableModel.addRow(new Object[] { word.getWord(), word.getMeaning() });
						}
						JOptionPane.showMessageDialog(DictionaryUI.this, "Data imported successfully!");
						viewButton.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(DictionaryUI.this,
								"No valid data found in the file or import failed.");
					}
				}
			}
		});

		viewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(DictionaryUI.this, "Viewing imported data...");
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previousWindow.setVisible(true);
				dispose();
			}
		});

		String[] columnNames = { "Word", "Meaning" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		dataTable.setModel(tableModel);

		JPanel panel = new JPanel();
		panel.add(new JLabel("File Path:"));
		panel.add(filePathField);
		panel.add(importButton);
		panel.add(viewButton);
		panel.add(backButton);
		panel.add(new JScrollPane(dataTable));

		add(panel);
		setSize(500, 500);
		setVisible(true);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		viewButton.setEnabled(false);
	}
}
