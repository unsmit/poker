package eval;
import java.util.ArrayList;
import model.*;

public class HandEval {
    // TODO: Create a large list containing all cards and evaluate all possible hands
    private ArrayList<Card> fullHand = new ArrayList<>();

    public HandEval(Board board, Player player){
        fullHand.addAll(board.getCommunityCards());
        fullHand.addAll(player.getPlayerCards());
    }
    
    public boolean isFlush(){
        return false;
    }

    public boolean isStraight(){
        return false;
    }

    public boolean straightFlush(){
        return false;
    }

    public boolean royaleFlush(){
        return false;
    }

    public boolean fullHouse(){
        return false;
    }
}
