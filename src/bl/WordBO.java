package bl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import dal.IWordDAOFacade;
import dal.WordDAOFacade;
import dto.Word;

public class WordBO {
	private IWordDAOFacade wordDAOFacade;

	public WordBO() {
		wordDAOFacade = new WordDAOFacade();
	}

	public boolean updateWord(Word w) {
		return wordDAOFacade.updateWordToDB(w);
	}

	public boolean addWord(Word w) {
		return wordDAOFacade.addWordToDB(w);
	}

	public boolean removeWord(String arabicWord) {
		return wordDAOFacade.removeWordFromDB(arabicWord);
	}

	public String[][] getWordsWithMeanings() {
		List<Word> wordList = wordDAOFacade.getAllWords();
		String[][] wordData = new String[wordList.size()][3];

		for (int i = 0; i < wordList.size(); i++) {
			Word word = wordList.get(i);
			wordData[i][0] = word.getArabicWord();
			wordData[i][1] = word.getUrduMeaning();
			wordData[i][2] = word.getPersianMeaning();
		}

		return wordData;
	}

	public Word viewOnceWord(String arabicWord) {
		return wordDAOFacade.viewOnceWord(arabicWord);
	}

	public List<Word> importDataFromFile(String filePath) {
		return wordDAOFacade.importDataFromFile(filePath);
	}

	public boolean insertImportedData(List<Word> words) {
		return wordDAOFacade.insertImportedData(words);
	}

	public String searchWord(String searchTerm) {
		List<Word> results = wordDAOFacade.searchWord(searchTerm);
		if (!results.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Word word : results) {
				sb.append("Word: ").append(word.getArabicWord()).append(", Urdu Meaning: ")
						.append(word.getUrduMeaning()).append(", Persian Meaning: ").append(word.getPersianMeaning())
						.append("\n");
			}
			return sb.toString();
		}
		return "Word not found.";
	}

	public String getMeanings(String searchText, String language) {
		return wordDAOFacade.getMeanings(searchText, language);
	}

	public List<String> getTaggedAndStemmedWords() {
		return wordDAOFacade.getTaggedAndStemmedWords();
	}

	public String getPOSTaggedWord(String arabicWord) {
		return wordDAOFacade.getPOSTaggedWord(arabicWord);
	}

	public String getStemmedWord(String arabicWord) {
		return wordDAOFacade.getStemmedWord(arabicWord);
	}

	public String performPOSTagging(String arabicText) throws Exception {
		StringBuilder resultText = new StringBuilder();

		try (URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File("/mnt/data/AlKhalil-2.1.21.jar").toURI().toURL() })) {

			Class<?> posTaggerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
			Object posTaggerInstance = posTaggerClass.getDeclaredConstructor().newInstance();
			Method tagMethod = posTaggerClass.getMethod("analyzedWords", String.class);
			Object result = tagMethod.invoke(posTaggerInstance, arabicText);

			if (result instanceof LinkedList) {
				LinkedList<?> wordList = (LinkedList<?>) result;
				if (!wordList.isEmpty()) {
					Object firstWord = wordList.getFirst();

					Method getVoweledWordMethod = firstWord.getClass().getMethod("getVoweledWord");
					Method getStemMethod = firstWord.getClass().getMethod("getStem");
					Method getWordTypeMethod = firstWord.getClass().getMethod("getWordType");
					Method getWordRootMethod = firstWord.getClass().getMethod("getWordRoot");
					Method getPosMethod = firstWord.getClass().getMethod("getPos");

					String voweledWord = (String) getVoweledWordMethod.invoke(firstWord);
					String stem = (String) getStemMethod.invoke(firstWord);
					String wordType = (String) getWordTypeMethod.invoke(firstWord);
					String wordRoot = (String) getWordRootMethod.invoke(firstWord);
					String pos = (String) getPosMethod.invoke(firstWord);

					resultText.append("Voweled Word: ").append(voweledWord).append("\n").append("Stem of the Word: ")
							.append(stem).append("\n").append("Type of the Word: ").append(wordType).append("\n")
							.append("Root of the Word: ").append(wordRoot).append("\n").append("Part of Speech (POS): ")
							.append(pos).append("\n");
				} else {
					resultText.append("Tags Not Found!");
				}
			} else {
				resultText.append("Unexpected result type: ").append(result.getClass().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error during POS tagging", e);
		}

		return resultText.toString();
	}
}