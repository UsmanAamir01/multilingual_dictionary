package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Word;

public class WordDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/your_database_name";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	public Word getWordFromDB(String word) {
		String query = "SELECT * FROM words WHERE word = ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, word);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				// Assuming 'words' table has columns 'word_text', 'language', and 'meaning'
				String word2 = resultSet.getString("word");
				String meaning = resultSet.getString("meaning");
				return new Word(word2, meaning);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean updateWordToDB(Word w) {

		String query = "UPDATE words SET meaning = ? WHERE word = ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, w.getMeaning());
			statement.setString(2, w.getWord());

			int rowsUpdated = statement.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean addWordToDB(Word w) {
		String query = "INSERT INTO words (word, meaning) VALUES (?, ?)";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, w.getWord());
			statement.setString(2, w.getMeaning());

			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeWordFromDB(String word) {
		String query = "DELETE FROM words WHERE word_text = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, word);
            
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
}