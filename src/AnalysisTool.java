import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AnalysisTool {
    private final List<Filter> filter;
    public AnalysisTool(List<Filter> filter) {
        this.filter = filter;
    }

    public void analyze(PartyProgrammStatistics party1, PartyProgrammStatistics party2, String file) {
        for (Filter filter : filter) {
            try {
                writeLinesToFile(filter.filter(party1), file + "-" + party1.getName() + "-" + filter.getName() + ".txt", "Filter-Beschreibung: " + filter.getDescription());
                writeLinesToFile(filter.filter(party2), file + "-" + party2.getName() + "-" + filter.getName() + ".txt", "Filter-Beschreibung: " + filter.getDescription());
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
    private static void writeLinesToFile (List<String> lines, String filename, String message) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(message);
            writer.newLine();
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
