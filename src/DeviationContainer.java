public class DeviationContainer implements Comparable<DeviationContainer> {
    private final double deviation;
    private final WordCount wordCount;
    public DeviationContainer(double deviation, WordCount wordCount) {
        this.deviation = deviation;
        this.wordCount = wordCount;
    }
    public int  getWordCount() {
        return wordCount.getCount();
    }
    public double getDeviation() {
        return deviation;
    }
    public String getWord() {
        return wordCount.getWord();
    }

    @Override
    public int compareTo(DeviationContainer object) {
        if  (this.deviation < object.deviation) {
            return 1;
        }
        else if (this.deviation > object.deviation) {
            return -1;
        }
        return 0;
    }
}
