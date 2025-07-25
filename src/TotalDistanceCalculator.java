import java.util.List;

public final class TotalDistanceCalculator {
    private TotalDistanceCalculator() {}
    public static double calculateDistance(List<Double> distanceList) {
        double totalDistance = 0;
        for (Double distance: distanceList) {
            totalDistance += distance;
        }
        return Math.sqrt(totalDistance);

    }
}
