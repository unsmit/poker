package eval;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import model.*;

public class HandEval {
    // TODO: Create a large list containing all cards and evaluate all possible hands
    private ArrayList<Card> fullHand = new ArrayList<>();
    private Map<Rank, Integer> rankCounts = new EnumMap<>(Rank.class);

    public HandEval(Board board, Player player){
        fullHand.addAll(board.getCommunityCards());
        fullHand.addAll(player.getPlayerCards());

        for(Card card: fullHand){
            Rank rank = card.getRank();

            if(rankCounts.containsKey(rank)){
                rankCounts.put(rank, rankCounts.get(rank)+1);
            } else{
                rankCounts.put(rank, 1);
            }
        }
    }
    
    public boolean isFlush(){
        int heart = 0;
        int spade = 0;
        int club = 0;
        int diamond = 0;

        for(Card card: fullHand){
            switch(card.getSuit()){
                case SPADE:
                    spade++;
                    break;
                case HEART:
                    heart++;
                    break;
                case CLUB:
                    club++;
                    break;
                case DIAMOND:
                    diamond++;
                    break;
            }
        }

        return heart >= 5 || spade >= 5 || club >= 5 || diamond >= 5;

    }

    public boolean isStraight(){
        
        
        return false;
    }

    public boolean isStraightFlush(){
        return false;
    }

    public boolean isRoyaleFlush(){
        return false;
    }

    public boolean isFullHouse(){
        boolean hasThree = false;
        boolean hasTwo = false;

        
        return false;
    }
}
