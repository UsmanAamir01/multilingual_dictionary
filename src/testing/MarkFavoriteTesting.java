package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;

public class MarkFavoriteTesting {
    private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private WordDAO dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        dao = new WordDAO(connection);

        String arabicWord = "سلام";
        String checkQuery = "SELECT COUNT(*) FROM arabic_word WHERE arabic_word = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, arabicWord);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("INSERT INTO arabic_word (arabic_word, isFavorite) VALUES ('سلام', false);");
                }
            }
        }
    }

    @Test
    public void testMarkAsFavorite_Success() throws SQLException {
        String arabicWord = "سلام";
        boolean isFavorite = true;

        dao.markAsFavorite(arabicWord, isFavorite);

        String query = "SELECT isFavorite FROM arabic_word WHERE arabic_word = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, arabicWord);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                boolean result = resultSet.getBoolean("isFavorite");
                assertTrue(result);
            } else {
                fail("The word was not found in the database.");
            }
        }
    }

    @Test
    public void testMarkAsFavorite_Duplicate() throws SQLException {
        String arabicWord = "سلام";
        boolean isFavorite = true;

        dao.markAsFavorite(arabicWord, isFavorite);
        dao.markAsFavorite(arabicWord, isFavorite);

        String query = "SELECT isFavorite FROM arabic_word WHERE arabic_word = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, arabicWord);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                boolean result = resultSet.getBoolean("isFavorite");
                assertTrue(result);
            } else {
                fail("The word was not found in the database.");
            }
        }
    }

    @Test
    public void testMarkAsFavorite_NullWord() {
        assertThrows(NullPointerException.class, () -> dao.markAsFavorite(null, true));
    }

   

    @AfterEach
    public void tearDown() throws SQLException {
        String deleteQuery = "DELETE FROM arabic_word WHERE arabic_word = 'سلام'";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.executeUpdate();
        }
    }
}
