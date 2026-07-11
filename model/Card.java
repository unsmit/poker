public Card{
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

    public bool equals(Card c){
        if(c.getRank() == this.getRank()){
            return true;
        }
        return false;
    }

    public String toString(){
        return "" + this.getRank() + " of " + this.getSuit "S";
    }

    
}