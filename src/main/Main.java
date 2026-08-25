package main;

import eval.HandComp;
import eval.HandEval;
import eval.HandRank;
import eval.HandValue;
import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Card;
import model.Deck;
import model.Player;
import model.Rank;
import model.Suit;

/** Dependency-free regression suite for the poker engine. */
public final class Main {
    private static int passed;
    private static int failed;

    private Main() { }

    public static void main(String[] args) {
        run("royal flush", () -> expectRank(HandRank.ROYAL_FLUSH,
                "AS", "KS", "QS", "JS", "TS", "2C", "3D"));
        run("straight flush", () -> expectRank(HandRank.STRAIGHT_FLUSH,
                "9H", "8H", "7H", "6H", "5H", "AC", "KD"));
        run("ace-low straight flush", () -> expectRank(HandRank.STRAIGHT_FLUSH,
                "AS", "2S", "3S", "4S", "5S", "KC", "QD"));
        run("four of a kind", () -> expectRank(HandRank.FOUR_OF_A_KIND,
                "AC", "AD", "AH", "AS", "KC", "2D", "3H"));
        run("full house from two triples", () -> expectRanks(
                evaluate("KC", "KD", "KH", "QC", "QD", "QH", "2S"),
                HandRank.FULL_HOUSE, 13, 13, 13, 12, 12));
        run("flush chooses best five", () -> expectRanks(
                evaluate("AH", "JH", "9H", "7H", "3H", "2H", "KS"),
                HandRank.FLUSH, 14, 11, 9, 7, 3));
        run("straight ignores duplicate ranks", () -> expectRanks(
                evaluate("9C", "8D", "7H", "6S", "5C", "5D", "AH"),
                HandRank.STRAIGHT, 9, 8, 7, 6, 5));
        run("ace-low straight", () -> expectRank(HandRank.STRAIGHT,
                "AC", "2D", "3H", "4S", "5C", "9D", "KH"));
        run("three of a kind", () -> expectRank(HandRank.THREE_OF_A_KIND,
                "QC", "QD", "QH", "AS", "KC", "7D", "2H"));
        run("three pair chooses top two and kicker", () -> expectRanks(
                evaluate("AC", "AD", "KC", "KD", "QC", "QD", "2S"),
                HandRank.TWO_PAIR, 14, 14, 13, 13, 12));
        run("one pair", () -> expectRank(HandRank.ONE_PAIR,
                "JC", "JD", "AH", "KS", "9C", "7D", "2H"));
        run("high card", () -> expectRanks(
                evaluate("AC", "KD", "JH", "9S", "7C", "4D", "2H"),
                HandRank.HIGH_CARD, 14, 13, 11, 9, 7));
        run("higher category wins", () -> expectComparison(1,
                evaluate("AC", "AD", "2H", "4S", "6C", "8D", "TH"),
                evaluate("KC", "QD", "JH", "9S", "7C", "4D", "2H")));
        run("kicker decides equal category", () -> expectComparison(1,
                evaluate("AC", "AD", "KH", "QS", "9C", "7D", "2H"),
                evaluate("AS", "AH", "KC", "QD", "8C", "7S", "2D")));
        run("equal hands tie", () -> expectComparison(0,
                evaluate("AC", "KD", "QH", "JS", "9C", "7D", "2H"),
                evaluate("AS", "KH", "QD", "JC", "9D", "7S", "2C")));
        run("wheel loses to six-high straight", () -> expectComparison(-1,
                evaluate("AC", "2D", "3H", "4S", "5C", "KD", "QH"),
                evaluate("2C", "3D", "4H", "5S", "6C", "AD", "KH")));
        run("board rejects sixth card", Main::testBoardLimit);
        run("player rejects third card", Main::testPlayerLimit);
        run("deck contains exactly 52 unique cards", Main::testDeck);

        System.out.printf("%nPoker engine checks: %d passed, %d failed.%n", passed, failed);
        if (failed > 0) {
            throw new IllegalStateException(failed + " poker engine check(s) failed");
        }
    }

    private static HandValue evaluate(String... codes) {
        if (codes.length != 7) {
            throw new IllegalArgumentException("An evaluation requires exactly seven cards");
        }
        Board board = new Board();
        for (int i = 0; i < 5; i++) {
            board.addCard(card(codes[i]));
        }
        Player player = new Player();
        player.addCard(card(codes[5]));
        player.addCard(card(codes[6]));
        return new HandEval(board, player).handEval();
    }

    private static Card card(String code) {
        if (code == null || code.length() != 2) {
            throw new IllegalArgumentException("Card code must look like AS or TD: " + code);
        }
        Rank rank;
        switch (Character.toUpperCase(code.charAt(0))) {
            case '2': rank = Rank.TWO; break;
            case '3': rank = Rank.THREE; break;
            case '4': rank = Rank.FOUR; break;
            case '5': rank = Rank.FIVE; break;
            case '6': rank = Rank.SIX; break;
            case '7': rank = Rank.SEVEN; break;
            case '8': rank = Rank.EIGHT; break;
            case '9': rank = Rank.NINE; break;
            case 'T': rank = Rank.TEN; break;
            case 'J': rank = Rank.JACK; break;
            case 'Q': rank = Rank.QUEEN; break;
            case 'K': rank = Rank.KING; break;
            case 'A': rank = Rank.ACE; break;
            default: throw new IllegalArgumentException("Unknown rank: " + code);
        }
        Suit suit;
        switch (Character.toUpperCase(code.charAt(1))) {
            case 'C': suit = Suit.CLUB; break;
            case 'D': suit = Suit.DIAMOND; break;
            case 'H': suit = Suit.HEART; break;
            case 'S': suit = Suit.SPADE; break;
            default: throw new IllegalArgumentException("Unknown suit: " + code);
        }
        return new Card(rank, suit);
    }

    private static void expectRank(HandRank expected, String... cards) {
        HandValue actual = evaluate(cards);
        check(actual.getHandRank() == expected,
                "expected " + expected + " but got " + actual.getHandRank());
    }

    private static void expectRanks(HandValue value, HandRank expectedRank, int... expected) {
        check(value.getHandRank() == expectedRank,
                "expected " + expectedRank + " but got " + value.getHandRank());
        List<Card> cards = value.getTieBreakers();
        check(cards.size() == expected.length,
                "expected " + expected.length + " tiebreakers but got " + cards.size());
        for (int i = 0; i < expected.length; i++) {
            check(cards.get(i).getRank().getVal() == expected[i],
                    "tiebreaker " + i + " should be " + expected[i]
                            + " but was " + cards.get(i).getRank().getVal());
        }
    }

    private static void expectComparison(int expected, HandValue first, HandValue second) {
        HandComp comparison = new HandComp();
        check(comparison.compare(first, second) == expected, "unexpected comparison result");
        check(comparison.compare(second, first) == -expected, "comparison is not symmetric");
    }

    private static void testBoardLimit() {
        Board board = new Board();
        for (String code : new String[] {"2C", "3D", "4H", "5S", "6C"}) {
            board.addCard(card(code));
        }
        expectThrows(IllegalStateException.class, () -> board.addCard(card("7D")));
    }

    private static void testPlayerLimit() {
        Player player = new Player();
        player.addCard(card("AC"));
        player.addCard(card("KD"));
        expectThrows(IllegalStateException.class, () -> player.addCard(card("QH")));
    }

    private static void testDeck() {
        Deck deck = new Deck();
        List<String> seen = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            Card next = deck.drawCard();
            String identity = next.getRank() + ":" + next.getSuit();
            check(!seen.contains(identity), "duplicate card in deck: " + next);
            seen.add(identity);
        }
        check(seen.size() == 52, "deck did not produce 52 unique cards");
        expectThrows(RuntimeException.class, deck::drawCard);
    }

    private static void expectThrows(Class<? extends Throwable> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable error) {
            check(type.isInstance(error),
                    "expected " + type.getSimpleName() + " but got "
                            + error.getClass().getSimpleName());
            return;
        }
        throw new AssertionError("expected " + type.getSimpleName() + " to be thrown");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS  " + name);
        } catch (Throwable error) {
            failed++;
            System.err.println("FAIL  " + name + " - " + error.getMessage());
        }
    }
}
