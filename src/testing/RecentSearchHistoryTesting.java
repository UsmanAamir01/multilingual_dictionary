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

public class RecentSearchHistoryTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();

        // Setup database connection for the test
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {

            // Clean up the searchhistory table before the test
            stmt.execute("DELETE FROM searchhistory");

            // Insert test data into the searchhistory table
            stmt.execute("INSERT INTO searchhistory (arabic_word, persian_meaning, urdu_meaning) VALUES ('example1', 'معنی1', 'مفہوم1')");
            stmt.execute("INSERT INTO searchhistory (arabic_word, persian_meaning, urdu_meaning) VALUES ('example2', 'معنی2', 'مفہوم2')");
            stmt.execute("INSERT INTO searchhistory (arabic_word, persian_meaning, urdu_meaning) VALUES ('example3', 'معنی3', 'مفہوم3')");
            stmt.execute("INSERT INTO searchhistory (arabic_word, persian_meaning, urdu_meaning) VALUES ('example4', 'معنی4', 'مفہوم4')");
        }
    }

    @Test
    public void testGetRecentSearchHistory_Limit() {
        // Retrieve the most recent 3 search history records
        List<Word> recentHistory = dao.getRecentSearchHistory(3);

        // Verify that the result contains exactly 3 records
        assertEquals(3, recentHistory.size(), "The recent search history should contain 3 records.");

        // Verify that the most recent record is "example4" and the least recent is "example2"
        assertEquals("example4", recentHistory.get(0).getArabicWord(), "First record should be example4.");
        assertEquals("example3", recentHistory.get(1).getArabicWord(), "Second record should be example3.");
        assertEquals("example2", recentHistory.get(2).getArabicWord(), "Third record should be example2.");
    }

    @Test
    public void testGetRecentSearchHistory_LimitExceedsRecords() {
        // Retrieve the most recent 10 search history records, but we only have 4 in the database
        List<Word> recentHistory = dao.getRecentSearchHistory(10);

        // Verify that the result contains exactly 4 records
        assertEquals(4, recentHistory.size(), "The recent search history should contain 4 records.");

        // Verify that the records match the entries in the expected order
        assertEquals("example4", recentHistory.get(0).getArabicWord(), "First record should be example4.");
        assertEquals("example3", recentHistory.get(1).getArabicWord(), "Second record should be example3.");
        assertEquals("example2", recentHistory.get(2).getArabicWord(), "Third record should be example2.");
        assertEquals("example1", recentHistory.get(3).getArabicWord(), "Fourth record should be example1.");
    }

    @Test
    public void testGetRecentSearchHistory_EmptyHistory() throws SQLException {
        // Clear the search history table
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM searchhistory");
        }

        // Retrieve the most recent 3 search history records when the table is empty
        List<Word> recentHistory = dao.getRecentSearchHistory(3);

        // Verify that the result is empty
        assertTrue(recentHistory.isEmpty(), "The recent search history should be empty.");
    }
}
