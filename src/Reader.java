import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.text.Normalizer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;


public class Reader {
    private static final Pattern pattern = Pattern.compile("[^\\p{L}]+", Pattern.UNICODE_CHARACTER_CLASS);
    private Map<String, Integer> readTextFile(String file, CharacterSetType type) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        BufferedReader bufferedReader;
        if (type == CharacterSetType.UTF8) {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(Files.newInputStream(Paths.get(file)), StandardCharsets.UTF_8)
            );
        } else {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(Files.newInputStream(Paths.get(file)), Charset.forName("windows-1252"))
            );
        }
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                builder.append(line).append(" ");
            }
        }
        bufferedReader.close();
        line = builder.toString().trim();
        line = Normalizer.normalize(line, Normalizer.Form.NFC);
        line = line.trim().replaceAll("â€ž", "").replaceAll("â€œ", "").replaceAll("- (?!und)", "").
                replaceAll("\\*innen", "").replaceAll("\\d+", "").replaceAll("[A-Z]\\.", "").toLowerCase();
        String[] words = pattern.split(line);
        int count = 0;
        for (String word : words) {
            if (word.trim().isEmpty()) {
                continue;
            }
            //System.out.print(word + "|");
            //if (count % 10 == 0) System.out.println();
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
            count++;
        }
        return map;
    }

    private List<WordCount> convertMap(Map<String, Integer> map) {
        List<WordCount> wordCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            WordCount wordCount = new WordCount(word, count);
            wordCounts.add(wordCount);
        }
        return wordCounts.stream().sorted().collect(Collectors.toList());
    }

    public List<WordCount> countFile(String file, CharacterSetType type) throws IOException {
        return setPercentages(convertMap(readTextFile(file, type)));
    }

    private List<WordCount> setPercentages(List<WordCount> list) {
        int countOfWords = 0;
        for (WordCount wordCount : list) {
            countOfWords += wordCount.getCount();
        }
        int i = 0;
        int previousCount = list.get(0).getCount();
        for (WordCount wordCount : list) {
            if (wordCount.getCount() < previousCount) {
                previousCount = wordCount.getCount();
                i++;
            }
            wordCount.setPercentage(wordCount.getCount()* 1.0f / countOfWords);
            wordCount.setRank(i);
        }
        return list;
    }

    public static void addDatabase(List<WordCount> dataBase, List<WordCount> list) {
        for (WordCount wordCount : list) {
            if (dataBase.contains(wordCount)) {
                wordCount.setReferencePercentage(getEntry(wordCount, dataBase).getPercentage());
            }
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

    public List<WordCount> importFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);

        Type listType = new TypeToken<List<WordCount>>() {}.getType();
        List<WordCount> referenceList = gson.fromJson(reader, listType);

        reader.close();
        return referenceList;
    }
}