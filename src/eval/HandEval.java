package eval;
import model.*;

import java.util.ArrayList;

public class HandEval {
    // TODO: Create a large list containing all cards and evaluate all possible hands
    private ArrayList<Card> fullHand = new ArrayList<>();

    public HandEval(Board board, Player player){
        fullHand.addAll(board.getCommunityCards());
        fullHand.addAll(player.getPlayerCards());
    }
    

}
