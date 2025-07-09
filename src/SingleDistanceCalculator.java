import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates a list of distances based on the frequency of words compared to the use in the normal german language.
 */
public final class SingleDistanceCalculator {
    private SingleDistanceCalculator() {}
    public static List<Double> calculateSingleDistance(PartyProgrammStatistics partyProgrammStatistics1, PartyProgrammStatistics partyProgrammStatistics2) {
        List<Double> distanceList = new ArrayList<>();
        Map<String, WordCount> wordMap2 = new HashMap<>();
        for (WordCount wordCount2: partyProgrammStatistics1.getList()) {
            wordMap2.put(wordCount2.getWord(), wordCount2);
        }
        for (WordCount wordCount1 : partyProgrammStatistics1.getList()) {
            if (wordMap2.containsKey(wordCount1.getWord())) {
                distanceList.add(calculateDistanceVariant1(wordCount1, wordMap2.get(wordCount1.getWord()), partyProgrammStatistics1.getNumberOfWords(), partyProgrammStatistics2.getNumberOfWords()));
            wordMap2.remove(wordCount1.getWord());
            }  else {
                distanceList.add(calculateDistanceVariant1(wordCount1, null, partyProgrammStatistics1.getNumberOfWords(), partyProgrammStatistics2.getNumberOfWords()));
            }
        }
        for (WordCount wordCount2: wordMap2.values()) {
            distanceList.add(calculateDistanceVariant1(null, wordCount2, partyProgrammStatistics1.getNumberOfWords(), partyProgrammStatistics2.getNumberOfWords()));
        }
        return distanceList;

    }

    private static double calculateDistanceVariant1(WordCount wordCount1, WordCount wordCount2, int wordCountTotal1, int wordCountTotal2) {
        if (wordCount2 != null&&wordCount1!=null) {
            double databasePercentile = wordCount1.getReferencePercentage();
            return calculateDistanceDifference(((double) wordCount1.getCount()* wordCountTotal2 - wordCount2.getCount()* wordCountTotal1)/ (wordCountTotal1*wordCountTotal2), databasePercentile);
        } else if (wordCount1!= null) {
            double databasePercentile = wordCount1.getReferencePercentage();
            return calculateDistanceDifference((double) wordCount1.getCount() / wordCountTotal1,  databasePercentile);
        } else if (wordCount2!= null) {
            double databasePercentile = wordCount2.getReferencePercentage();
            return calculateDistanceDifference((double) wordCount2.getCount() / wordCountTotal2,  databasePercentile);
        }
        return 0;
    }
    private static double calculateDistanceDifference(double value, double percentile) {
        double distance = Math.abs(value);
       return distance / percentile;
}
    private static double calculateDistanceVariant2(WordCount wordCount1, WordCount wordCount2, int wordCountTotal1, int wordCountTotal2) {
        if (wordCount2 != null&&wordCount1!=null) {
            return calculateDifferenceToNormal(((double)wordCount1.getCount())/ wordCountTotal1, wordCount1.getPercentage()) - calculateDifferenceToNormal(((double)wordCount2.getCount())/ wordCountTotal2, wordCount2.getPercentage());
            } else if (wordCount1!= null) {
            return calculateDifferenceToNormal(((double)wordCount1.getCount())/ wordCountTotal1, wordCount1.getPercentage());
            } else if (wordCount2!= null) {
            return calculateDifferenceToNormal(((double)wordCount2.getCount())/ wordCountTotal2, wordCount2.getPercentage());
            }
        return 0;
    }
    private static double calculateDifferenceToNormal(double valuePercentile, double percentile) {
        if (valuePercentile > percentile) {
            return valuePercentile/ percentile -1;
        }
        return -1* percentile/ valuePercentile +1;
    }
}