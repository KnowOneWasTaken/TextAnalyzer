import java.util.List;

public class PartyProgrammStatistics {
    private List<WordCount> list;
    private String name;
    private int numberOfWords = 0;

    public PartyProgrammStatistics(String name, List<WordCount> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public List<WordCount> getList() {
        return list;
    }

    public void setNumberOfWords (int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public int getNumberOfWords () {
        return numberOfWords;
    }
}
