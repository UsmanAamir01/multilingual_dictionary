package dal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dto.Word;

public class WordDAO implements IWordDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static final Logger LOGGER = Logger.getLogger(WordDAO.class.getName());

	private Connection connection;

	public WordDAO(Connection connection) {
		this.connection = connection;
	}

	public WordDAO() {
		try {
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to connect to the database", e);
		}
	}

	public Word getWordFromDB(String arabicWord) {
		String query = "SELECT * FROM dictionary WHERE arabic_word = ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, arabicWord);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String wordText = resultSet.getString("arabic_word");
				String urduMeaning = resultSet.getString("urdu_meaning");
				String persianMeaning = resultSet.getString("persian_meaning");
				return new Word(wordText, urduMeaning, persianMeaning);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return null;
	}

	public boolean updateWordToDB(Word w) {
		String query = "UPDATE dictionary SET urdu_meaning = ?, persian_meaning = ? WHERE arabic_word = ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, w.getUrduMeaning());
			statement.setString(2, w.getPersianMeaning());
			statement.setString(3, w.getArabicWord());
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return false;
	}

	public boolean addWordToDB(Word w) {
		String query = "INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES (?, ?, ?)";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, w.getArabicWord());
			statement.setString(2, w.getUrduMeaning());
			statement.setString(3, w.getPersianMeaning());
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return false;
	}

	public boolean removeWordFromDB(String arabicWord) {
		String query = "DELETE FROM dictionary WHERE arabic_word = ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, arabicWord);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return false;
	}

	public List<Word> getAllWords() {
		List<Word> wordList = new ArrayList<>();
		String query = "SELECT arabic_word, urdu_meaning, persian_meaning FROM dictionary";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String arabicWord = rs.getString("arabic_word");
				String urduMeaning = rs.getString("urdu_meaning");
				String persianMeaning = rs.getString("persian_meaning");
				wordList.add(new Word(arabicWord, urduMeaning, persianMeaning));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return wordList;
	}

	public Word viewOnceWord(String arabicWord) {
		String query = "SELECT arabic_word, urdu_meaning, persian_meaning FROM dictionary WHERE arabic_word = ?";
		Word word = null;

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, arabicWord);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String wordText = resultSet.getString("arabic_word");
				String urduMeaning = resultSet.getString("urdu_meaning");
				String persianMeaning = resultSet.getString("persian_meaning");
				word = new Word(wordText, urduMeaning, persianMeaning);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error in viewOnceWord", e);
		}
		return word;
	}

	public List<Word> importDataFromFile(String filePath) {
		List<Word> importedWords = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					importedWords.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim()));
				} else {
					LOGGER.log(Level.WARNING, "Invalid line format: {0}", line);
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading file", e);
		}
		return importedWords;
	}

	public boolean insertImportedData(List<Word> words) {
		for (Word word : words) {
			if (!addWordToDB(word)) {
				LOGGER.log(Level.SEVERE, "Failed to add word: {0}", word.getArabicWord());
				return false;
			}
		}
		return true;
	}

	public List<Word> searchWord(String searchTerm) {
		List<Word> results = new ArrayList<>();
		String query = "SELECT * FROM dictionary WHERE arabic_word LIKE ? OR urdu_meaning LIKE ? OR persian_meaning LIKE ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			String searchPattern = "%" + searchTerm + "%";
			statement.setString(1, searchPattern);
			statement.setString(2, searchPattern);
			statement.setString(3, searchPattern);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				results.add(new Word(resultSet.getString("arabic_word"), resultSet.getString("urdu_meaning"),
						resultSet.getString("persian_meaning")));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return results;
	}

	public String getMeanings(String word, String language) {
		StringBuilder meanings = new StringBuilder();
		String query;

		// Define query based on selected language
		if ("Urdu".equalsIgnoreCase(language)) {
			query = "SELECT arabic_word, persian_meaning FROM dictionary WHERE urdu_meaning = ?";
		} else if ("Persian".equalsIgnoreCase(language)) {
			query = "SELECT arabic_word, urdu_meaning FROM dictionary WHERE persian_meaning = ?";
		} else if ("Arabic".equalsIgnoreCase(language)) {
			query = "SELECT urdu_meaning, persian_meaning FROM dictionary WHERE arabic_word = ?";
		} else {
			return "Invalid language selection.";
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, word);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				if ("Urdu".equalsIgnoreCase(language)) {
					meanings.append("Arabic: ").append(resultSet.getString("arabic_word")).append("\n");
					meanings.append("Persian: ").append(resultSet.getString("persian_meaning")).append("\n");
				} else if ("Persian".equalsIgnoreCase(language)) {
					meanings.append("Arabic: ").append(resultSet.getString("arabic_word")).append("\n");
					meanings.append("Urdu: ").append(resultSet.getString("urdu_meaning")).append("\n");
				} else if ("Arabic".equalsIgnoreCase(language)) {
					meanings.append("Urdu: ").append(resultSet.getString("urdu_meaning")).append("\n");
					meanings.append("Persian: ").append(resultSet.getString("persian_meaning")).append("\n");
				}
			} else {
				meanings.append("Word not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			meanings.append("Error retrieving meanings.");
		}

		return meanings.toString();
	}
}