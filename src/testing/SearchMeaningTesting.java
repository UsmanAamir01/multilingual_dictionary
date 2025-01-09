package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;

public class SearchMeaningTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM persian_meaning");
            stmt.execute("DELETE FROM urdu_meaning");
            stmt.execute("DELETE FROM arabic_word");
            stmt.execute("DELETE FROM dictionary");

            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('example', 'نمونہ', 'مثال')");
            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('test', 'آزمائش', 'آزمایشی')");
            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('search', 'تلاش', 'جستجو')");
        }
    }

    @Test
    public void testGetMeanings_ValidWord_Urdu() {
        String word = "نمونہ";  // Urdu meaning for "example"
        String language = "Urdu";
        String expected = "Arabic: example\nPersian: مثال\n";

        String result = dao.getMeanings(word, language);

        assertNotNull(result, "The result should not be null.");
        assertEquals(expected, result, "The meanings should be retrieved correctly for Urdu.");
    }

    @Test
    public void testGetMeanings_ValidWord_Persian() {
        String word = "آزمایشی";  // Persian meaning for "test"
        String language = "Persian";
        String expected = "Arabic: test\nUrdu: آزمائش\n";

        String result = dao.getMeanings(word, language);

        assertNotNull(result, "The result should not be null.");
        assertEquals(expected, result, "The meanings should be retrieved correctly for Persian.");
    }

    @Test
    public void testGetMeanings_ValidWord_Arabic() {
        String word = "search";  // Arabic word for "search"
        String language = "Arabic";
        String expected = "Urdu: تلاش\nPersian: جستجو\n";

        String result = dao.getMeanings(word, language);

        assertNotNull(result, "The result should not be null.");
        assertEquals(expected, result, "The meanings should be retrieved correctly for Arabic.");
    }

    @Test
    public void testGetMeanings_InvalidLanguage() {
        String word = "example";
        String language = "French";  // Invalid language
        String expected = "Invalid language selection.";

        String result = dao.getMeanings(word, language);

        assertNotNull(result, "The result should not be null.");
        assertEquals(expected, result, "The result should indicate invalid language selection.");
    }

    @Test
    public void testGetMeanings_WordNotFound() {
        String word = "nonexistent";
        String language = "Urdu";
        String expected = "Word not found.";

        String result = dao.getMeanings(word, language);

        assertNotNull(result, "The result should not be null.");
        assertEquals(expected, result, "The result should indicate that the word was not found.");
    }
}
