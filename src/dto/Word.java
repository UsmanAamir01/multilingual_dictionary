package dto;

public class Word {
    private String arabicWord;
    private String urduMeaning;
    private String persianMeaning;
    private boolean isFavorite;

    public Word(String arabicWord, String urduMeaning, String persianMeaning) {
        this.arabicWord = arabicWord;
        this.urduMeaning = urduMeaning;
        this.persianMeaning = persianMeaning;
        this.isFavorite = false; 
    }

    public Word(String arabicWord, String urduMeaning, String persianMeaning, boolean isFavorite) {
        this.arabicWord = arabicWord;
        this.urduMeaning = urduMeaning;
        this.persianMeaning = persianMeaning;
        this.isFavorite = isFavorite;
    }

    public String getArabicWord() {
        return arabicWord;
    }

    public void setArabicWord(String arabicWord) {
        this.arabicWord = arabicWord;
    }

    public String getUrduMeaning() {
        return urduMeaning;
    }

    public void setUrduMeaning(String urduMeaning) {
        this.urduMeaning = urduMeaning;
    }

    public String getPersianMeaning() {
        return persianMeaning;
    }

    public void setPersianMeaning(String persianMeaning) {
        this.persianMeaning = persianMeaning;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
