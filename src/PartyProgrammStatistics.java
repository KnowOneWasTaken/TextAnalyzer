import java.util.List;

public class PartyProgrammStatistics {
    private List<WordCount> list;
    private String name;
    private int numberOfWords = 0;

    public PartyProgrammStatistics(String name, List<WordCount> list) {
        this.name = name;
        this.list = list;
        calculateNumberOfWords();
    }

    public String getName() {
        return name;
    }

    public List<WordCount> getList() {
        return list;
    }

    public void setNumberOfWords () {
        calculateNumberOfWords();
    }

    public int getNumberOfWords () {
        return numberOfWords;
    }

    private void calculateNumberOfWords() {
        int count = 0;
        for (WordCount wc : list) {
            count += wc.getCount();
        }
        numberOfWords = count;
    }
}
