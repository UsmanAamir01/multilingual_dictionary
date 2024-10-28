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

    public boolean removeWord(String arabicWord) {
        return wordDAO.removeWordFromDB(arabicWord);
    }

    public List<Word> getAllWords() {
        return wordDAO.getAllWords();
    }

    public String[][] getWordsWithMeanings() {
        List<Word> wordList = getAllWords();
        String[][] wordData = new String[wordList.size()][3]; 

        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            wordData[i][0] = word.getArabicWord();
            wordData[i][1] = word.getUrduMeaning();
            wordData[i][2] = word.getPersianMeaning(); 
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
