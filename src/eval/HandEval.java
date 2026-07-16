package eval;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import model.*;

public class HandEval {
    private ArrayList<Card> fullHand = new ArrayList<>();
    private Map<Rank, Integer> rankCounts = new EnumMap<>(Rank.class);

    public HandEval(Board board, Player player){
        fullHand.addAll(board.getCommunityCards());
        fullHand.addAll(player.getPlayerCards());

        for(Card card: fullHand){
            Rank rank = card.getRank();

            if(rankCounts.containsKey(rank)){
                rankCounts.put(rank, rankCounts.get(rank)+1);
            } else{
                rankCounts.put(rank, 1);
            }
        }
    }
    
    public boolean isFlush(){
        int heart = 0;
        int spade = 0;
        int club = 0;
        int diamond = 0;

        for(Card card: fullHand){
            switch(card.getSuit()){
                case SPADE:
                    spade++;
                    break;
                case HEART:
                    heart++;
                    break;
                case CLUB:
                    club++;
                    break;
                case DIAMOND:
                    diamond++;
                    break;
            }
        }

        return heart >= 5 || spade >= 5 || club >= 5 || diamond >= 5;

    }

    public boolean fourOfAKind(){
        return rankCounts.containsValue(4);
    }

    public boolean threeOfAKind(){
        return rankCounts.containsValue(3);
    }

    public boolean onePair(){
        return rankCounts.containsValue(2);
    }

    public boolean twoPair(){
        ArrayList<Rank> pairs = new ArrayList<>();
        for(Map.Entry<Rank, Integer> entry : rankCounts.entrySet()){
            if(entry.getValue() == 2){
                pairs.add(entry.getKey());
            }
        }

        return pairs.size() >= 2; 
    }

    public boolean isFullHouse(){
        int twoCount = 0;
        int threeCount = 0;

        for(Map.Entry<Rank, Integer> entry : rankCounts.entrySet()){
            if(entry.getValue() == 2){
                twoCount += 1;
            } else if(entry.getValue() == 3){
                threeCount += 1;
            }
        }

        return threeCount >= 1 && (twoCount >= 1 || threeCount >= 2);
    }

    public boolean isStraight(){
        return isStraight(fullHand);
    }

    private boolean isStraight(List<Card> cards){
        Map<Rank, Integer> cardCheck = new EnumMap<>(Rank.class);

        for(Card card: cards){
            Rank rank = card.getRank();

            if(cardCheck.containsKey(rank)){
                cardCheck.put(rank, cardCheck.get(rank)+1);
            } else{
                cardCheck.put(rank, 1);
            }
        }

        if(cardCheck.containsKey(Rank.ACE)){
            boolean frontStraight = cardCheck.containsKey(Rank.TWO) && 
                                    cardCheck.containsKey(Rank.THREE) &&
                                    cardCheck.containsKey(Rank.FOUR) &&
                                    cardCheck.containsKey(Rank.FIVE);

            if(frontStraight){
                return true;
            }
        }

        int curr = 0;
        int prev = -1;
        int straightCount = 0;

        for(Rank rank : cardCheck.keySet()){
            curr = rank.getVal();
            if(prev != -1 && curr == prev + 1){
                straightCount++;
                prev = curr;
            } else if(prev == -1){
                prev = curr;
                straightCount = 1;
            } else{
                straightCount = 1;
                prev = curr;
            }

            if(straightCount >= 5){
                return true;
            }
        }

        return false;
    }

    public boolean isStraightFlush(){
        return isStraightFlush(fullHand);
    }

    private boolean isStraightFlush(List<Card> l){
        ArrayList<Card> clubs = new ArrayList<>();
        ArrayList<Card> hearts = new ArrayList<>();
        ArrayList<Card> diamonds = new ArrayList<>();
        ArrayList<Card> spades = new ArrayList<>();

        for(Card card: l){
            switch (card.getSuit()){
                case CLUB:
                    clubs.add(card);
                    break;
                case SPADE:
                    spades.add(card);
                    break;
                case DIAMOND:
                    diamonds.add(card);
                    break;
                case HEART:
                    hearts.add(card);
                    break;
            }
        }

        return (clubs.size() >= 5 && isStraight(clubs)) || (spades.size() >= 5 && isStraight(spades)) || (diamonds.size() >= 5 && isStraight(diamonds)) || (hearts.size() >= 5 && isStraight(hearts));
    }

    public boolean isRoyalFlush(){
        ArrayList<Card> check = new ArrayList<>();

        for(Card card: fullHand){
            if(card.getRank().getVal() >= 10){
                check.add(card);
            }
        }
        
        return isStraightFlush(check);
    }
}