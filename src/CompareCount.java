public class CompareCount extends WordCount implements Comparable<WordCount> {
    private float multiplier = 1;

    public CompareCount(String word, int count) {
        super(word, count);
    }
    public CompareCount(WordCount count, float multiplier) {
        super(count.getWord(), count.getCount());
        setPercentage(count.getPercentage());
        this.multiplier = multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }
    public float getMultiplier() {
        return multiplier;
    }
    @Override
    public int compareTo(WordCount o) {
        if (o instanceof CompareCount) {
            if (((CompareCount)o).getMultiplier() < getMultiplier()) {
                return -1;
            } else if (((CompareCount)o).getMultiplier() > getMultiplier()) {
                return 1;
            }
            return 0;
        } else {
            return super.compareTo(o);
        }
    }

    @Override
    public String toString() {
        return getWord() + ": " + getCount() + "; " + percentage() + "; " + multiplier;
    }
}
