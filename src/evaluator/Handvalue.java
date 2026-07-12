import java.util.List;
import model.Rank;

public class Handvalue{
    private final Handrank handrank;
    private final List<Rank> tieBreakers;

    public Handvalue(Handrank handRank, List<Rank> tieBreakers){
        this.handrank = handRank;
        this.tieBreakers = tieBreakers;
    }

}