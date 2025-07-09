import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.stream.Stream;

public class Main {
    public static void main (String[] args) {
        Reader reader = new Reader();
        try {
            String[] files = {"MLPD","DW", "AFD", "GRUENE", "CDU-CSU", "FDP", "BSW", "LINKE", "SPD"};
            List<WordCount> referenz = reader.importFromJson("referenz_export.json");
            System.out.println("Loaded referenze");
            List<PartyProgrammStatistics> parteiprogramme = new ArrayList<>();
            for (String file : files) {
                List<WordCount> list = reader.importFromJson("wps/" + file + "/" + file + "_export.json");
                parteiprogramme.add(new PartyProgrammStatistics(file, list));
                System.out.println("Loaded " + file);
            }
            List<Filter> filters = new ArrayList<>();
            filters.add(new FilterOnlyInPartyProgramms());
            AnalysisTool analysisTool = new AnalysisTool(filters);
            double[][] distanceMatrix = new double[parteiprogramme.size()][parteiprogramme.size()];
            for (int i = 0; i<parteiprogramme.size(); i++) {
                for (int j = i+1; j<parteiprogramme.size(); j++) {
                    distanceMatrix[i][j] = analysisTool.analyze(parteiprogramme.get(i), parteiprogramme.get(j), "Analyse Vergleich"+parteiprogramme.get(i).getName()+" & "+ parteiprogramme.get(j).getName());
                    distanceMatrix[j][i] = distanceMatrix[i][j];
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    private static void findWord(String word, List<WordCount> wordCounts, int countOfWords) {
        boolean found = false;
        for (WordCount wc : wordCounts) {
            if (wc.equals(new WordCount(word, 0))) {
                System.out.println(wc.getWord() + ": " + wc.getCount() + "; " + wc.percentage(wc.getPercentage()));
                found = true;
            }
        }
        if (!found) {
            System.out.println("Not found");
        }
    }

    private static WordCount getEntry(WordCount wc, List<WordCount> list) {
        if (!list.contains(wc)) {
            return new WordCount(wc.getWord(), wc.getCount());
        }
        for (WordCount wc1 : list) {
            if (wc.equals(wc1)) {
                return wc1;
            }
        }
        return new WordCount(wc.getWord(), wc.getCount());
    }

    /**
     * Schreibt die gegebenen Strings zeilenweise in die angegebene Datei.
     *
     * @param lines Array von Strings, die in die Datei geschrieben werden sollen
     * @param filename Name der Zieldatei (inkl. Pfad, falls nötig)
     * @throws IOException Falls ein Fehler beim Schreiben auftritt
     */
    private static void writeLinesToFile (String[] lines, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void generateJsonReferences(String inputFolder, CharacterSetType encoding) throws IOException {
        Reader reader = new Reader();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Stream<Path> paths = Files.walk(Paths.get(inputFolder)
        )) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(path -> {
                        try {
                            System.out.println("Processing: " + path);
                            List<WordCount> referenz = reader.importFromJson("referenz_export.json");
                            List<WordCount> wordCounts = reader.countFile(path.toString(), encoding);
                            Reader.addDatabase(referenz, wordCounts);

                            // Write JSON output
                            String outputFileName = path.toString().replace(".txt", "_export.json");
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                                String json = gson.toJson(wordCounts);
                                writer.write(json);
                            }

                            System.out.println("Saved: " + outputFileName);
                        } catch (IOException e) {
                            System.err.println("Failed for: " + path + " → " + e.getMessage());
                        }
                    });
        }
    }
}