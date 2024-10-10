package pl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import bl.WordBO;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AllWordView extends JFrame {
	JTable table;
	JLabel wordLabel, meaningLabel;

	private WordBO word;

	public AllWordView() {

		word = new WordBO();

		String[][] wordData = word.getWordsWithMeanings();

		String[] columnNames = { "Word", "Meaning" };

		DefaultTableModel model = new DefaultTableModel(wordData, columnNames);

		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		wordLabel = new JLabel("Word: ");
		meaningLabel = new JLabel("Meaning: ");

		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					int selectedRow = table.getSelectedRow();
					if (selectedRow != -1) {
						String word = (String) table.getValueAt(selectedRow, 0);
						String meaning = (String) table.getValueAt(selectedRow, 1);
						wordLabel.setText("Word: " + word);
						meaningLabel.setText("Meaning: " + meaning);
					}
				}
			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2, 1));
		infoPanel.add(wordLabel);
		infoPanel.add(meaningLabel);
		add(infoPanel, BorderLayout.SOUTH);

		setTitle("Word and Meaning Viewer");
		setSize(500, 300);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		setVisible(true);
	}

	public static void main(String[] args) {
		AllWordView word = new AllWordView();
	}
}
