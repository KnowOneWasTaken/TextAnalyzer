import java.util.ArrayList;
import java.util.List;

public class FilterNotInReference implements Filter {
    private String name = "Not-In-Reference";

    @Override
    public String getName () {
        return name;
    }

    @Override
    public String getDescription () {
        return "This Filter filters out all words that are not in the reference database. ";
    }

    @Override
    public List<WordCount> filter (PartyProgrammStatistics party1, PartyProgrammStatistics party2) {
        List<WordCount> filtered = new ArrayList<>();
        for (WordCount wordCount : party1.getList()) {
            if (wordCount.getReferencePercentage() == 0) {
                filtered.add(wordCount);
                party1.getList().remove(wordCount);
            }
        }
        for (WordCount wordCount : party2.getList()) {
            if (wordCount.getReferencePercentage() == 0) {
                filtered.add(wordCount);
                party2.getList().remove(wordCount);
            }
        }
        return filtered;
    }
}
