package dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dto.Word;

public class FileWordDAO implements IWordDAO {
	private String filePath;
	private Object stemmerInstance;
	private Object posTaggerInstance;

	public FileWordDAO(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public Word getWordFromDB(String arabicWord) {
		List<Word> words = getAllWords();
		for (Word word : words) {
			if (word.getArabicWord().equals(arabicWord)) {
				return word;
			}
		}
		return null;
	}

	@Override
	public boolean updateWordToDB(Word w) {
		List<Word> words = getAllWords();
		boolean updated = false;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (Word word : words) {
				if (word.getArabicWord().equals(w.getArabicWord())) {
					writer.write(w.getArabicWord() + "," + w.getUrduMeaning() + "," + w.getPersianMeaning());
					updated = true;
				} else {
					writer.write(word.getArabicWord() + "," + word.getUrduMeaning() + "," + word.getPersianMeaning());
				}
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return updated;
	}

	@Override
	public boolean addWordToDB(Word w) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.write(w.getArabicWord() + "," + w.getUrduMeaning() + "," + w.getPersianMeaning());
			writer.newLine();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeWordFromDB(String arabicWord) {
		List<Word> words = getAllWords();
		boolean removed = false;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (Word word : words) {
				if (!word.getArabicWord().equals(arabicWord)) {
					writer.write(word.getArabicWord() + "," + word.getUrduMeaning() + "," + word.getPersianMeaning());
					writer.newLine();
				} else {
					removed = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return removed;
	}

	@Override
	public List<Word> getAllWords() {
		List<Word> words = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					words.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim(), false));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}

	@Override
	public Word viewOnceWord(String arabicWord) {
		return getWordFromDB(arabicWord);
	}

	@Override
	public List<Word> importDataFromFile(String filePath) {
		List<Word> importedWords = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					importedWords.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim(), false));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return importedWords;
	}

	@Override
	public boolean insertImportedData(List<Word> words) {
		boolean success = true;
		for (Word word : words) {
			if (!addWordToDB(word)) {
				success = false;
			}
		}
		return success;
	}

	@Override
	public List<Word> searchWord(String searchTerm) {
		List<Word> result = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					String arabicWord = parts[0].trim();
					String urduMeaning = parts[1].trim();
					String persianMeaning = parts[2].trim();
					if (arabicWord.toLowerCase().contains(searchTerm.toLowerCase())
							|| urduMeaning.toLowerCase().contains(searchTerm.toLowerCase())
							|| persianMeaning.toLowerCase().contains(searchTerm.toLowerCase())) {
						result.add(new Word(arabicWord, urduMeaning, persianMeaning, false));
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error searching words: " + e.getMessage());
		}
		return result;
	}

	@Override
	public String getMeanings(String searchText, String language) {
		StringBuilder meanings = new StringBuilder();
		boolean found = false;
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					String word = parts[0].trim();
					String urduMeaning = parts[1].trim();
					String persianMeaning = parts[2].trim();
					if (word.equalsIgnoreCase(searchText)) {
						meanings.append("Arabic: ").append(word).append("\n");
						meanings.append("Urdu: ").append(urduMeaning).append("\n");
						meanings.append("Persian: ").append(persianMeaning).append("\n");
						found = true;
						break;
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error retrieving meanings: " + e.getMessage());
			return "Error retrieving meanings.";
		}
		if (!found) {
			return "Word not found.";
		}
		return meanings.toString();
	}

	@Override
	public List<String> getTaggedAndStemmedWords() {
		List<String> results = new ArrayList<>();
		List<Word> words = getAllWords();
		for (Word word : words) {
			String arabicWord = word.getArabicWord();
			LinkedList<?> stemmedWord = getStemmedWord(arabicWord);
			LinkedList<?> taggedWord = getPOSTaggedWord(arabicWord);
			results.add("Original: " + arabicWord + " | Stemmed: " + stemmedWord + " | Tagged: " + taggedWord);
		}
		return results;
	}

	@Override
	public LinkedList<?> getPOSTaggedWord(String arabicWord) {
		LinkedList<Object> posTaggedResult = new LinkedList<>();
		try {
			Method tagMethod = posTaggerInstance.getClass().getMethod("analyzedWords", String.class);
			posTaggedResult = (LinkedList<Object>) tagMethod.invoke(posTaggerInstance, arabicWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return posTaggedResult;
	}

	@Override
	public LinkedList<?> getStemmedWord(String arabicWord) {
		LinkedList<Object> stemmedResult = new LinkedList<>();
		try {
			Method stemMethod = stemmerInstance.getClass().getMethod("stemWords", String.class);
			stemmedResult = (LinkedList<Object>) stemMethod.invoke(stemmerInstance, arabicWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stemmedResult;
	}

	@Override
	public String[] scrapeWordAndUrduMeaning(String filePath) {
		return null;
	}

	@Override
	public void saveWordAndUrduMeaning(String word, String urduMeaning) {

	}

	@Override
	public String scrapeFarsiMeaning(String filePath) {
		return null;
	}

	@Override
	public void updateFarsiMeaning(String word, String farsiMeaning) {

	}

	@Override
	public String getFarsiMeaning(String word) {
		return null;
	}

	@Override
	public void markAsFavorite(String word, boolean isFavorite) {

	}

	@Override
	public List<Word> getFavoriteWords() {
		return null;
	}

	@Override
	public boolean isWordFavorite(String arabicWord) {
		return false;
	}

	@Override
	public void addSearchToHistory(Word word) {

	}

	@Override
	public List<Word> getRecentSearchHistory(int limit) {
		return null;
	}

	@Override
	public List<String> getAllLemmaztizedWords() {
		return null;
	}

	@Override
	public boolean insertLemmatizedWord(String originalWord, String lemmatizedWord) {
		return false;
	}

	@Override
	public String getLemmatizedWord(String originalWord) {
		return null;
	}


	@Override
	public List<String> segmentWordWithDiacritics(String word) {
		// TODO Auto-generated method stub
		return null;
	}

}
