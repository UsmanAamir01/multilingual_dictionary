package bl;

import java.util.List;

import dal.WordDAO;
import dto.Word;

public class WordBO {
    private WordDAO wordDAO;

    public WordBO() {
        wordDAO = new WordDAO();
    }

    public boolean updateWord(Word w) {
        return wordDAO.updateWordToDB(w);
    }

    public boolean addWord(Word w) {
        return wordDAO.addWordToDB(w);
    }

    public boolean removeWord(String word) {
        return wordDAO.removeWordFromDB(word);
    }

    public List<Word> getAllWords() {
        return wordDAO.getAllWords();
    }

    public String[][] getWordsWithMeanings() {
        List<Word> wordList = getAllWords();
        String[][] wordData = new String[wordList.size()][2];

        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            wordData[i][0] = word.getWord();
            wordData[i][1] = word.getMeaning();
        }

        return wordData;
    }

    public List<Word> importDataFromFile(String filePath) {
        return wordDAO.importDataFromFile(filePath);
    }

    public boolean insertImportedData(List<Word> words) {
        return wordDAO.insertImportedData(words);
    }
}
