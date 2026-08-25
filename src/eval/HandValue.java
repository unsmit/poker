package eval;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class HandValue{
    private final HandRank handRank;
    private final ArrayList<Card> tieBreakers;

    public HandValue(HandRank handRank, ArrayList<Card> tieBreakers){
        this.handRank = handRank;
        this.tieBreakers = new ArrayList<>(tieBreakers);
    }

    public HandRank getHandRank(){
        return this.handRank;
    }

    public List<Card> getTieBreakers(){
        return new ArrayList<Card>(tieBreakers);
    }

}