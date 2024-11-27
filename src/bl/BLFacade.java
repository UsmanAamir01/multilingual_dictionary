package bl;

import java.util.LinkedList;
import java.util.List;

import dto.Word;

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
	public boolean validateUser(String username, String password) {
		return userBO.validateUser(username, password);
	}

	@Override
	public String[] saveWordAndUrduMeaning(String filePath) {
		return wordBO.saveWordAndUrduMeaning(filePath);
	}

	@Override
	public void saveFarsiMeaning(String word, String filePath) {
		wordBO.saveFarsiMeaning(word, filePath);
	}

	@Override
	public String getFarsiMeaning(String word) {
		return wordBO.getFarsiMeaning(word);
	}

	@Override
	public void markWordAsFavorite(String word, boolean isFavorite) {
		wordBO.markWordAsFavorite(word, isFavorite);
	}

	@Override
	public List<Word> getFavoriteWords() {
		return wordBO.getFavoriteWords();
	}

	@Override
	public boolean isWordFavorite(String arabicWord) {
		return wordBO.isWordFavorite(arabicWord);
	}

	@Override
	public void addSearchToHistory(Word word) {
		wordBO.addSearchToHistory(word);
	}

	@Override
	public List<Word> getRecentSearchHistory(int limit) {
		return wordBO.getRecentSearchHistory(limit);
	}

	@Override
	public List<String> getAllLemmaztizedWords() {
		return wordBO.getAllLemmaztizedWords();
	}

	@Override
	public boolean insertLemmatizedWord(String originalWord, String lemmatizedWord) {
		return wordBO.insertLemmatizedWord(originalWord, lemmatizedWord);
	}

	@Override
	public String getLemmatizedWord(String originalWord) {
		return wordBO.getLemmatizedWord(originalWord);
	}

	public List<String> getSegmentedWordsWithDiacritics(String word) {
		return wordBO.getSegmentedWordsWithDiacritics(word);
	}

	@Override
	public String processWord(String arabicText) throws Exception {
		return wordBO.processWord(arabicText);
	}

	@Override
	public void saveResults(String word, String stem, String root, String pos) throws Exception {
		 wordBO.saveResults(word, stem, root, pos);
		
	}

}