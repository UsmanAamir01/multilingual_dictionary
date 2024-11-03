package bl;

import dto.Word;
import java.util.List;


public class BLFacade implements IBLFacade {
	private IWordBO wordBO;
	private IUserBO userBO;

	public BLFacade() {
		this.wordBO = new WordBO();
		this.userBO = new UserBO();
	}

	@Override
	public boolean updateWord(Word w) {
		return wordBO.updateWord(w);
	}

	@Override
	public boolean addWord(Word w) {
		return wordBO.addWord(w);
	}

	@Override
	public boolean removeWord(String arabicWord) {
		return wordBO.removeWord(arabicWord);
	}

	@Override
	public String[][] getWordsWithMeanings() {
		return wordBO.getWordsWithMeanings();
	}

	@Override
	public Word viewOnceWord(String arabicWord) {
		return wordBO.viewOnceWord(arabicWord);
	}

	@Override
	public List<Word> importDataFromFile(String filePath) {
		return wordBO.importDataFromFile(filePath);
	}

	@Override
	public boolean insertImportedData(List<Word> words) {
		return wordBO.insertImportedData(words);
	}

	@Override
	public String searchWord(String searchTerm) {
		return wordBO.searchWord(searchTerm);
	}

	@Override
	public String getMeanings(String searchText, String language) {
		return wordBO.getMeanings(searchText, language);
	}

	@Override
	public List<String> getTaggedAndStemmedWords() {
		return wordBO.getTaggedAndStemmedWords();
	}

	@Override
	public String getPOSTaggedWord(String arabicWord) {
		return wordBO.getPOSTaggedWord(arabicWord);
	}

	@Override
	public String getStemmedWord(String arabicWord) {
		return wordBO.getStemmedWord(arabicWord);
	}

	@Override
	public String performPOSTagging(String arabicText) throws Exception {
		return wordBO.performPOSTagging(arabicText);
	}

	@Override
	public boolean validateUser(String username, String password) {
		return userBO.validateUser(username, password);
	}
}