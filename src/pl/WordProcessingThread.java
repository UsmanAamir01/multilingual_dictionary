package pl;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;

public class WordProcessingThread extends Thread {
    private String[] words;
    private DefaultTableModel tableModel;
    private IBLFacade facade;

    public WordProcessingThread(String[] words, DefaultTableModel tableModel, IBLFacade facade) {
        this.words = words;
        this.tableModel = tableModel;
        this.facade = facade;
    }

    @Override
    public void run() {
        for (String word : words) {
            try {
                String[] meanings = facade.getMeaning1(word);
                String urduMeaning = meanings != null && meanings.length > 0 ? meanings[0] : "Not found";
                String persianMeaning = meanings != null && meanings.length > 1 ? meanings[1] : "Not found";
                Word wordObj = new Word(word, urduMeaning, persianMeaning);
                facade.addWord(wordObj);
                SwingUtilities.invokeLater(() -> tableModel.addRow(new Object[]{word, urduMeaning, persianMeaning, null}));
                Thread.sleep(500);  
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> tableModel.addRow(new Object[]{word, "Error retrieving meaning", "Error retrieving meaning", null}));
            }
        }
    }
}
