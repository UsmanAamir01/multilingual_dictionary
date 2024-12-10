package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class GetFavoriteWordsTesting {
    private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private WordDAO dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        dao = new WordDAO(connection);

        String insertArabicQuery = "INSERT INTO arabic_word (id, arabic_word, isFavorite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertArabicQuery)) {
            stmt.setInt(1, 12);
            stmt.setString(2, "سلام");
            stmt.setBoolean(3, true);
            stmt.executeUpdate();

            stmt.setInt(1, 13);
            stmt.setString(2, "مرحبا");
            stmt.setBoolean(3, false);
            stmt.executeUpdate();
        }

        String insertUrduQuery = "INSERT INTO urdu_meaning (id, urdu_meaning) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertUrduQuery)) {
            stmt.setInt(1, 12);
            stmt.setString(2, "سلام");
            stmt.executeUpdate();
        }

        String insertPersianQuery = "INSERT INTO persian_meaning (id, persian_meaning) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertPersianQuery)) {
            stmt.setInt(1, 12);
            stmt.setString(2, "سلام");
            stmt.executeUpdate();
        }
    }

    @Test
    public void testGetFavoriteWords_Success() throws SQLException {
        List<Word> favoriteWords = dao.getFavoriteWords();
        assertNotNull(favoriteWords);
        assertEquals(1, favoriteWords.size());
        Word favoriteWord = favoriteWords.get(0);
        assertEquals("سلام", favoriteWord.getArabicWord());
        assertEquals("سلام", favoriteWord.getUrduMeaning());
        assertEquals("سلام", favoriteWord.getPersianMeaning());
        assertTrue(favoriteWord.isFavorite());
    }

    @Test
    public void testGetFavoriteWords_EmptyResult() throws SQLException {
        String updateQuery = "UPDATE arabic_word SET isFavorite = 0 WHERE isFavorite = 1";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateQuery);
        }
        List<Word> favoriteWords = dao.getFavoriteWords();
        assertNotNull(favoriteWords);
        assertTrue(favoriteWords.isEmpty());
    }

    @Test
    public void testGetFavoriteWords_MultipleFavorites() throws SQLException {
        String updateQuery = "UPDATE arabic_word SET isFavorite = 1 WHERE id = 13";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateQuery);
        }
        List<Word> favoriteWords = dao.getFavoriteWords();
        assertNotNull(favoriteWords);
        assertEquals(2, favoriteWords.size());
    }

    @Test
    public void testGetFavoriteWords_NoMatchingMeanings() throws SQLException {
        String deleteUrduQuery = "DELETE FROM urdu_meaning WHERE id = 12";
        String deletePersianQuery = "DELETE FROM persian_meaning WHERE id = 12";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteUrduQuery);
            stmt.executeUpdate(deletePersianQuery);
        }
        List<Word> favoriteWords = dao.getFavoriteWords();
        assertNotNull(favoriteWords);
        assertEquals(1, favoriteWords.size());
        Word favoriteWord = favoriteWords.get(0);
        assertEquals("سلام", favoriteWord.getArabicWord());
        assertNull(favoriteWord.getUrduMeaning());
        assertNull(favoriteWord.getPersianMeaning());
        assertTrue(favoriteWord.isFavorite());
    }


    @AfterEach
    public void tearDown() throws SQLException {
        String deletePersianQuery = "DELETE FROM persian_meaning WHERE id = 12";
        String deleteUrduQuery = "DELETE FROM urdu_meaning WHERE id = 12";
        String deleteArabicQuery = "DELETE FROM arabic_word WHERE id IN (12, 13)";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deletePersianQuery);
            stmt.executeUpdate(deleteUrduQuery);
            stmt.executeUpdate(deleteArabicQuery);
        }
    }
}
