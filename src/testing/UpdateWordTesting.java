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

public class UpdateWordTesting {

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
    public void testUpdateWordInDB_ValidWord() throws SQLException {
        Word word = new Word("example", "نیا معنی", "جدید مثال");
        assertTrue(dao.updateWordToDB(word), "The word should be updated successfully.");
    }

    @Test
    public void testUpdateWordInDB_NonExistentWord() throws SQLException {
        Word word = new Word("nonexistent", "معنی", "مثال");
        assertFalse(dao.updateWordToDB(word), "Updating a non-existent word should return false.");
    }

    @Test
    public void testUpdateWordInDB_NullWord() {
        assertThrows(IllegalArgumentException.class, () -> dao.updateWordToDB(null), 
                "Updating a null word should throw IllegalArgumentException.");
    }
}
