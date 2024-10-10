package bl;

import dal.WordDAO;
import dto.Word;

public class WordBO {
    private WordDAO wordDAO;

    public WordBO() {
        wordDAO = new WordDAO();
    }

    public boolean validateWord(String username, String password) {
        Word word = wordDAO.getWord(username);
        return word != null && word.getPassword().equals(password);
    }
}