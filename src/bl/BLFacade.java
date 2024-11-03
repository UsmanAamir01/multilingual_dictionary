package bl;

import dto.Word;

import java.util.LinkedList;
import java.util.List;

public class BLFacade implements IBLFacade {
	private IWordBO wordBO;
	private IUserBO userBO;

	public BLFacade(WordBO wordBO, UserBO userBO) {
		this.wordBO = wordBO;
		this.userBO = userBO;
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
	public  LinkedList<?> getPOSTaggedWord(String arabicWord) {
		return wordBO.getPOSTaggedWord(arabicWord);
	}

	@Override
	public LinkedList<?> getStemmedWord(String arabicWord) {
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

	@Override
	public String getFarsiMeaning(String word) {
		return wordBO.getFarsiMeaning(word);
	}

	@Override
	public void updateFarsiMeaning(String word, String farsiMeaning) {
		wordBO.updateFarsiMeaning(word, farsiMeaning);
		
	}

	@Override
	public String scrapeFarsiMeaning(String filePath) {
		return wordBO.scrapeFarsiMeaning(filePath);
	}

	@Override
	public void saveWordAndUrduMeaning(String word, String urduMeaning) {
		wordBO.saveWordAndUrduMeaning(word, urduMeaning);
		
	}

	@Override
	public String[] scrapeWordAndUrduMeaning(String filePath) {
		return wordBO.scrapeWordAndUrduMeaning(filePath);
	}

}