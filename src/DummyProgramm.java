import java.util.ArrayList;
import java.util.List;

public class DummyProgramm {
    public static void main(String[] args) {
        List<WordCount> list1 = new ArrayList<>();
        list1.add(new WordCount("Hello", 1));
        list1.add(new WordCount("World", 2));

        List<WordCount> list2 = new ArrayList<>();
        list2.add(new WordCount("Hello", 1));
        list2.add(new WordCount("World", 2));

        List<WordCount> list3 = new ArrayList<>();
        list3.add(new WordCount("Hello", 2));
        list3.add(new WordCount("World", 1));

        PartyProgrammStatistics party1 = new PartyProgrammStatistics("List1", list1);
        PartyProgrammStatistics party2 = new PartyProgrammStatistics("List2", list2);
        PartyProgrammStatistics party3 = new PartyProgrammStatistics("List3", list3);
        AnalysisTool analysisTool = new AnalysisTool(new ArrayList<>());
        System.out.println(analysisTool.analyze(party1,party2, "analyse 1"));
        System.out.println(analysisTool.analyze(party2,party3, "analyse 2"));
    }
}
