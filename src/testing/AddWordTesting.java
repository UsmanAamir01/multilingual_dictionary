package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class AddWordTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
    }

    @Test
    public void testAddWordToDB_ValidWord() {
        Word word = new Word("سلام", "سلام", "سلام");
        assertTrue(dao.addWordToDB(word), "The word should be added successfully.");
    }


    @Test
    public void testAddWordToDB_DuplicateWord() {
        Word word = new Word("كلمة", "لفظ", "واژه");
        dao.addWordToDB(word);

        Word duplicateWord = new Word("كلمة", "لفظ", "واژه");
        assertFalse(dao.addWordToDB(duplicateWord), "Adding a duplicate word should return false.");
    }

    @Test
    public void testAddWordToDB_NullWord() {
        assertThrows(NullPointerException.class, () -> dao.addWordToDB(null),
                "Adding a null word should throw NullPointerException.");
    }
    
}
