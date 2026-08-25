package eval;

public class HandComp{

    public int compare(HandValue hand1, HandValue hand2){
        if(hand1.getHandRank().getValue() > hand2.getHandRank().getValue()){
            return 1;
        } else if(hand2.getHandRank().getValue() > hand1.getHandRank().getValue()){
            return -1;
        }
        
        for(int i = 0; i < hand1.getTieBreakers().size(); i++){
            if(hand1.getTieBreakers().get(i).getRank().getVal() == 
                hand2.getTieBreakers().get(i).getRank().getVal()){
                    continue;
                }

            if(hand1.getTieBreakers().get(i).getRank().getVal() > 
                hand2.getTieBreakers().get(i).getRank().getVal()){
                    return 1;
                } else {
                    return -1;
                }
        }

        return 0;
    }

}