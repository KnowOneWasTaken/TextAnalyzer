import java.util.Objects;

public class WordCount implements Comparable<WordCount> {
    private final String word;
    private int count;
    private double percentage = 0;
    private int rank = 0;
    private double referencePercentage = 0;
    String party;

    public WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getWord() {
        return word;
    }

    public void setPercentage(double p) {
        this.percentage = p;
    }

    public double getPercentage() {
        return percentage;
    }

    public String percentage(double percentage) {
        return Math.round(percentage*1000000)/10000f + "%";
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return getWord() + ": " + getCount() + "; " + percentage(percentage) + "; Rang: " + rank + "; Referenz: " + percentage(referencePercentage);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WordCount && word.equals(((WordCount) o).word);
    }

    public void setReferencePercentage(double percentage) {
        this.referencePercentage = percentage;
    }

    public double getReferencePercentage() {
        return referencePercentage;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    @Override
    public int compareTo(WordCount o) {
        if (o instanceof WordCount) {
            if (o.getPercentage() < getPercentage()) {
                return -1;
            } else if (o.getCount() > getCount()) {
                return 1;
            }
            return 0;
        }
        return 0;
    }

    @Override
    public int hashCode () {
        return Objects.hashCode(word);
    }
}
