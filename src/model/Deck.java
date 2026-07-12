package model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck{
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> burnPile = new ArrayList<Card>();

    public Deck(){
        reset();
    }

    public void reset(){
        deck.clear();

        for(Suit suit: Suit.values()){
            for(Rank rank: Rank.values()){
                Card c = new Card(rank, suit);
                deck.add(c);
            }
        }
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Card drawCard(){
        Card c = deck.remove(deck.size() - 1);
        return c;
    }

    public void removeCard(Card c){
        if (!deck.remove(c)) {
            throw new IllegalArgumentException("Card not found in deck: " + c);
        }
    }

    public void burn(){
        burnPile.add(drawCard());
    }
}