package sim;

import model.*;
import java.util.ArrayList;

public class Monte {
    private ArrayList<Player> players;
    private Deck deck;
    private Board board;

    public Monte(int numPlayers, Player hero, Board knownBoard){
        this.deck = new Deck();
        this.board = new Board(knownBoard);

        for(Card card: hero.getPlayerCards()){
            deck.removeCard(card);
        }

        for(Card card: knownBoard.getCommunityCards()){
            deck.removeCard(card);
        }

        deck.shuffle();

        players = new ArrayList<>();
        players.add(hero);

        for(int i = 0; i < numPlayers - 1; i++){
            Player opponent = new Player();

            opponent.addCard(deck.drawCard());
            opponent.addCard(deck.drawCard());

            players.add(opponent);
        }
    }

    public void finishBoard(){
        while(board.getCommunityCards().size() < 5){
            this.deck.burn();
            board.addCard(deck.drawCard());
        }
    }

    public ArrayList<Player> getPlayers(){
        return new ArrayList<Player>(this.players);
    }

    public Board getBoard(){
        return new Board(this.board);
    }
}