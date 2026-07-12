package eval;

public enum HandRank{
    HIGH_CARD(0),
    ONE_PAIR(1),
    TWO_PAIR(2),
    TREE_OF_A_KIND(3),
    STRAIGHT(4),
    FLUSH(5),
    FULL_HOUSE(6),
    FOUR_OF_A_KIND(7),
    STRAIGHT_FLUSH(8);

    private final int value;

    HandRank(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}