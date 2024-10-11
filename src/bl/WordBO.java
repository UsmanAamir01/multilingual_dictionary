package bl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dal.WordDAO;
import dto.Word;

public class WordBO {
    private static final Logger LOGGER = Logger.getLogger(WordBO.class.getName());
    //zainab's commit
    private WordDAO wordDAO;
    public WordBO() {
        wordDAO = new WordDAO();
    }
    public boolean updateWord(Word w)
    {
    	
    	return wordDAO.updateWordToDB(w);
    }
    public boolean addWord(Word w)
    {
    	return wordDAO.addWordToDB(w);
    }
    public boolean removeWord(String word)
    {
    	return wordDAO.removeWordFromDB(word);
    }
    
    public String[][] getWordsWithMeanings() {
        List<Word> wordList = wordDAO.getAllWords();
        String[][] wordData = new String[wordList.size()][2];

        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            wordData[i][0] = word.getWord();
            wordData[i][1] = word.getMeaning();
        }

        return wordData;
    }
    

    // Import data from a file
    public List<Word> importDataFromFile(String filePath) {
        List<Word> importedWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Trim leading and trailing spaces
                if (line.isEmpty()) continue; // Skip empty lines
                
                String[] parts = line.split(","); // Change delimiter to ';'
                if (parts.length >= 1) { // Ensure at least word and meaning1 are present
                    String wordText = parts[0].trim();
                    String meaning1Text = parts[1].trim();                    
                    Word newWord = new Word(wordText, meaning1Text);
                    importedWords.add(newWord);
                } else {
                    LOGGER.log(Level.WARNING, "Invalid line format: {0}", line);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: {0}", e.getMessage());
        }
        return importedWords;
    }

    // Insert imported data into the database
    public boolean insertImportedData(List<Word> words) {
        boolean allInserted = true; // Track if all inserts succeed
        for (Word word : words) {
            if (!addWord(word)) {
                allInserted = false; // Mark as false if any insert fails
                LOGGER.log(Level.SEVERE, "Failed to add word: {0}", word.getWord());
            }
        }
        return allInserted; // Return true if all inserts succeed
    }
}