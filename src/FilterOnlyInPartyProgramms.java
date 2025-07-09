import java.util.ArrayList;
import java.util.List;

public class FilterOnlyInPartyProgramms implements Filter {
    private String name = "OnlyInPartyProgramms";

    @Override
    public String getName () {
        return name;
    }

    @Override
    public String getDescription () {
        return "This Filter filters out every word other than these, that are in both party programms but not in the reference database ";
    }

    @Override
    public List<WordCount> filter (PartyProgrammStatistics party1, PartyProgrammStatistics party2) {
        List<WordCount> filtered = new ArrayList<>();
        for (WordCount wordCount : party1.getList()) {
            if (party2.getList().contains(wordCount) && wordCount.getReferencePercentage() == 0) {
                filtered.add(wordCount);
            }
        }
        for (int i = 0; i < party1.getList().size(); i++) {
            if (!filtered.contains(party1.getList().get(i))) {
                party1.getList().remove(party1.getList().get(i));
            }
        }
        for (int i = 0; i < party2.getList().size(); i++) {
            if (!filtered.contains(party2.getList().get(i))) {
                party2.getList().remove(party2.getList().get(i));
            }
        }
        party1.setNumberOfWords();
        party2.setNumberOfWords();
        return filtered;
    }
}
