package dal;

import dto.Word;
import java.util.HashMap;
import java.util.Map;

public class WordDAO {
    private Map<String, String> words;

    public WordDAO() {
        words = new HashMap<>();

        words.put("admin", "password");
        words.put("user1", "pass123");
    }

    public Word getWord(String username) {
        if (words.containsKey(username)) {
            return new Word(username, words.get(username));
        }
        return null;
    }
}