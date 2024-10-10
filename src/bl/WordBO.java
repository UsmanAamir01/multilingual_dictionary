package bl;

import java.util.List;

import dal.WordDAO;
import dto.Word;

public class WordBO {
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
    	return wordDAO.updateWordToDB(w);
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
    
}