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
            List<List> parteiprogramme = new ArrayList<>();
            String[] files = {"afd.txt", "green.txt", "cdu.txt", "fdp.txt"};
            for (String file : files) {
                parteiprogramme.add(reader.countFile("wps/" + file, CharacterSetType.WINDOWS));
                Reader.addDatabase(referenz, parteiprogramme.get(parteiprogramme.size() - 1));
                System.out.println("Loaded " + file);
            }

            saveList(parteiprogramme.get(0), "first.txt");
            System.out.println("Saved first file");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    private static void findWord(String word, List<WordCount> wordCounts, int countOfWords) {
        boolean found = false;
        for (WordCount wc : wordCounts) {
            if (wc.equals(new WordCount(word, 0))) {
                System.out.println(wc.getWord() + ": " + wc.getCount() + "; " + wc.percentage());
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

    private static void analysiereHaeufigerAlsNormalAuftretendeWoerter(List<WordCount> referenz, List<WordCount> list, String fileName) throws IOException {
        System.out.println("Creating " + fileName);
        List<CompareCount> modifiedList = new ArrayList<>();
        for (WordCount wordCount : list) {
            WordCount reference = getEntry(wordCount, referenz);
            float multiplyer = (wordCount.getPercentage()/reference.getPercentage());
            if (multiplyer < 2 || wordCount.getCount() < 4 || !referenz.contains(wordCount) || reference.getCount() < 2) {
                continue;
            }
            modifiedList.add(new CompareCount(wordCount, multiplyer));
        }
        System.out.println("Created modified List");
        modifiedList = modifiedList.stream().sorted().collect(Collectors.toList());
        System.out.println("Sorted modified list");
        String[] lines = new String[modifiedList.size()];
        int i = 0;
        for (CompareCount wordCount : modifiedList) {
            WordCount reference = getEntry(wordCount, referenz);
            lines[i] = (wordCount + " | "+ reference.getCount() + ": " + reference.percentage() + " | mutiplyer: "+wordCount.getMultiplier());
            i++;
        }
        writeLinesToFile(lines, fileName);
        System.out.println(fileName + " saved");
    }

    private static void analysiereNichtInReferenzWoerter(List<WordCount> referenz, List<WordCount> list, String fileName) throws IOException {
        System.out.println("Creating " + fileName);
        List<WordCount> modifiedList = new ArrayList<>();
        for (WordCount wordCount : list) {
            if (!referenz.contains(wordCount)) {
                modifiedList.add(wordCount);
            }
        }
        modifiedList = modifiedList.stream().sorted().collect(Collectors.toList());
        String[] lines = new String[modifiedList.size()];
        for (int i = 0; i < modifiedList.size(); i++) {
            lines[i] = modifiedList.get(i).toString();
        }
        writeLinesToFile(lines, fileName);
        System.out.println(fileName + " saved");
    }

    private static void analysiereLowRankReferenzHighCount(List<WordCount> referenz, List<WordCount> list, String fileName) throws IOException {
        System.out.println("Creating " + fileName);
        List<CompareCount> modifiedList = new ArrayList<>();
        for (WordCount wordCount : list) {
            modifiedList.add(new CompareCount(wordCount, (float) Math.pow(getEntry(wordCount, referenz).getRank(), 1) / (wordCount.getRank()+1)));
        }
        modifiedList = modifiedList.stream().sorted().collect(Collectors.toList()).subList(0,30);
        String[] lines = new String[modifiedList.size()];
        for (int i = 0; i < modifiedList.size(); i++) {
            lines[i] = modifiedList.get(i).toString();
        }
        writeLinesToFile(lines, fileName);
        System.out.println(fileName + " saved");
    }

    private static void saveList(List<WordCount> list, String fileName) throws IOException {
        String[] lines = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lines[i] = list.get(i).toString();
        }
        writeLinesToFile(lines, fileName);

    }

    private static void rareWords(List<WordCount> referenz, List<WordCount> list, String fileName) throws IOException {
        System.out.println("Creating " + fileName);
        List<WordCount> modifiedList = new ArrayList<>();
        for (WordCount wordCount : list) {
            if (referenz.contains(wordCount) && getEntry(wordCount, referenz).getCount() < 10000) {
                modifiedList.add(wordCount);
            }
        }
        modifiedList = modifiedList.stream().sorted().collect(Collectors.toList());
        String[] lines = new String[modifiedList.size()];
        for (int i = 0; i < modifiedList.size(); i++) {
            lines[i] = modifiedList.get(i).toString();
        }
        writeLinesToFile(lines, fileName);
        System.out.println(fileName + " saved");
    }
}