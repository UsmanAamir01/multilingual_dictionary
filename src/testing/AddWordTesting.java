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

public class AddWordTesting {

	private WordDAO dao;

	@BeforeEach
	public void setUp() throws Exception {
		dao = new WordDAO();

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionarydb", "root", "");
				Statement stmt = conn.createStatement()) {
		}
	}

	@Test
	public void testAddWordToDB_ValidWord() throws SQLException {
		// Test adding a valid word
		Word word = new Word("newWord", "نیا کلمہ", "کلمہ جدید");
		assertTrue(dao.addWordToDB(word), "The word should be added successfully.");
	}

	@Test
	public void testAddWordToDB_DuplicateWord() throws SQLException {
		// Insert the word first
		Word word = new Word("example", "نیا معنی", "جدید مثال");
		dao.addWordToDB(word);

		Word duplicateWord = new Word("example", "نیا معنی", "جدید مثال");
		assertFalse(dao.addWordToDB(duplicateWord), "Adding a duplicate word should return false.");
	}

	@Test
	public void testAddWordToDB_NullWord() {
		// Test adding a null word
		assertThrows(IllegalArgumentException.class, () -> dao.addWordToDB(null),
				"Adding a null word should throw IllegalArgumentException.");
	}
}