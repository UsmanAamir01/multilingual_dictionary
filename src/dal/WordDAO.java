package dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qcri.farasa.segmenter.Farasa;

import dto.Word;

public class WordDAO implements IWordDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static final Logger LOGGER = Logger.getLogger(WordDAO.class.getName());

	static {
		try {
			FileHandler fileHandler = new FileHandler("src/logs/dictionary-app-log.txt", true);
			fileHandler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.ALL);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error setting up file handler", e);
		}
	}

	private Connection connection;
	private Object posTaggerInstance;
	private Object stemmerInstance;

	public WordDAO(Connection connection) {
		this.connection = connection;
		try {
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error initializing Farasa segmenter", e);
		}
	}

	private Connection connect() {
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			LOGGER.log(Level.INFO, "Database connection established successfully.");
			return conn;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Database connection failed: {0}", e.getMessage());
			return null;
		}
	}

	public WordDAO() {
		try {
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			LOGGER.log(Level.INFO, "WordDAO initialized and database connection established.");
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to initialize WordDAO: {0}", e.getMessage());
		}
	}

	private Connection getConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			LOGGER.log(Level.WARNING, "Existing database connection is null or closed. Reestablishing connection...");
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			LOGGER.log(Level.INFO, "Database connection reestablished.");
		} else {
			LOGGER.log(Level.INFO, "Using existing database connection.");
		}
		return this.connection;
	}

	@Override
	public Word getWordFromDB(String arabicWord) {
		String query = "SELECT * FROM dictionary WHERE arabic_word = ?";
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			statement.setString(1, arabicWord);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String wordText = resultSet.getString("arabic_word");
				String urduMeaning = resultSet.getString("urdu_meaning");
				String persianMeaning = resultSet.getString("persian_meaning");
				return new Word(wordText, urduMeaning, persianMeaning, false);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return null;
	}

	@Override
	public String[] getMeaningsFromDB(String word) {
		String[] meanings = new String[2];
		String query = "SELECT persian_meaning, urdu_meaning FROM dictionary WHERE arabic_word = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, word);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					meanings[0] = rs.getString("urdu_meaning");
					meanings[1] = rs.getString("persian_meaning");
				}
				LOGGER.log(Level.INFO, "Successfully retrieved meanings for word: {0}", word);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error retrieving meanings for word {0}: {1}",
					new Object[] { word, e.getMessage() });
		}

		return meanings;
	}

	@Override
	public boolean updateWordToDB(Word w) {
		String query = "UPDATE dictionary SET urdu_meaning = ?, persian_meaning = ? WHERE arabic_word = ?";
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			statement.setString(1, w.getUrduMeaning());
			statement.setString(2, w.getPersianMeaning());
			statement.setString(3, w.getArabicWord());
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return false;
	}

	@Override
	public boolean addWordToDB(Word w) {
		String query = "INSERT INTO dictionary (arabic_word, urdu_meaning, persian_meaning) VALUES (?, ?, ?)";
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			statement.setString(1, w.getArabicWord());
			statement.setString(2, w.getUrduMeaning());
			statement.setString(3, w.getPersianMeaning());
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return false;
	}

	@Override
	public boolean removeWordFromDB(String arabicWord) {
		String query = "DELETE FROM dictionary WHERE arabic_word = ?";
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			statement.setString(1, arabicWord);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return false;
	}

	@Override
	public List<Word> getAllWords() {
		List<Word> wordList = new ArrayList<>();
		String query = "SELECT arabic_word, urdu_meaning, persian_meaning, isFavorite FROM dictionary";
		try (PreparedStatement stmt = getConnection().prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				wordList.add(new Word(rs.getString("arabic_word"), rs.getString("urdu_meaning"),
						rs.getString("persian_meaning"), rs.getBoolean("isFavorite")));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error while fetching all words.", e);
		}
		return wordList;
	}

	@Override
	public Word viewOnceWord(String arabicWord) {
		return getWordFromDB(arabicWord);
	}

	@Override
	public List<Word> importDataFromFile(String filePath) {
		List<Word> importedWords = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					importedWords.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim(), false));
				} else {
					LOGGER.log(Level.WARNING, "Invalid line format: {0}", line);
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading file", e);
		}
		return importedWords;
	}

	@Override
	public boolean insertImportedData(List<Word> words) {
		for (Word word : words) {
			if (!addWordToDB(word)) {
				LOGGER.log(Level.SEVERE, "Failed to add word: {0}", word.getArabicWord());
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Word> searchWord(String searchTerm) {
		List<Word> results = new ArrayList<>();
		String query = "SELECT * FROM dictionary WHERE arabic_word LIKE ? OR urdu_meaning LIKE ? OR persian_meaning LIKE ?";
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			String searchPattern = "%" + searchTerm + "%";
			statement.setString(1, searchPattern);
			statement.setString(2, searchPattern);
			statement.setString(3, searchPattern);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				results.add(new Word(resultSet.getString("arabic_word"), resultSet.getString("urdu_meaning"),
						resultSet.getString("persian_meaning"), false));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return results;
	}

	@Override
	public String getMeanings(String word, String language) {
		StringBuilder meanings = new StringBuilder();
		String query;
		if ("Urdu".equalsIgnoreCase(language)) {
			query = "SELECT arabic_word, persian_meaning FROM dictionary WHERE urdu_meaning = ?";
		} else if ("Persian".equalsIgnoreCase(language)) {
			query = "SELECT arabic_word, urdu_meaning FROM dictionary WHERE persian_meaning = ?";
		} else if ("Arabic".equalsIgnoreCase(language)) {
			query = "SELECT urdu_meaning, persian_meaning FROM dictionary WHERE arabic_word = ?";
		} else {
			return "Invalid language selection.";
		}
		try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
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
			LOGGER.log(Level.SEVERE, "Error retrieving meanings", e);
			meanings.append("Error retrieving meanings.");
		}
		return meanings.toString();
	}

	@Override
	public String fetchArabicWord() throws Exception {
		String arabicWord = null;
		String query = "SELECT arabic_word FROM dictionary";
		try (Connection connection = connect();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			if (resultSet.next()) {
				arabicWord = resultSet.getString("arabic_word");
			}
		}
		return arabicWord;
	}

	@Override
	public void saveResults(String word, String stem, String root, String pos) throws Exception {
		String insertQuery = "INSERT INTO processed_words (original_word, stem, root, pos) VALUES (?, ?, ?, ?)";
		try (Connection connection = connect();
				PreparedStatement statement = connection.prepareStatement(insertQuery)) {

			statement.setString(1, word);
			statement.setString(2, stem);
			statement.setString(3, root);
			statement.setString(4, pos);
			statement.executeUpdate();
		}
	}

	@Override
	public String[] scrapeWordAndUrduMeaning(String url) {
		try {
			Document doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0 Safari/537.36")
					.get();
			Element wordElement = doc.select("td[id^=w]").first();
			Element meaningElement = doc.select("td[id^=m]").first();
			if (wordElement != null && meaningElement != null) {
				String word = wordElement.text().replaceAll("\\s*\\[.*?\\]", "");
				String urduMeaning = meaningElement.text();
				return new String[] { word, urduMeaning };
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error scraping word and Urdu meaning.", e);
		}
		return null;
	}

	@Override
	public void saveWordAndUrduMeaning(String word, String urduMeaning) {
		String sql = "INSERT INTO dictionary (arabic_word, urdu_meaning) VALUES (?, ?)";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, word);
			pstmt.setString(2, urduMeaning);
			pstmt.executeUpdate();
			LOGGER.log(Level.INFO, "Record inserted successfully for word: " + word);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error saving word and Urdu meaning: " + e.getMessage(), e);
		}
	}

	@Override
	public String scrapeFarsiMeaning(String url) {
		try {
			Document doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.64")
					.get();

			Elements farsiMeaningElements = doc.select("td[id^=m]");

			if (farsiMeaningElements.size() > 1) {
				Element farsiMeaningElement = farsiMeaningElements.get(1);
				if (farsiMeaningElement != null) {
					LOGGER.log(Level.INFO, "Successfully scraped Farsi meaning from URL: {0}", url);
					return farsiMeaningElement.text();
				}
			} else {
				LOGGER.log(Level.WARNING, "Farsi meaning not found (not enough elements) in URL: {0}", url);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error scraping Farsi meaning from URL {0}: {1}",
					new Object[] { url, e.getMessage() });
		}
		return null;
	}

	@Override
	public void updateFarsiMeaning(String word, String farsiMeaning) {
		String sql = "UPDATE dictionary SET persian_meaning = ? WHERE arabic_word = ?";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, farsiMeaning);
			pstmt.setString(2, word);
			pstmt.executeUpdate();
			LOGGER.log(Level.INFO, "Farsi Meaning updated successfully for word: {0}", word);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error updating Farsi meaning for word {0}: {1}",
					new Object[] { word, e.getMessage() });
		}
	}

	@Override
	public String getFarsiMeaning(String word) {
		String sql = "SELECT persian_meaning FROM dictionary WHERE arabic_word = ?";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, word);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String meaning = rs.getString("persian_meaning");
				LOGGER.log(Level.INFO, "Successfully retrieved Farsi meaning for word: {0}", word);
				return meaning;
			} else {
				LOGGER.log(Level.WARNING, "No Farsi meaning found for word: {0}", word);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error retrieving Farsi meaning for word {0}: {1}",
					new Object[] { word, e.getMessage() });
		}
		return null;
	}

	@Override
	public void markAsFavorite(String arabicWord, boolean isFavorite) {
		String query = "UPDATE dictionary SET isFavorite = ? WHERE arabic_word = ?";
		try (PreparedStatement statement = getConnection().prepareStatement(query)) {
			statement.setBoolean(1, isFavorite);
			statement.setString(2, arabicWord);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error updating favorite status.", e);
		}
	}

	@Override
	public List<Word> getFavoriteWords() {
		List<Word> favoriteWords = new ArrayList<>();
		String query = "SELECT * FROM dictionary WHERE isFavorite = TRUE";
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				favoriteWords.add(new Word(rs.getString("arabic_word"), rs.getString("urdu_meaning"),
						rs.getString("persian_meaning"), rs.getBoolean("isFavorite")));
			}
			LOGGER.log(Level.INFO, "Fetched favorite words successfully.");
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error fetching favorite words: {0}", e.getMessage());
		}
		return favoriteWords;
	}

	@Override
	public boolean isWordFavorite(String arabicWord) {
		String query = "SELECT isFavorite FROM dictionary WHERE arabic_word = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, arabicWord);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				boolean isFavorite = resultSet.getBoolean("isFavorite");
				LOGGER.log(Level.INFO, "Checked favorite status for word: {0} - isFavorite: {1}",
						new Object[] { arabicWord, isFavorite });
				return isFavorite;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error checking favorite status for word {0}: {1}",
					new Object[] { arabicWord, e.getMessage() });
		}
		return false;
	}

	@Override
	public void addSearchToHistory(Word word) {
		String query = "INSERT INTO searchhistory (arabic_word, persian_meaning, urdu_meaning) VALUES (?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, word.getArabicWord() != null ? word.getArabicWord() : null);
			stmt.setString(2, word.getPersianMeaning() != null ? word.getPersianMeaning() : null);
			stmt.setString(3, word.getUrduMeaning() != null ? word.getUrduMeaning() : null);

			stmt.executeUpdate();
			LOGGER.log(Level.INFO, "Added search to history: {0}", word);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error adding search to history for word {0}: {1}",
					new Object[] { word, e.getMessage() });
		}
	}

	@Override
	public List<Word> getRecentSearchHistory(int limit) {
		List<Word> history = new ArrayList<>();
		String query = "SELECT arabic_word, persian_meaning, urdu_meaning FROM searchhistory ORDER BY id DESC LIMIT ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, limit);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Word word = new Word(rs.getString("arabic_word"), rs.getString("persian_meaning"),
							rs.getString("urdu_meaning"));
					history.add(word);
				}
			}
			LOGGER.log(Level.INFO, "Retrieved recent search history with limit: {0}", limit);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error retrieving search history with limit {0}: {1}",
					new Object[] { limit, e.getMessage() });
		}
		return history;
	}

	@Override
	public List<String> segmentWordWithDiacritics(String word) {
		Map<Integer, Character> diacriticalMarks = new HashMap<>();
		StringBuilder strippedWord = new StringBuilder();

		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (isDiacriticalMark(c)) {
				diacriticalMarks.put(strippedWord.length() - 1, c);
			} else {
				strippedWord.append(c);
			}
		}

		try {
			Farasa farasaSegmenter = new Farasa();
			ArrayList<String> segmentedWords = farasaSegmenter.segmentLine(strippedWord.toString());
			ArrayList<String> segmentedWithDiacritics = reapplyDiacritics(segmentedWords, diacriticalMarks);
			List<String> result = splitByCommasAndPlus(segmentedWithDiacritics);
			LOGGER.log(Level.INFO, "Successfully segmented word with diacritics: {0}", word);
			return result;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error during segmentation for word {0}: {1}",
					new Object[] { word, e.getMessage() });
			return null;
		}
	}

	private boolean isDiacriticalMark(char c) {
		return (c >= 0x064B && c <= 0x0652);
	}

	private ArrayList<String> reapplyDiacritics(ArrayList<String> segments, Map<Integer, Character> diacriticalMarks) {
		ArrayList<String> result = new ArrayList<>();
		StringBuilder currentSegment = new StringBuilder();
		int positionInOriginal = 0;

		for (String segment : segments) {
			currentSegment.setLength(0);
			for (int i = 0; i < segment.length(); i++) {
				char c = segment.charAt(i);
				currentSegment.append(c);
				if (diacriticalMarks.containsKey(positionInOriginal)) {
					currentSegment.append(diacriticalMarks.get(positionInOriginal));
				}
				positionInOriginal++;
			}
			result.add(currentSegment.toString());
		}

		LOGGER.log(Level.FINE, "Reapplied diacritics to segments successfully.");
		return result;
	}

	private List<String> splitByCommasAndPlus(List<String> segmentedWords) {
		List<String> result = new ArrayList<>();
		for (String segment : segmentedWords) {
			String[] parts = segment.split("[,+]");
			result.addAll(Arrays.asList(parts));
		}
		LOGGER.log(Level.FINE, "Split segmented words by commas and plus signs successfully.");
		return result;
	}

	@Override
	public List<String> getRecentSearchSuggestions() {
		List<String> recentSuggestions = new ArrayList<>();
		String query = "SELECT arabic_word FROM searchhistory ORDER BY search_time DESC LIMIT 5";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				recentSuggestions.add(rs.getString("arabic_word"));
			}
			LOGGER.log(Level.INFO, "Successfully retrieved recent search suggestions.");
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error retrieving recent search suggestions: {0}", e.getMessage());
		}
		return recentSuggestions;
	}
}