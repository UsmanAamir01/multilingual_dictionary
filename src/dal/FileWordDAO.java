package dal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import dto.Word;

public class FileWordDAO implements IWordDAO {
	private String filePath;

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
					words.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim()));
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
					importedWords.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim()));
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
}
