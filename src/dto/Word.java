package dto;

public class Word {
    private final String arabicWord;
    private final String urduMeaning;
    private final String persianMeaning;

    public Word(String arabicWord, String urduMeaning, String persianMeaning) {
        this.arabicWord = arabicWord;
        this.urduMeaning = urduMeaning;
        this.persianMeaning = persianMeaning;
    }

    public String getArabicWord() {
        return arabicWord;
    }

    public String getUrduMeaning() {
        return urduMeaning;
    }

    public String getPersianMeaning() {
        return persianMeaning;
    }
}