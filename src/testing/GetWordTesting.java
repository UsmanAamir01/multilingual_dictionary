package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class GetWordTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
    }

    @Test
    public void testGetWordFromDB_ValidWord() {
        String arabicWord = "دوست";
        Word word = dao.getWordFromDB(arabicWord);
        assertNotNull(word, "The word should be found in the database.");
        assertEquals("دوست", word.getArabicWord(), "The Arabic word should be 'دوست'.");
        assertEquals("دوست", word.getUrduMeaning(), "The Urdu meaning should be 'دوست'.");
        assertEquals("دوست", word.getPersianMeaning(), "The Persian meaning should be 'دوست'.");
    }

    @Test
    public void testGetWordFromDB_NonExistentWord() {
        String arabicWord = "كتا";
        Word word = dao.getWordFromDB(arabicWord);
        assertNull(word, "The word should not be found in the database.");
    }

    @Test
    public void testGetWordFromDB_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> dao.getWordFromDB(null),
                "Fetching a null word should throw an IllegalArgumentException.");
    }
}
