package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class GetWordTesting {

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
            stmt.execute("INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES ('example', 'نمونہ', 'مثال')");
        }
    }

    @Test
    public void testGetWordFromDB_ValidWord() {
        String arabicWord = "example";
        Word word = dao.getWordFromDB(arabicWord);
        assertNotNull(word, "The word should be found in the database.");
        assertEquals("example", word.getArabicWord(), "The Arabic word should be 'example'.");
        assertEquals("نمونہ", word.getUrduMeaning(), "The Urdu meaning should be 'نمونہ'.");
        assertEquals("مثال", word.getPersianMeaning(), "The Persian meaning should be 'مثال'.");
    }

    @Test
    public void testGetWordFromDB_NonExistentWord() {
        String arabicWord = "nonexistent";
        Word word = dao.getWordFromDB(arabicWord);
        assertNull(word, "The word should not be found in the database.");
    }

    @Test
    public void testGetWordFromDB_NullWord() {
        String arabicWord = null;
        Word word = dao.getWordFromDB(arabicWord);
        assertNull(word, "The result should be null when the word is null.");
    }
}