package dto;

public class Word {
    private String word;
    private String meaning1;
    private String meaning2;

    public Word(String word, String meaning1, String meaning2) {
        this.word = word;
        this.meaning1 = meaning1;
        this.meaning2 = meaning2;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning1() {
        return meaning1;
    }

    public String getMeaning2() {
        return meaning2;
    }

    public void setMeaning1(String meaning1) {
        this.meaning1 = meaning1;
    }

    public void setMeaning2(String meaning2) {
        this.meaning2 = meaning2;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", meaning1='" + meaning1 + '\'' +
                ", meaning2='" + meaning2 + '\'' +
                '}';
    }
}
