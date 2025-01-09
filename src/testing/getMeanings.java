package testing;

import dal.WordDAO;
import dto.Word;
import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class getMeanings {

    private WordDAO wordDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up an in-memory H2 database
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;", "sa", "");
        wordDAO = new WordDAO(connection);  // Assuming your DAO accepts a Connection

        // Set up the database schema and sample data
        try (Statement stmt = connection.createStatement()) {
            // Create tables
            stmt.execute("CREATE TABLE arabic_word (id INT PRIMARY KEY, arabic_word VARCHAR(255))");
            stmt.execute("CREATE TABLE urdu_meaning (id INT PRIMARY KEY, urdu_meaning VARCHAR(255))");
            stmt.execute("CREATE TABLE persian_meaning (id INT PRIMARY KEY, persian_meaning VARCHAR(255))");

            // Insert sample data
            stmt.execute("INSERT INTO arabic_word (id, arabic_word) VALUES (1, 'کتاب')");
            stmt.execute("INSERT INTO urdu_meaning (id, urdu_meaning) VALUES (1, 'کتاب کا مطلب')");
            stmt.execute("INSERT INTO persian_meaning (id, persian_meaning) VALUES (1, 'کتاب کا معنی')");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Close the connection and clean up
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS arabic_word");
            stmt.execute("DROP TABLE IF EXISTS urdu_meaning");
            stmt.execute("DROP TABLE IF EXISTS persian_meaning");
        }
        connection.close();
    }

    @Test
    public void testGetMeanings_UrduLanguage() throws SQLException {
        // Test retrieving meanings for Urdu language
        String word = "کتاب";
        String language = "Urdu";

        String result = wordDAO.getMeanings(word, language);

        // Assert the expected output
        assertEquals("Arabic: کتاب\nPersian: کتاب کا معنی\n", result);
    }

    @Test
    public void testGetMeanings_PersianLanguage() throws SQLException {
        // Test retrieving meanings for Persian language
        String word = "کتاب";
        String language = "Persian";

        String result = wordDAO.getMeanings(word, language);

        // Assert the expected output
        assertEquals("Arabic: کتاب\nUrdu: کتاب کا مطلب\n", result);
    }

    @Test
    public void testGetMeanings_ArabicLanguage() throws SQLException {
        // Test retrieving meanings for Arabic language
        String word = "کتاب";
        String language = "Arabic";

        String result = wordDAO.getMeanings(word, language);

        // Assert the expected output
        assertEquals("Urdu: کتاب کا مطلب\nPersian: کتاب کا معنی\n", result);
    }

    @Test
    public void testGetMeanings_InvalidLanguage() throws SQLException {
        // Test invalid language selection
        String word = "کتاب";
        String language = "French";  // Invalid language

        String result = wordDAO.getMeanings(word, language);

        // Assert that the method returns an error for invalid language
        assertEquals("Invalid language selection.", result);
    }

    @Test
    public void testGetMeanings_NoResultFound() throws SQLException {
        // Test case where the word is not found in the database
        String word = "غیر موجود";
        String language = "Urdu";

        String result = wordDAO.getMeanings(word, language);

        // Assert that the method indicates the word was not found
        assertEquals("Word not found.", result);
    }

    @Test
    public void testGetMeanings_SQLException() throws SQLException {
        // Test case where there is a database error (you can simulate this by closing the connection)
        wordDAO = new WordDAO(null);  // Pass null to simulate a failed connection

        String word = "کتاب";
        String language = "Urdu";

        String result = wordDAO.getMeanings(word, language);

        // Assert that the method returns an error message
        assertEquals("Error retrieving meanings.", result);
    }
}
