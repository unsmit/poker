package model;

public class Card{
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank(){
        return this.rank;
    }

    public Suit getSuit(){
        return this.suit;
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Card)) {
            return false;
        }

        Card c = (Card) obj;

        return c.getRank() == this.getRank() && c.getSuit() == this.getSuit();
    }

    @Override
    public String toString(){
        return "" + this.getRank() + " of " + this.getSuit() + "S";
    }
}