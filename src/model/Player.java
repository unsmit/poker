package model;
import java.util.ArrayList;

public class Player{
    private ArrayList<Card> playerCards = new ArrayList<Card>(2);

    public Player(){ }

    public void addCard(Card card){
        if(playerCards.size() >= 2){
            throw new IllegalStateException("Player already has two cards");
        }

        playerCards.add(card);
    }

    public void removeCard(Card card){
        if(!playerCards.remove(card)){
            throw new IllegalArgumentException("Card not found in player cards");
        }
    }

    public void clearCards(){
        playerCards.clear();
    }

    public ArrayList<Card> getPlayerCards(){
        ArrayList<Card> p = new ArrayList<Card>(playerCards);
        return p;
    }
}