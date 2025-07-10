import java.util.ArrayList;
import java.util.List;

public class TopFilter {
private static final String name = "TopFilter";
private static final String description = "The Top Filter filters for the top %d words with the highest deviation from the german language";
private final int numberOfWords;
private final int numberFilter;

public TopFilter(int numberOfWords, int numberFilter) {
    this.numberOfWords = numberOfWords;
    this.numberFilter = numberFilter;
}

    public String getName() {
        return name;
    }


    public String getDescription() {
        return String.format(description,numberOfWords);
    }


    public List<DeviationContainer> filter(PartyProgrammStatistics party1) {
        List<DeviationContainer> deviationContainers1 = new ArrayList<>();
        for (WordCount wordCount1: party1.getList()) {
            if (wordCount1.getReferencePercentage() == 0 || wordCount1.getCount() <= numberFilter) {
            }
            else if (wordCount1.getPercentage() > wordCount1.getReferencePercentage()) {
                deviationContainers1.add(new DeviationContainer(wordCount1.getPercentage() / wordCount1.getReferencePercentage(), wordCount1));
            } else {
                deviationContainers1.add(new DeviationContainer(wordCount1.getReferencePercentage()/wordCount1.getPercentage(), wordCount1));
            }

        }
        deviationContainers1.sort(null);
        List<DeviationContainer> list = new ArrayList<>();
        while (list.size() < numberOfWords && !deviationContainers1.isEmpty()) {
            list.add(deviationContainers1.get(0));
            deviationContainers1.remove(0);
        }
    return list;
    }
}
