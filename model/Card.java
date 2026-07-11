public class Card{
    private Rank rank;
    private Suit suit;

    Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank(){
        return this.rank;
    }

    public Suit getSuit(){
        return this.suit;
    }

    public boolean equals(Card c){
        if(c.getRank() == this.getRank() && c.getSuit() == this.getSuit()){
            return true;
        }
        return false;
    }

    public String toString(){
        return "" + this.getRank() + " of " + this.getSuit() "S";
    }


}