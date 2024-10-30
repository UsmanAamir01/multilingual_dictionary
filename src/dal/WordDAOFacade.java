package dal;

import java.util.List;
import dto.Word;

public class WordDAOFacade implements IWordDAOFacade {
	private final IWordDAO wordDAO;

	public WordDAOFacade(IWordDAO wordDAO) {
		this.wordDAO = wordDAO;
	}

	public WordDAOFacade() {
		this.wordDAO = new WordDAO();
	}

	@Override
	public Word getWordFromDB(String arabicWord) {
		return wordDAO.getWordFromDB(arabicWord);
	}

	@Override
	public boolean updateWordToDB(Word w) {
		return wordDAO.updateWordToDB(w);
	}

	@Override
	public boolean addWordToDB(Word w) {
		return wordDAO.addWordToDB(w);
	}

	@Override
	public boolean removeWordFromDB(String arabicWord) {
		return wordDAO.removeWordFromDB(arabicWord);
	}

	@Override
	public List<Word> getAllWords() {
		return wordDAO.getAllWords();
	}

	@Override
	public Word viewOnceWord(String arabicWord) {
		return wordDAO.viewOnceWord(arabicWord);
	}

	@Override
	public List<Word> importDataFromFile(String filePath) {
		return wordDAO.importDataFromFile(filePath);
	}

	@Override
	public boolean insertImportedData(List<Word> words) {
		return wordDAO.insertImportedData(words);
	}
}
