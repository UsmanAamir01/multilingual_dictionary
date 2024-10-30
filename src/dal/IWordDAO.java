package dal;

import dto.Word;
import java.util.List;

public interface IWordDAO {
	Word getWordFromDB(String arabicWord);

	boolean updateWordToDB(Word w);

	boolean addWordToDB(Word w);

	boolean removeWordFromDB(String arabicWord);

	List<Word> getAllWords();

	Word viewOnceWord(String arabicWord);

	List<Word> importDataFromFile(String filePath);

	boolean insertImportedData(List<Word> words);

	List<Word> searchWord(String searchTerm);
}