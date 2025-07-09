import java.util.List;

public class PartyProgrammStatistics {
    private List<WordCount> list;
    private String name;
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
}
