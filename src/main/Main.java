package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import model.Board;
import model.Card;
import model.Player;
import model.Rank;
import model.Suit;
import sim.Simulator;
import sim.Worker;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            runBenchmark(scanner);
        } catch (IllegalArgumentException exception) {
            System.err.println("Input error: " + exception.getMessage());
        }
    }

    private static void runBenchmark(Scanner scanner) {
        System.out.println("Poker simulation benchmark");

        Board board = new Board();
        List<Card> knownCards = new ArrayList<>();

        int boardSize = readInt(scanner, "Number of known board cards (0-5): ");
        if (boardSize < 0 || boardSize > 5) {
            throw new IllegalArgumentException("Board must contain between 0 and 5 cards");
        }

        for (int i = 0; i < boardSize; i++) {
            Card card = readCard(scanner, "Board card " + (i + 1) + ": ");
            addKnownCard(board, card, knownCards);
        }

        int numberOfPlayers = readInt(scanner, "Number of players (including hero): ");
        if (numberOfPlayers < 1) {
            throw new IllegalArgumentException("There must be at least one player");
        }

        Player hero = new Player();
        for (int i = 0; i < 2; i++) {
            Card card = readCard(scanner, "Hero card " + (i + 1) + ": ");
            if (knownCards.contains(card)) {
                throw new IllegalArgumentException("Duplicate card: " + cardCode(card));
            }
            knownCards.add(card);
            hero.addCard(card);
        }

        int requestedSimulations = readInt(scanner, "Number of simulations: ");
        if (requestedSimulations < 1) {
            throw new IllegalArgumentException("Number of simulations must be positive");
        }

        int threadCount = Runtime.getRuntime().availableProcessors();
        int simulations = normalizeSimulationCount(requestedSimulations, threadCount);
        validateCardAvailability(numberOfPlayers, boardSize, knownCards.size());

        Simulator multithreaded = new Simulator(
                simulations,
                numberOfPlayers,
                new Player(hero),
                new Board(board)
        );

        long multithreadedStart = System.nanoTime();
        multithreaded.simulate();
        long multithreadedNanos = System.nanoTime() - multithreadedStart;

        Worker singleThreaded = new Worker(
                simulations,
                numberOfPlayers,
                new Player(hero),
                new Board(board)
        );

        long singleThreadedStart = System.nanoTime();
        singleThreaded.run();
        long singleThreadedNanos = System.nanoTime() - singleThreadedStart;

        double multithreadedMillis = multithreadedNanos / 1_000_000.0;
        double singleThreadedMillis = singleThreadedNanos / 1_000_000.0;
        double percentageFaster =
                (singleThreadedNanos - multithreadedNanos)
                        * 100.0 / singleThreadedNanos;

        System.out.println();
        System.out.println("Simulations per benchmark: " + simulations);
        System.out.printf("Multithreaded time: %.3f ms%n", multithreadedMillis);
        System.out.printf("Single-threaded time: %.3f ms%n", singleThreadedMillis);
        System.out.printf(
                "Multithreaded performance difference: %.2f%%%n",
                percentageFaster
        );
        System.out.println(
                "Multithreaded result: "
                        + multithreaded.getWins() + " wins, "
                        + multithreaded.getLosses() + " losses, "
                        + multithreaded.getTies() + " ties"
        );
        System.out.println(
                "Single-threaded result: "
                        + singleThreaded.getWins() + " wins, "
                        + singleThreaded.getLosses() + " losses, "
                        + singleThreaded.getTies() + " ties"
        );
    }

    private static int normalizeSimulationCount(int requested, int threadCount) {
        int simulations = requested - requested % threadCount;

        if (simulations == 0) {
            simulations = threadCount;
        }

        if (simulations != requested) {
            System.out.println(
                    "Using " + simulations
                            + " simulations so the multithreaded run distributes evenly."
            );
        }

        return simulations;
    }

    private static void validateCardAvailability(
            int numberOfPlayers,
            int boardSize,
            int knownCardCount
    ) {
        int cardsNeeded =
                2 * (numberOfPlayers - 1)
                        + 2 * (5 - boardSize);

        if (knownCardCount + cardsNeeded > 52) {
            throw new IllegalArgumentException(
                    "Too many players for the known cards and remaining deck"
            );
        }
    }

    private static void addKnownCard(
            Board board,
            Card card,
            List<Card> knownCards
    ) {
        if (knownCards.contains(card)) {
            throw new IllegalArgumentException("Duplicate card: " + cardCode(card));
        }
        knownCards.add(card);
        board.addCard(card);
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException("Expected a whole number");
        }
        return scanner.nextInt();
    }

    private static Card readCard(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Expected a card such as AS or TD");
        }
        return parseCard(scanner.next());
    }

    private static Card parseCard(String code) {
        if (code.length() != 2) {
            throw new IllegalArgumentException(
                    "Card must use two characters, such as AS or TD"
            );
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
            default:
                throw new IllegalArgumentException("Unknown rank: " + code);
        }

        Suit suit;
        switch (Character.toUpperCase(code.charAt(1))) {
            case 'C': suit = Suit.CLUB; break;
            case 'D': suit = Suit.DIAMOND; break;
            case 'H': suit = Suit.HEART; break;
            case 'S': suit = Suit.SPADE; break;
            default:
                throw new IllegalArgumentException("Unknown suit: " + code);
        }

        return new Card(rank, suit);
    }

    private static String cardCode(Card card) {
        String rank;
        switch (card.getRank()) {
            case TWO: rank = "2"; break;
            case THREE: rank = "3"; break;
            case FOUR: rank = "4"; break;
            case FIVE: rank = "5"; break;
            case SIX: rank = "6"; break;
            case SEVEN: rank = "7"; break;
            case EIGHT: rank = "8"; break;
            case NINE: rank = "9"; break;
            case TEN: rank = "T"; break;
            case JACK: rank = "J"; break;
            case QUEEN: rank = "Q"; break;
            case KING: rank = "K"; break;
            case ACE: rank = "A"; break;
            default: throw new IllegalStateException("Unknown rank");
        }

        String suit;
        switch (card.getSuit()) {
            case CLUB: suit = "C"; break;
            case DIAMOND: suit = "D"; break;
            case HEART: suit = "H"; break;
            case SPADE: suit = "S"; break;
            default: throw new IllegalStateException("Unknown suit");
        }

        return rank + suit;
    }
}
