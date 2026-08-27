package sim;
import java.util.ArrayList;
import model.*;

public class Worker implements Runnable{
    private int numSims;
    private int numPlayers;
    private Player hero;
    private Board knownBoard;
    
    public Worker(int numSims, int numPlayers, Player hero, Board knownBoard){
        this.numSims = numSims;
        this.numPlayers = numPlayers;
        this.hero = new Player(hero);
        this.knownBoard = new Board(knownBoard);
    }

    @Override
    public void run(){
        for(int i = 0; i < this.numSims; i++){
            Monte monte = new Monte(this.numPlayers, this.hero, this.knownBoard);

            ArrayList<Card> fullHand = new ArrayList<>();
            
        }
    }
}