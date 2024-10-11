package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Word;

public class WordDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	public Word getWordFromDB(String word) {
		String query = "SELECT * FROM dictionary WHERE word = ?";
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, word);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				
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

		String query = "UPDATE dictionary SET meaning = ? WHERE word = ?";
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
		String query = "INSERT INTO dictionary (word, meaning) VALUES (?, ?)";
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
		String query = "DELETE FROM dictionary WHERE word = ?";
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
	
	public List<Word> getAllWords() {
        String query = "SELECT word, meaning FROM dictionary";
        List<Word> wordList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String word = rs.getString("word");
                String meaning = rs.getString("meaning");
                wordList.add(new Word(word, meaning));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wordList;
    }
	
}