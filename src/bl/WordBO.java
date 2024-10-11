package bl;

import dal.WordDAO;
import dto.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordBO {
    private static final Logger LOGGER = Logger.getLogger(WordBO.class.getName());
    private WordDAO wordDAO;

    public WordBO() {
        wordDAO = new WordDAO();
    }

    // Update an existing word
    public boolean updateWord(Word w) {
        return wordDAO.updateWordToDB(w);
    }

    // Add a new word
    public boolean addWord(Word w) {
        if (validateWord(w)) {
            return wordDAO.addWordToDB(w);
        } else {
            LOGGER.log(Level.WARNING, "Invalid word data: {0}", w);
            return false;
        }
    }

    // Remove a word
    public boolean removeWord(String word) {
        return wordDAO.removeWordFromDB(word);
    }

    // Get all words and their meanings
    public List<Word> getAllWords() {
        return wordDAO.getAllWords();
    }

    // Import data from a file
    public List<Word> importDataFromFile(String filePath) {
        List<Word> importedWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Trim leading and trailing spaces
                if (line.isEmpty()) continue; // Skip empty lines
                
                String[] parts = line.split(";"); // Change delimiter to ';'
                if (parts.length >= 2) { // Ensure at least word and meaning1 are present
                    String wordText = parts[0].trim();
                    String meaning1Text = parts[1].trim();
                    String meaning2Text = parts.length > 2 ? parts[2].trim() : ""; // Handle optional meaning
                    
                    Word newWord = new Word(wordText, meaning1Text, meaning2Text);
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

    // Validate the word data
    private boolean validateWord(Word word) {
        return word.getWord() != null && !word.getWord().isEmpty()
                && word.getMeaning1() != null && !word.getMeaning1().isEmpty();
    }

    // Get words with meanings in a 2D array format
    public String[][] getWordsWithMeanings() {
        List<Word> wordList = getAllWords();
        String[][] wordData = new String[wordList.size()][3]; // Adjust for multiple meanings

        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            wordData[i][0] = word.getWord();
            wordData[i][1] = word.getMeaning1();
            wordData[i][2] = word.getMeaning2(); // Handle optional meaning
        }

        return wordData;
    }
}
