package bl;

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
    
    
    
}