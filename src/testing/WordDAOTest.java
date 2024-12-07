package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;

public class WordDAOTest {

    private WordDAO dao;
    private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createTableSQL = "CREATE TABLE dictionary (" +
                                    "arabic_word VARCHAR(255) PRIMARY KEY, " +
                                    "urdu_meaning VARCHAR(255), " +
                                    "persian_meaning VARCHAR(255));";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
            String insertSQL = "INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) " +
                               "VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "سلام");
                pstmt.setString(2, "Hello in Urdu");
                pstmt.setString(3, "Hello in Persian");
                pstmt.executeUpdate();
            }
        }
    }

    @Test
    public void testGetMeaningsFromDB_ValidWord() {
        String word = "سلام";
        String[] meanings = dao.getMeaningsFromDB(word);
        assertNotNull(meanings, "Meanings should not be null.");
        assertEquals(2, meanings.length, "There should be two meanings.");
        assertEquals("Hello in Urdu", meanings[0], "The Urdu meaning should be correct.");
        assertEquals("Hello in Persian", meanings[1], "The Persian meaning should be correct.");
    }

    @Test
    public void testGetMeaningsFromDB_InvalidWord() {
        String word = "غير موجود"; 
        String[] meanings = dao.getMeaningsFromDB(word);
        assertNotNull(meanings, "Meanings should not be null.");
        assertNull(meanings[0], "The Urdu meaning should be null for non-existent word.");
        assertNull(meanings[1], "The Persian meaning should be null for non-existent word.");
    }

    @Test
    public void testGetMeaningsFromDB_EmptyWord() {
        String word = ""; 
        String[] meanings = dao.getMeaningsFromDB(word);
        assertNotNull(meanings, "Meanings should not be null.");
        assertNull(meanings[0], "The Urdu meaning should be null for an empty word.");
        assertNull(meanings[1], "The Persian meaning should be null for an empty word.");
    }

    @Test
    public void testGetMeaningsFromDB_NullWord() {
        String word = null;  
        String[] meanings = dao.getMeaningsFromDB(word);
        assertNotNull(meanings, "Meanings should not be null.");
        assertNull(meanings[0], "The Urdu meaning should be null for a null word.");
        assertNull(meanings[1], "The Persian meaning should be null for a null word.");
    }
}
