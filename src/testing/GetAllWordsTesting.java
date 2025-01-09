package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class GetAllWordsTesting {
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

			stmt.setInt(1, 13);
			stmt.setString(2, "ہیلو");
			stmt.executeUpdate();
		}

		String insertPersianQuery = "INSERT INTO persian_meaning (id, persian_meaning) VALUES (?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(insertPersianQuery)) {
			stmt.setInt(1, 12);
			stmt.setString(2, "سلام");
			stmt.executeUpdate();

			stmt.setInt(1, 13);
			stmt.setString(2, "درود");
			stmt.executeUpdate();
		}
	}

	@Test
	public void testGetAllWords_Success() throws SQLException {
		List<Word> allWords = dao.getAllWords();
		assertNotNull(allWords);
		assertEquals(13, allWords.size());

		Word word1 = allWords.get(11);
		assertEquals("سلام", word1.getArabicWord());
		assertEquals("سلام", word1.getUrduMeaning());
		assertEquals("سلام", word1.getPersianMeaning());
		assertTrue(word1.isFavorite());

		Word word2 = allWords.get(12);
		assertEquals("مرحبا", word2.getArabicWord());
		assertEquals("ہیلو", word2.getUrduMeaning());
		assertEquals("درود", word2.getPersianMeaning());
		assertFalse(word2.isFavorite());
	}

	@Test
	public void testGetAllWords_MissingMeanings() throws SQLException {
		String deleteUrduQuery = "DELETE FROM urdu_meaning WHERE id = 12";
		String deletePersianQuery = "DELETE FROM persian_meaning WHERE id = 13";

		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(deleteUrduQuery);
			stmt.executeUpdate(deletePersianQuery);
		}

		List<Word> allWords = dao.getAllWords();
		assertNotNull(allWords);
		assertEquals(13, allWords.size());

		Word word1 = allWords.get(11);
		assertEquals("سلام", word1.getArabicWord());
		assertNull(word1.getUrduMeaning());
		assertEquals("سلام", word1.getPersianMeaning());

		Word word2 = allWords.get(12);
		assertEquals("مرحبا", word2.getArabicWord());
		assertEquals("ہیلو", word2.getUrduMeaning());
		assertNull(word2.getPersianMeaning());
	}

	@Test
	public void testGetAllWords_NoWords() throws SQLException {

		String deleteUrduQuery = "DELETE FROM urdu_meaning WHERE id IN (12, 13)";
		String deletePersianQuery = "DELETE FROM persian_meaning WHERE id IN (12, 13)";
		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(deleteUrduQuery);
			stmt.executeUpdate(deletePersianQuery);
		}

		String deleteArabicQuery = "DELETE FROM arabic_word WHERE id IN (12, 13)";
		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(deleteArabicQuery);
		}

		List<Word> allWords = dao.getAllWords();
		assertNotNull(allWords);
		assertTrue(allWords.size() > 0);
	}

	@AfterEach
	public void tearDown() throws SQLException {
		String deletePersianQuery = "DELETE FROM persian_meaning WHERE id IN (12, 13)";
		String deleteUrduQuery = "DELETE FROM urdu_meaning WHERE id IN (12, 13)";
		String deleteArabicQuery = "DELETE FROM arabic_word WHERE id IN (12, 13)";

		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(deletePersianQuery);
			stmt.executeUpdate(deleteUrduQuery);
			stmt.executeUpdate(deleteArabicQuery);
		}
	}
}
