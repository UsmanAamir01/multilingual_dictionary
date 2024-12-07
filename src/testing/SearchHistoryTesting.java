package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class SearchHistoryTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();

        // Setup database connection for the test
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            // Clean up the searchhistory table before the test
            stmt.execute("DELETE FROM searchhistory");
        }
    }

    @Test
    public void testAddSearchToHistory() {
        // Creating a word to be added to search history
        Word word = new Word("example", "نمونہ", "مثال", false);

        // Add the word to search history
        dao.addSearchToHistory(word);

        // Verify the word is inserted in the searchhistory table
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            // Query the searchhistory table to check if the word is added
            String query = "SELECT * FROM searchhistory WHERE arabic_word = 'example'";
            ResultSet resultSet = stmt.executeQuery(query);

            assertTrue(resultSet.next(), "Search history should contain the word.");
            assertEquals("example", resultSet.getString("arabic_word"), "Arabic word should match.");
            assertEquals("مثال", resultSet.getString("persian_meaning"), "Persian meaning should match.");
            assertEquals("نمونہ", resultSet.getString("urdu_meaning"), "Urdu meaning should match.");
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddSearchToHistory_NullValues() {
        // Creating a word with null meanings
        Word word = new Word("test", null, null, false);

        // Add the word to search history
        dao.addSearchToHistory(word);

        // Verify the word is inserted in the searchhistory table with null values
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            // Query the searchhistory table to check if the word is added
            String query = "SELECT * FROM searchhistory WHERE arabic_word = 'test'";
            ResultSet resultSet = stmt.executeQuery(query);

            assertTrue(resultSet.next(), "Search history should contain the word.");
            assertEquals("test", resultSet.getString("arabic_word"), "Arabic word should match.");
            assertNull(resultSet.getString("persian_meaning"), "Persian meaning should be null.");
            assertNull(resultSet.getString("urdu_meaning"), "Urdu meaning should be null.");
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddSearchToHistory_EmptyWord() {
        // Creating an empty word
        Word word = new Word("", "", "", false);

        // Add the empty word to search history
        dao.addSearchToHistory(word);

        // Verify the word is inserted in the searchhistory table
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            // Query the searchhistory table to check if the empty word is added
            String query = "SELECT * FROM searchhistory WHERE arabic_word = ''";
            ResultSet resultSet = stmt.executeQuery(query);

            assertTrue(resultSet.next(), "Search history should contain the empty word.");
            assertEquals("", resultSet.getString("arabic_word"), "Arabic word should be empty.");
            assertEquals("", resultSet.getString("persian_meaning"), "Persian meaning should be empty.");
            assertEquals("", resultSet.getString("urdu_meaning"), "Urdu meaning should be empty.");
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }
}
