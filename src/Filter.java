import java.util.List;

public interface Filter {
    String getName ();
    String getDescription();
    List<String> filter(PartyProgrammStatistics party);
}
