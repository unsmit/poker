package sim;
import eval.*;
import java.util.List;
import model.*;

public class Worker implements Runnable{
    private int numSims;
    private int numPlayers;
    private Player hero;
    private Board knownBoard;
    private int wins;
    private int loss;
    private int tie;
    
    public Worker(int numSims, int numPlayers, Player hero, Board knownBoard){
        this.numSims = numSims;
        this.numPlayers = numPlayers;
        this.hero = new Player(hero);
        this.knownBoard = new Board(knownBoard);
    }

    @Override
    public void run(){
        for(int i = 0; i < this.numSims; i++){
            int numLoss = 0;
            int numTie = 0;

            Monte monte = new Monte(this.numPlayers, this.hero, this.knownBoard);

            monte.finishBoard();

            HandEval playerEval = new HandEval(monte.getBoard(), this.hero);
            HandValue playerVal = playerEval.handEval();

            for(int j = 0; j < monte.getPlayers().size() - 1; j++){
                HandEval oppEval = new HandEval(monte.getBoard(), monte.getPlayers().get(j+1));
                HandValue oppVal = oppEval.handEval();

                if(playerVal.getHandRank().getValue() < oppVal.getHandRank().getValue()){
                    numLoss++;
                    break;
                } else if(playerVal.getHandRank().getValue() == oppVal.getHandRank().getValue()){
                    List<Card> playerTie = playerVal.getTieBreakers();
                    List<Card> oppTie = oppVal.getTieBreakers();

                    for(int k = 0; k < oppTie.size(); k++){
                        if(playerTie.get(k).getRank().getVal() > oppTie.get(k).getRank().getVal()){
                            break;
                        } else if(playerTie.get(k).getRank().getVal() < oppTie.get(k).getRank().getVal()){
                            numLoss++;
                            break;
                        } else if(k == oppTie.size()-1){
                            numTie++;
                            break;
                        }
                    }

                    if(numLoss != 0){
                        break;
                    }
                }
            }

            if(numLoss != 0){
                this.loss++;
            } else if(numTie != 0){
                this.tie++;
            } else {
                this.wins++;
            }
        }
    }

    public int getLosses(){
        return this.loss;
    }

    public int getWins(){
        return this.wins;
    }

    public int getTies(){
        return this.tie;
    }
}