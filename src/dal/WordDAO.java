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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import dto.Word;

public class WordDAO implements IWordDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/a";
	private static final String USER = "ranko";
	private static final String PASSWORD = "huzaifa123";
	private static final Logger LOGGER = Logger.getLogger(WordDAO.class.getName());
	private Connection connection;
	private Object posTaggerInstance;
	private Object stemmerInstance;

	public WordDAO(Connection connection) {
		this.connection = connection;
	}
	private Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
	public WordDAO() {
		try {
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to connect to the database", e);
		}
	}

	private Connection getConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
				return new Word(wordText, urduMeaning, persianMeaning);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return null;
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
		String query = "SELECT arabic_word, urdu_meaning, persian_meaning FROM dictionary";
		try (PreparedStatement stmt = getConnection().prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
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
						resultSet.getString("persian_meaning")));
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

	public List<String> getTaggedAndStemmedWords() {
		List<String> results = new ArrayList<>();
		String query = "SELECT arabic_word FROM dictionary";
		try (PreparedStatement statement = getConnection().prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				String arabicWord = resultSet.getString("arabic_word");
				LinkedList<?> stemmedWord = getStemmedWord(arabicWord);
				LinkedList<?> taggedWord = getPOSTaggedWord(arabicWord);
				results.add("Original: " + arabicWord + " | Stemmed: " + stemmedWord + " | Tagged: " + taggedWord);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Database error", e);
		}
		return results;

	}

	@Override
	public LinkedList<?> getPOSTaggedWord(String arabicWord) {
		try {
			Method tagMethod = posTaggerInstance.getClass().getMethod("analyzedWords", String.class);
			LinkedList<?> posTaggedResult = (LinkedList<?>) tagMethod.invoke(posTaggerInstance, arabicWord);
			return posTaggedResult;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "POS tagging error", e);
			return null;
		}
	}

	@Override
	public LinkedList<?> getStemmedWord(String arabicWord) {
		try {
			Method stemMethod = stemmerInstance.getClass().getMethod("stemWords", String.class);
			LinkedList<?> stemmedResult = (LinkedList<?>) stemMethod.invoke(stemmerInstance, arabicWord);
			return stemmedResult;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Stemming error", e);
			return null;
		}
	}
//	public String[] scrapeWordAndUrduMeaning(String filePath) {
//        try {
//            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
//            Element wordElement = doc.select("td[id^=w]").first();
//            Element meaningElement = doc.select("td[id^=m]").first();
//            if (wordElement != null && meaningElement != null) {
//                String word = wordElement.text().replaceAll("\\s*\\[.*?\\]", "");
//                String urduMeaning = meaningElement.text();
//                return new String[]{word, urduMeaning};
//            }
//        } catch (Exception e) {
//            System.err.println("Error scraping word and Urdu meaning: " + e.getMessage());
//        }
//        return null;
//    }
//	public void saveWordAndUrduMeaning(String word, String urduMeaning) {
//        String sql = "INSERT INTO dictionary (word, mean1) VALUES (?, ?)";
//        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, word);
//            pstmt.setString(2, urduMeaning);
//            pstmt.executeUpdate();
//        } catch (Exception e) {
//            System.err.println("Error saving word and Urdu meaning: " + e.getMessage());
//        }
//    }
//	public String scrapeFarsiMeaning(String filePath) {
//        try {
//            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
//            Element farsiMeaningElement = doc.select("td[id^=m]").get(1); // Second meaning
//            return farsiMeaningElement != null ? farsiMeaningElement.text() : null;
//        } catch (Exception e) {
//            System.err.println("Error scraping Farsi meaning: " + e.getMessage());
//        }
//        return null;
//    }
//	public void updateFarsiMeaning(String word, String farsiMeaning) {
//        String sql = "UPDATE dictionary SET mean2 = ? WHERE word = ?";
//        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, farsiMeaning);
//            pstmt.setString(2, word);
//            pstmt.executeUpdate();
//        } catch (Exception e) {
//            System.err.println("Error updating Farsi meaning: " + e.getMessage());
//        }
//    }
//	 public String getFarsiMeaning(String word) {
//	        String sql = "SELECT mean2 FROM dictionary WHERE word = ?";
//	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//	            pstmt.setString(1, word);
//	            ResultSet rs = pstmt.executeQuery();
//	            if (rs.next()) {
//	                return rs.getString("mean2");
//	            }
//	        } catch (Exception e) {
//	            System.err.println("Error retrieving Farsi meaning: " + e.getMessage());
//	        }
//	        return null;
//	    }
//	
//	 
//	 
//	 
//	 
//	 
//	 
//
//	    private Connection connect() {
//	        try {
//	            return DriverManager.getConnection(url, user, password);
//	        } catch (Exception e) {
//	            System.err.println("Database connection failed: " + e.getMessage());
//	            return null;
//	        }
//	    }

	    // Scrape Word and Urdu Meaning from HTML file
//	    public String[] scrapeWordAndUrduMeaning(String filePath) {
//	        try {
//	            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
//	            Element wordElement = doc.select("td[id^=w]").first();
//	            Element meaningElement = doc.select("td[id^=m]").first();
//
//	            if (wordElement != null && meaningElement != null) {
//	                String word = wordElement.text().replaceAll("\\s*\\[.*?\\]", "");
//	                String urduMeaning = meaningElement.text();
//	                return new String[]{word, urduMeaning};
//	            }
//	        } catch (Exception e) {
//	            System.err.println("Error scraping word and Urdu meaning: " + e.getMessage());
//	        }
//	        return null;
//	    }
//
//	    // Save Word and Urdu Meaning into Database
//	    public void saveWordAndUrduMeaning(String word, String urduMeaning) {
//	        String sql = "INSERT INTO dictionary (word, mean1) VALUES (?, ?)";
//	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//	            pstmt.setString(1, word);
//	            pstmt.setString(2, urduMeaning);
//	            pstmt.executeUpdate();
//	            System.out.println("Record inserted successfully for word: " + word);
//	        } catch (Exception e) {
//	            System.err.println("Error saving word and Urdu meaning: " + e.getMessage());
//	        }
//	    }
//
//	    // Scrape Farsi Meaning from HTML file
//	    public String scrapeFarsiMeaning(String filePath) {
//	        try {
//	            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
//	            Element farsiMeaningElement = doc.select("td[id^=m]").get(1); // Get second meaning (Farsi)
//	            if (farsiMeaningElement != null) {
//	                return farsiMeaningElement.text();
//	            }
//	        } catch (Exception e) {
//	            System.err.println("Error scraping Farsi meaning: " + e.getMessage());
//	        }
//	        return null;
//	    }
//
//	    // Update Farsi Meaning in Database (mean2)
//	    public void updateFarsiMeaning(String word, String farsiMeaning) {
//	        String sql = "UPDATE dictionary SET mean2 = ? WHERE word = ?";
//	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//	            pstmt.setString(1, farsiMeaning);
//	            pstmt.setString(2, word);
//	            pstmt.executeUpdate();
//	            System.out.println("Farsi Meaning updated successfully for word: " + word);
//	        } catch (Exception e) {
//	            System.err.println("Error updating Farsi meaning: " + e.getMessage());
//	        }
//	    }
//
//	    // Get Farsi Meaning from Database
//	    public String getFarsiMeaning(String word) {
//	        String sql = "SELECT mean2 FROM dictionary WHERE word = ?";
//	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//	            pstmt.setString(1, word);
//	            ResultSet rs = pstmt.executeQuery();
//	            if (rs.next()) {
//	                return rs.getString("mean2");
//	            }
//	        } catch (Exception e) {
//	            System.err.println("Error retrieving Farsi meaning: " + e.getMessage());
//	        }
//	        return null;
//	    }
	 public String[] scrapeWordAndUrduMeaning(String filePath) {
	        try {
	            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
	            Element wordElement = doc.select("td[id^=w]").first();
	            Element meaningElement = doc.select("td[id^=m]").first();

	            if (wordElement != null && meaningElement != null) {
	                String word = wordElement.text().replaceAll("\\s*\\[.*?\\]", "");
	                String urduMeaning = meaningElement.text();
	                return new String[]{word, urduMeaning};
	            }
	        } catch (Exception e) {
	            System.err.println("Error scraping word and Urdu meaning: " + e.getMessage());
	        }
	        return null;
	    }

	    // Save Word and Urdu Meaning into Database
	    public void saveWordAndUrduMeaning(String word, String urduMeaning) {
	        String sql = "INSERT INTO dictionary (arabic_word, urdu_meaning) VALUES (?, ?)";
	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, word);
	            pstmt.setString(2, urduMeaning);
	            pstmt.executeUpdate();
	            System.out.println("Record inserted successfully for word: " + word);
	        } catch (Exception e) {
	            System.err.println("Error saving word and Urdu meaning: " + e.getMessage());
	        }
	    }

	    // Scrape Farsi Meaning from HTML file
	    public String scrapeFarsiMeaning(String filePath) {
	        try {
	            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
	            Element farsiMeaningElement = doc.select("td[id^=m]").get(1); // Get second meaning (Farsi)
	            if (farsiMeaningElement != null) {
	                return farsiMeaningElement.text();
	            }
	        } catch (Exception e) {
	            System.err.println("Error scraping Farsi meaning: " + e.getMessage());
	        }
	        return null;
	    }

	    // Update Farsi Meaning in Database
	    public void updateFarsiMeaning(String word, String farsiMeaning) {
	        String sql = "UPDATE dictionary SET persian_meaning = ? WHERE arabic_word = ?";
	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, farsiMeaning);
	            pstmt.setString(2, word);
	            pstmt.executeUpdate();
	            System.out.println("Farsi Meaning updated successfully for word: " + word);
	        } catch (Exception e) {
	            System.err.println("Error updating Farsi meaning: " + e.getMessage());
	        }
	    }

	    // Get Farsi Meaning from Database
	    public String getFarsiMeaning(String word) {
	        String sql = "SELECT persian_meaning FROM dictionary WHERE arabic_word = ?";
	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, word);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                return rs.getString("persian_meaning");
	            }
	        } catch (Exception e) {
	            System.err.println("Error retrieving Farsi meaning: " + e.getMessage());
	        }
	        return null;
	    }
}
