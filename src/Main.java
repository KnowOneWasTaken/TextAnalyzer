import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main (String[] args) {
        Reader reader = new Reader();
        try {
            List<WordCount> referenz = reader.countFile("referenz.txt", CharacterSetType.UTF8);
            System.out.println("Loaded referenze");
            List<PartyProgrammStatistics> parteiprogramme = new ArrayList<>();
            String[] files = {"AFD", "GRUENE", "CDU-CSU", "FDP", "BSW", "LINKE", "SPD"};
            for (String file : files) {
                List<WordCount> list = reader.countFile("wps/" + file + "/" + file + ".txt", CharacterSetType.WINDOWS);
                Reader.addDatabase(referenz, list);
                parteiprogramme.add(new PartyProgrammStatistics(file, list));
                System.out.println("Loaded " + file);
            }
            List<Filter> filters = new ArrayList<>();
            filters.add(new FilterNotInReference());
            AnalysisTool analysisTool = new AnalysisTool(filters);
            analysisTool.analyze(parteiprogramme.get(0), parteiprogramme.get(1), "analysen/ersteAnalyse");
            System.out.println("Analyzed 2 party programmes");

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
}