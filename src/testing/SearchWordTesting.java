package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class SearchWordTesting {

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

            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE urdumeaning AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE persianmeaning AUTO_INCREMENT = 1");

            // Insert sample words into the 'dictionary' table for testing
            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('example', 'نمونہ', 'مثال')");
            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('test', 'آزمائش', 'آزمایشی')");
            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('search', 'تلاش', 'جستجو')");
        }
    }

    @Test
    public void testSearchWord_ValidSearchTerm() {
        // Valid search term that matches multiple words
        String searchTerm = "test";

        // Perform the search
        List<Word> results = dao.searchWord(searchTerm);

        // Assert that the result list is not empty
        assertNotNull(results, "The search results should not be null.");
        assertFalse(results.isEmpty(), "The search results should not be empty.");
        
        // Assert that the correct word is found
        boolean foundTest = false;
        for (Word word : results) {
            if ("test".equals(word.getArabicWord())) {
                foundTest = true;
                break;
            }
        }
        assertTrue(foundTest, "The word 'test' should be found in the search results.");
    }

    @Test
    public void testSearchWord_NoResults() {
        // Search term that doesn't match any word in the database
        String searchTerm = "nonexistent";

        // Perform the search
        List<Word> results = dao.searchWord(searchTerm);

        // Assert that no results are found
        assertNotNull(results, "The search results should not be null.");
        assertTrue(results.isEmpty(), "The search results should be empty.");
    }

    @Test
    public void testSearchWord_EmptySearchTerm() {
        // Empty search term
        String searchTerm = "";

        // Perform the search
        List<Word> results = dao.searchWord(searchTerm);

        // Assert that no results are found for an empty search term
        assertNotNull(results, "The search results should not be null.");
        assertTrue(results.isEmpty(), "The search results should be empty.");
    }

    @Test
    public void testSearchWord_PartialMatch() {
        // Search term that matches part of a word
        String searchTerm = "ex";

        // Perform the search
        List<Word> results = dao.searchWord(searchTerm);

        // Assert that the word "example" is found
        assertNotNull(results, "The search results should not be null.");
        assertFalse(results.isEmpty(), "The search results should not be empty.");

        boolean foundExample = false;
        for (Word word : results) {
            if ("example".equals(word.getArabicWord())) {
                foundExample = true;
                break;
            }
        }
        assertTrue(foundExample, "The word 'example' should be found in the search results.");
    }
}
