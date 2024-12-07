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

public class RemoveWordTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM persian_meaning");
            stmt.execute("DELETE FROM urdu_meaning");
            stmt.execute("DELETE FROM arabic_word");

            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE urdumeaning AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE persianmeaning AUTO_INCREMENT = 1");

            stmt.execute("INSERT INTO arabic_word (id, word) VALUES (1, 'example')");
            stmt.execute("INSERT INTO urdu_meaning (word_id, meaning) VALUES (1, 'نمونہ')");
            stmt.execute("INSERT INTO persian_meaning (word_id, meaning) VALUES (1, 'مثال')");
        }
    }

    @Test
    public void testRemoveWordFromDB_ValidWord() throws SQLException {
    	String arabicWord = "example";
        Word word = new Word("example", "نیا معنی", "جدید مثال");
        dao.addWordToDB(word);
        assertTrue(dao.removeWordFromDB(arabicWord), "The word should be removed successfully.");
    }

    @Test
    public void testRemoveWordFromDB_NonExistentWord() throws SQLException {
    	String arabicWord = null;
        Word word = new Word("nonexistent", "معنی", "مثال");
        assertFalse(dao.removeWordFromDB(arabicWord), "Removing a non-existent word should return false.");
    }

    @Test
    public void testRemoveWordFromDB_NullWord() {
    	String arabicWord = null;
        assertThrows(IllegalArgumentException.class, () -> dao.removeWordFromDB(arabicWord),
                "Removing a null word should throw IllegalArgumentException.");
    }
}
