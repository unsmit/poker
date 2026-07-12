import java.util.ArrayList;

public class Board{
    private ArrayList<Card> communityCards = new ArrayList<>();

    public Board(){ }

    public void addCard(Card c){
        if(communityCards.size() >= 5){
            throw new IllegalStateException("Board already has 5 cards");
        }

        communityCards.add(c);
    }

    public void removeCard(Card c){
        if(!communityCards.remove(c)){
            throw new IllegalArgumentException("Card not in communityCards");
        }
    }

    public void clear(){
        communityCards.clear();
    }

    public ArrayList<Card> getCommunityCards(){
        return new ArrayList<>(communityCards);
    }
}