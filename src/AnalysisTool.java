import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;

public class AnalysisTool {
    private final List<Filter> filter;
    public AnalysisTool(List<Filter> filter) {
        this.filter = filter;
    }

    public void topFilter(PartyProgrammStatistics party1, int numberOfTopDeviations, int numberFilter, String file) {
        TopFilter topFilter= new TopFilter(numberOfTopDeviations, numberFilter);
        try {
            writeDeviationListToJsonFile(topFilter.filter(party1),file  +" "+topFilter.getName()+ ".json");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public double analyze(PartyProgrammStatistics party1, PartyProgrammStatistics party2, String file) {
        for (Filter filter : filter) {
            try {
                writeLinesToFile(filter.filter(party1, party2), file + "-" + party1.getName() + "-" + party2.getName() + "-" + filter.getName() + ".txt", "Filter-Beschreibung: " + filter.getDescription());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        List<Double> distances = SingleDistanceCalculator.calculateSingleDistance(party1, party2);
        for(int i = 0; i < distances.size(); i++) {
            if (distances.get(i) > 200000000) {
                distances.set(i, Double.valueOf(200000000));
            }
        }
        return TotalDistanceCalculator.calculateDistance(distances);
    }

    /**
     * Schreibt die gegebenen Strings zeilenweise in die angegebene Datei.
     *
     * @param lines Array von Strings, die in die Datei geschrieben werden sollen
     * @param filename Name der Zieldatei (inkl. Pfad, falls nötig)
     * @throws IOException Falls ein Fehler beim Schreiben auftritt
     */
    private static void writeLinesToFile (List<WordCount> lines, String filename, String message) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(message);
            writer.newLine();
            for (WordCount wordCount : lines) {
                writer.write(wordCount.toString());
                writer.newLine();
            }
        }

    }
    /**
     * Schreibt die gegebenen Strings zeilenweise in die angegebene Datei.
     *
     * @param lines Array von Strings, die in die Datei geschrieben werden sollen
     * @param filename Name der Zieldatei (inkl. Pfad, falls nötig)
     * @throws IOException Falls ein Fehler beim Schreiben auftritt
     */
    private static void writeLinesToFileDeviation (List<DeviationContainer> lines, String filename, String message) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(message);
            writer.newLine();
            for (DeviationContainer container : lines) {
                writer.write("Word "+container.getWord()+" deviation: " + container.getDeviation() + " count: " + container.getWordCount());
                writer.newLine();
            }
        }
    }

    public static void findWord(List<PartyProgrammStatistics> parties, String word) {
        WordCount wordCount = new WordCount(word,0);
        List<WordCount> wordCounts = new ArrayList<>();
        for (PartyProgrammStatistics party : parties) {
            if (party.getList().contains(wordCount)) {
                WordCount listElement = party.getList().get(party.getList().indexOf(wordCount));
                listElement.setParty(party.getName());
                wordCounts.add(listElement);
            } else {
                WordCount wc = new WordCount(word, 0);
                wc.setParty(party.getName());
                wordCounts.add(wc);
            }
        }
        wordCounts = wordCounts.stream().sorted().collect(Collectors.toList());
        for (WordCount wc : wordCounts) {
            System.out.println("Party: " + wc.getParty() + "; " + wc);
        }
    }

    public static void writeDeviationListToJsonFile(List<DeviationContainer> list, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Konvertiere in flaches Exportformat
        List<DeviationExport> exportList = new ArrayList<>();
        for (DeviationContainer dc : list) {
            exportList.add(new DeviationExport(
                    dc.getDeviation(),
                    dc.getWord(),
                    dc.getWordCount()
            ));
        }

        // Schreibe als JSON-Array
        mapper.writeValue(new File(filePath), exportList);
    }
}
