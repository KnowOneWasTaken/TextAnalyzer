import java.util.ArrayList;
import java.util.List;

public class FilterCommonWords implements Filter {
    private String name = "FilterCommonWords";

    @Override
    public String getName () {
        return name;
    }

    @Override
    public String getDescription () {
        return "This Filter filters out every word that is common in the reference";
    }

    @Override
    public List<WordCount> filter (PartyProgrammStatistics party1, PartyProgrammStatistics party2) {
        List<WordCount> filtered = new ArrayList<>();
        for (WordCount wordCount : party1.getList()) {
            if (wordCount.getReferencePercentage() >= 0.001) {
                filtered.add(wordCount);
            }
        }
        for (WordCount wordCount : party2.getList()) {
            if (wordCount.getReferencePercentage() >= 0.001) {
                filtered.add(wordCount);
            }
        }
        for (WordCount wordCount : filtered) {
            party1.getList().remove(wordCount);
            party2.getList().remove(wordCount);
        }
        party1.setNumberOfWords();
        party2.setNumberOfWords();
        return filtered;
    }
}
