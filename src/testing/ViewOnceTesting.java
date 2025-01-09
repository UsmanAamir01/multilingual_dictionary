package testing;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ViewOnceTesting {

	private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	private WordDAO dao;
	private Connection connection;

	@BeforeEach
	public void setUp() throws Exception {
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
		dao = new WordDAO(connection);
	}

    @Test
    public void testViewOnceWord_ValidWord() throws SQLException {
        String arabicWord = "سلام";
        String urduMeaning = "سلام";
        String persianMeaning = "سلام";
        boolean isFavorite = true;

        String insertArabicQuery = "INSERT INTO arabic_word (id, arabic_word, isFavorite) VALUES (?, ?, ?)";
        String insertUrduQuery = "INSERT INTO urdu_meaning (id, urdu_meaning) VALUES (?, ?)";
        String insertPersianQuery = "INSERT INTO persian_meaning (id, persian_meaning) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertArabicQuery)) {
            stmt.setInt(1, 12);
            stmt.setString(2, arabicWord);
            stmt.setBoolean(3, isFavorite);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(insertUrduQuery)) {
            stmt.setInt(1, 12);
            stmt.setString(2, urduMeaning);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(insertPersianQuery)) {
            stmt.setInt(1, 12);
            stmt.setString(2, persianMeaning);
            stmt.executeUpdate();
        }

        Word word = dao.viewOnceWord(arabicWord);

        assertNotNull(word);
        assertEquals(arabicWord, word.getArabicWord());
        assertEquals(urduMeaning, word.getUrduMeaning());
        assertEquals(persianMeaning, word.getPersianMeaning());
        assertTrue(word.isFavorite());
    }

    @Test
    public void testViewOnceWord_InvalidWord() throws SQLException {
        String arabicWord = "غير موجود";
        Word word = dao.viewOnceWord(arabicWord);

        assertNull(word);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        String deletePersianQuery = "DELETE FROM persian_meaning WHERE id = 12";
        String deleteUrduQuery = "DELETE FROM urdu_meaning WHERE id = 12";
        String deleteArabicQuery = "DELETE FROM arabic_word WHERE id = 12";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deletePersianQuery);
            stmt.executeUpdate(deleteUrduQuery);
            stmt.executeUpdate(deleteArabicQuery);
        }
    }
}
