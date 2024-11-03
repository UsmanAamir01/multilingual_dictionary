package dal;

import java.util.LinkedList;
import java.util.List;

import dto.Word;

public interface IWordDAOFacade extends IWordDAO {
	@Override
	Word getWordFromDB(String arabicWord);

	@Override
	boolean updateWordToDB(Word w);

	@Override
	boolean addWordToDB(Word w);

	@Override
	boolean removeWordFromDB(String arabicWord);

	@Override
	List<Word> getAllWords();

	@Override
	Word viewOnceWord(String arabicWord);

	@Override
	List<Word> importDataFromFile(String filePath);

	@Override
	boolean insertImportedData(List<Word> words);

	@Override
	List<Word> searchWord(String searchTerm);

	@Override
	String getMeanings(String searchText, String language);

	@Override
	List<String> getTaggedAndStemmedWords();

	@Override
	LinkedList<?> getPOSTaggedWord(String arabicWord);

	@Override
	LinkedList<?> getStemmedWord(String arabicWord);
}