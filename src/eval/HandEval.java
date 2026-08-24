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

    private ArrayList<Card> sortHand(ArrayList<Card> hand){
        if(hand != null){
            hand.sort((c1, c2) ->
                    Integer.compare(c2.getRank().getVal(), c1.getRank().getVal()));
        }
        return hand;
    }

    private void addHighestKickers(ArrayList<Card> hand, int numberOfKickers,
            Rank... excludedRanks){
        ArrayList<Card> sortedHand = sortHand(new ArrayList<>(fullHand));
        ArrayList<Rank> usedKickerRanks = new ArrayList<>();

        for(Card card : sortedHand){
            boolean excluded = false;
            for(Rank rank : excludedRanks){
                if(card.getRank() == rank){
                    excluded = true;
                    break;
                }
            }

            if(!excluded && !usedKickerRanks.contains(card.getRank())){
                hand.add(card);
                usedKickerRanks.add(card.getRank());
                if(usedKickerRanks.size() == numberOfKickers){
                    return;
                }
            }
        }
    }

    public ArrayList<Card> checkCondition(int n){
        Rank highestRank = null;

        for(Map.Entry<Rank, Integer> entry : rankCounts.entrySet()){
            if(entry.getValue() == n &&
                    (highestRank == null || entry.getKey().getVal() > highestRank.getVal())){
                highestRank = entry.getKey();
            }
        }

        if(highestRank != null){
            ArrayList<Card> cards = new ArrayList<>();
            for(Card card : fullHand){
                if(card.getRank() == highestRank){
                    cards.add(card);
                }
            }
            return sortHand(cards);
        }
        return null;
    }
    
    public ArrayList<Card> isFlush(){
        int heart = 0;
        int spade = 0;
        int club = 0;
        int diamond = 0;

        ArrayList<Card> spadeCards = new ArrayList<>();
        ArrayList<Card> heartCards = new ArrayList<>();
        ArrayList<Card> diamondCards = new ArrayList<>();
        ArrayList<Card> clubCards = new ArrayList<>();

        for(Card card: fullHand){
            switch(card.getSuit()){
                case SPADE:
                    spade++;
                    spadeCards.add(card);
                    break;
                case HEART:
                    heart++;
                    heartCards.add(card);
                    break;
                case CLUB:
                    club++;
                    clubCards.add(card);
                    break;
                case DIAMOND:
                    diamond++;
                    diamondCards.add(card);
                    break;
            }
        }

        ArrayList<Card> flushCards = null;
        if(spade >= 5){
            flushCards = spadeCards;
        } else if(heart >= 5){
            flushCards = heartCards;
        } else if(club >= 5){
            flushCards = clubCards;
        } else if(diamond >= 5){
            flushCards = diamondCards;
        }

        if(flushCards != null){
            sortHand(flushCards);
            return new ArrayList<>(flushCards.subList(0, 5));
        }

        return null;

    }

    public ArrayList<Card> fourOfAKind(){
        ArrayList<Card> check = checkCondition(4);
        if(check != null){
            Rank fourRank = check.get(0).getRank();
            addHighestKickers(check, 1, fourRank);
            return check;
        }
        return null;
    }

    public ArrayList<Card> threeOfAKind(){
        ArrayList<Card> check = checkCondition(3);
        if(check != null){
            Rank threeRank = check.get(0).getRank();
            addHighestKickers(check, 2, threeRank);
            return check;
        }
        return null;
    }

    public ArrayList<Card> onePair(){
        ArrayList<Card> check = checkCondition(2);
        if(check != null){
            Rank pairRank = check.get(0).getRank();
            addHighestKickers(check, 3, pairRank);
            return check;
        }
        return null;
    }

    public ArrayList<Card> twoPair(){
        ArrayList<Rank> pairRanks = new ArrayList<>();

        for (Map.Entry<Rank, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() >= 2) {
                pairRanks.add(entry.getKey());
            }
        }

        pairRanks.sort((r1, r2) -> Integer.compare(r2.getVal(), r1.getVal()));

        ArrayList<Card> cards = new ArrayList<>();
        if(pairRanks.size() >= 2){
            int firstPairCount = 0;
            for(Card card : fullHand){
                if(card.getRank() == pairRanks.get(0) && firstPairCount < 2){
                    cards.add(card);
                    firstPairCount++;
                }
            }

            int secondPairCount = 0;
            for(Card card : fullHand){
                if(card.getRank() == pairRanks.get(1) && secondPairCount < 2){
                    cards.add(card);
                    secondPairCount++;
                }
            }
            addHighestKickers(cards, 1, pairRanks.get(0), pairRanks.get(1));
            return cards;
        }
        return null;
    }

    public ArrayList<Card> isFullHouse(){
        ArrayList<Rank> threeRanks = new ArrayList<>();
        ArrayList<Rank> pairRanks = new ArrayList<>();

        for(Map.Entry<Rank, Integer> entry : rankCounts.entrySet()){
            if(entry.getValue() >= 3){
                threeRanks.add(entry.getKey());
            } else if(entry.getValue() >= 2){
                pairRanks.add(entry.getKey());
            }
        }

        if(threeRanks.size() >= 1 && (threeRanks.size() >= 2 || pairRanks.size() >=1)){
            ArrayList<Card> fullSet = new ArrayList<>();

            threeRanks.sort((r1, r2) -> Integer.compare(r2.getVal(), r1.getVal()));
            Rank threeRank = threeRanks.get(0);

            for(int i = 1; i < threeRanks.size(); i++){
                pairRanks.add(threeRanks.get(i));
            }
            pairRanks.sort((r1, r2) -> Integer.compare(r2.getVal(), r1.getVal()));
            Rank pairRank = pairRanks.get(0);

            int threeCount = 0;
            for(Card card: fullHand){
                if(card.getRank() == threeRank && threeCount < 3){
                    fullSet.add(card);
                    threeCount++;
                }
            }

            int pairCount = 0;
            for(Card card: fullHand){
                if(card.getRank() == pairRank && pairCount < 2){
                    fullSet.add(card);
                    pairCount++;
                }
            }
            return fullSet;
        }

        return null;
    }

    public ArrayList<Card> isStraight(){
        return isStraight(fullHand);
    }

    private ArrayList<Card> isStraight(List<Card> cards){
        Map<Rank, Integer> cardCheck = new EnumMap<>(Rank.class);
        Map<Rank, Card> rankToCard = new EnumMap<>(Rank.class);
        ArrayList<Card> strSet = new ArrayList<>();
        ArrayList<Card> bestStraight = null;

        // Set up rankToCard
        for(Card card : cards){
                rankToCard.putIfAbsent(card.getRank(), card);
            }

        // Initialize the map with cards in hand
        for(Card card: cards){
            Rank rank = card.getRank();

            if(cardCheck.containsKey(rank)){
                cardCheck.put(rank, cardCheck.get(rank)+1);
            } else{
                cardCheck.put(rank, 1);
            }
        }

        int curr = 0;
        int prev = -1;
        int straightCount = 0;

        for(Rank rank : cardCheck.keySet()){
            curr = rank.getVal();
            if(prev != -1 && curr == prev + 1){
                strSet.add(rankToCard.get(rank));
                straightCount++;
                prev = curr;
            } else if(prev == -1){
                prev = curr;
                strSet.clear();
                strSet.add(rankToCard.get(rank));
                straightCount = 1;
            } else{
                strSet.clear();
                strSet.add(rankToCard.get(rank));
                straightCount = 1;
                prev = curr;
            }

            if(straightCount >= 5){
                bestStraight = new ArrayList<>(
                        strSet.subList(strSet.size() - 5, strSet.size()));
            }
        }

        if(bestStraight != null){
            return sortHand(bestStraight);
        }

        boolean frontStraight = false;

        if(cardCheck.containsKey(Rank.ACE)){
            frontStraight = cardCheck.containsKey(Rank.TWO) &&
                                    cardCheck.containsKey(Rank.THREE) &&
                                    cardCheck.containsKey(Rank.FOUR) &&
                                    cardCheck.containsKey(Rank.FIVE);
        }

        strSet.clear();

        if(frontStraight){
            strSet.add(rankToCard.get(Rank.FIVE));
            strSet.add(rankToCard.get(Rank.FOUR));
            strSet.add(rankToCard.get(Rank.THREE));
            strSet.add(rankToCard.get(Rank.TWO));
            strSet.add(rankToCard.get(Rank.ACE));

            return strSet;
        }


        return null;
    }

    public ArrayList<Card> isStraightFlush(){
        return isStraightFlush(fullHand);
    }

    private ArrayList<Card> isStraightFlush(List<Card> l){
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

        ArrayList<Card> straightFlush;
        if(clubs.size() >= 5 && (straightFlush = isStraight(clubs)) != null){
            return straightFlush;
        } else if(diamonds.size() >= 5 && (straightFlush = isStraight(diamonds)) != null){
            return straightFlush;
        } else if(hearts.size() >= 5 && (straightFlush = isStraight(hearts)) != null){
            return straightFlush;
        } else if(spades.size() >= 5 && (straightFlush = isStraight(spades)) != null){
            return straightFlush;
        }
        return null;
    }

    public ArrayList<Card> isRoyalFlush(){
        ArrayList<Card> check = new ArrayList<>();

        for(Card card: fullHand){
            if(card.getRank().getVal() >= 10){
                check.add(card);
            }
        }
        
        return isStraightFlush(check);
    }

    public HandValue handEval(){
        ArrayList<Card> tieBreakers = new ArrayList<>();
        ArrayList<Card> usedCards;

        if((usedCards = isRoyalFlush()) != null){
            return new HandValue(HandRank.ROYAL_FLUSH, new ArrayList<>());
        } else if((usedCards = isStraightFlush()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.STRAIGHT_FLUSH, tieBreakers);
        } else if((usedCards = fourOfAKind()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.FOUR_OF_A_KIND, tieBreakers);
        } else if((usedCards = isFullHouse()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.FULL_HOUSE, tieBreakers);
        } else if((usedCards = isFlush()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.FLUSH, tieBreakers);
        } else if((usedCards = isStraight()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.STRAIGHT, tieBreakers);
        } else if((usedCards = threeOfAKind()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.THREE_OF_A_KIND, tieBreakers);
        } else if((usedCards = twoPair()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.TWO_PAIR, tieBreakers);
        } else if((usedCards = onePair()) != null){
            tieBreakers = usedCards;
            return new HandValue(HandRank.ONE_PAIR, tieBreakers);
        }

        ArrayList<Card> highCards = sortHand(new ArrayList<>(fullHand));
 
        return new HandValue(HandRank.HIGH_CARD, new ArrayList<>(highCards.subList(0, 5)));

    }
}
