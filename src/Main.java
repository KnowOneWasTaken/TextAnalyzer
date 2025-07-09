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
            List<WordCount> afd = reader.countFile("afd.txt", CharacterSetType.WINDOWS);
            System.out.println("Finished first file");
            List<WordCount> green = reader.countFile("green.txt", CharacterSetType.WINDOWS);
            System.out.println("Finished second file");
            List<WordCount> cdu = reader.countFile("cdu.txt", CharacterSetType.WINDOWS);
            System.out.println("Finished third file");
            List<WordCount> fdp = reader.countFile("fdp.txt", CharacterSetType.WINDOWS);
            System.out.println("Finished forth file");
            List<WordCount> referenz = reader.countFile("referenz.txt", CharacterSetType.UTF8);
            System.out.println("Finished counting");
            System.out.println();
            /*analysiereHaeufigerAlsNormalAuftretendeWoerter(referenz, green, "green-analyse.txt");
            analysiereHaeufigerAlsNormalAuftretendeWoerter(referenz, cdu, "cdu-analyse.txt");
            analysiereHaeufigerAlsNormalAuftretendeWoerter(referenz, afd, "afd-analyse.txt");
            analysiereHaeufigerAlsNormalAuftretendeWoerter(referenz, fdp, "fdp-analyse.txt");

            analysiereNichtInReferenzWoerter(referenz, green, "green-not-in-reference.txt");
            analysiereNichtInReferenzWoerter(referenz, cdu, "cdu-not-in-reference.txt");
            analysiereNichtInReferenzWoerter(referenz, afd, "afd-not-in-reference.txt");
            analysiereNichtInReferenzWoerter(referenz, fdp, "fdp-not-in-reference.txt");

            analysiereLowRankReferenzHighCount(referenz, green, "green-LowRankReferenzHighCount.txt");
            analysiereLowRankReferenzHighCount(referenz, cdu, "cdu-LowRankReferenzHighCount.txt");
            analysiereLowRankReferenzHighCount(referenz, afd, "afd-LowRankReferenzHighCount.txt");
            analysiereLowRankReferenzHighCount(referenz, fdp, "fdp-LowRankReferenzHighCount.txt");

            saveList(green, "green-statistics");
            saveList(cdu, "cdu-statistics");
            saveList(afd, "afd-statistics");
            saveList(fdp, "fdp-statistics");
            */

            rareWords(referenz, green, "green-rareWords.txt");
            rareWords(referenz, cdu, "cdu-rareWords.txt");
            rareWords(referenz, afd, "afd-rareWords.txt");
            rareWords(referenz, fdp, "fdp-rareWords.txt");

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