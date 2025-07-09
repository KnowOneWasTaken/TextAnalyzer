import java.util.List;

public interface Filter {
    String getName ();
    String getDescription();
    List<WordCount> filter(PartyProgrammStatistics party1, PartyProgrammStatistics party2);
}
