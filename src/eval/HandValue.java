package eval;
import model.Rank;

import java.util.ArrayList;
import java.util.List;

public class HandValue{
    private final HandRank handRank;
    private final List<Rank> tieBreakers;

    public HandValue(HandRank handRank, List<Rank> tieBreakers){
        this.handRank = handRank;
        this.tieBreakers = new ArrayList<>(tieBreakers);
    }

    public HandRank getHandRank(){
        return this.handRank;
    }

    public List<Rank> getTieBreakers(){
        return new ArrayList<Rank>(tieBreakers);
    }
}