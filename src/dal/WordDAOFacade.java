package dal;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public List<Word> searchWord(String searchTerm) {
		return wordDAO.getAllWords().stream()
				.filter(word -> word.getArabicWord().equalsIgnoreCase(searchTerm)
						|| word.getUrduMeaning().equalsIgnoreCase(searchTerm)
						|| word.getPersianMeaning().equalsIgnoreCase(searchTerm))
				.collect(Collectors.toList());
	}

	@Override
	public String getMeanings(String searchText, String language) {
		return wordDAO.getMeanings(searchText, language);
	}

	@Override
	public List<String> getTaggedAndStemmedWords() {
		return wordDAO.getTaggedAndStemmedWords();
	}

	@Override
	public LinkedList<?> getPOSTaggedWord(String arabicWord) {
		return wordDAO.getPOSTaggedWord(arabicWord);
	}

	@Override
	public LinkedList<?> getStemmedWord(String arabicWord) {
		return wordDAO.getStemmedWord(arabicWord);
	}

	@Override
	public String getFarsiMeaning(String word) {
		return wordDAO.getFarsiMeaning(word);
	}

	@Override
	public void updateFarsiMeaning(String word, String farsiMeaning) {
		wordDAO.updateFarsiMeaning(word, farsiMeaning);
	}

	@Override
	public String scrapeFarsiMeaning(String filePath) {
		return wordDAO.scrapeFarsiMeaning(filePath);
	}

	@Override
	public void saveWordAndUrduMeaning(String word, String urduMeaning) {
		wordDAO.saveWordAndUrduMeaning(word, urduMeaning);
	}

	@Override
	public String[] scrapeWordAndUrduMeaning(String filePath) {
		return wordDAO.scrapeWordAndUrduMeaning(filePath);
	}

	@Override
	public void markAsFavorite(String arabicWord, boolean isFavorite) {
		wordDAO.markAsFavorite(arabicWord, isFavorite);

	}

	@Override
	public List<Word> getFavoriteWords() {
		return wordDAO.getFavoriteWords();
	}

	@Override
	public boolean isWordFavorite(String arabicWord) {
		return wordDAO.isWordFavorite(arabicWord);
	}

	@Override
	public void addSearchToHistory(Word word) {
		wordDAO.addSearchToHistory(word);

	}

	@Override
	public List<Word> getRecentSearchHistory(int limit) {
		return wordDAO.getRecentSearchHistory(limit);
	}

	@Override
	public List<String> getAllLemmaztizedWords() {
		return wordDAO.getAllLemmaztizedWords();
	}

	@Override
	public boolean insertLemmatizedWord(String originalWord, String lemmatizedWord) {
		return wordDAO.insertLemmatizedWord(originalWord, lemmatizedWord);
	}

	@Override
	public String getLemmatizedWord(String originalWord) {
		return wordDAO.getLemmatizedWord(originalWord);
	}
	public String[] getMeaningsFromDB(String word) {
		return wordDAO.getMeaningsFromDB(word);
	}

}