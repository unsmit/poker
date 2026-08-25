package main;

import eval.*;
import model.*;

public class Main {
    public static void main(String[] args) {

        // -------------------------
        // Create players
        // -------------------------
        Player player1 = new Player();
        Player player2 = new Player();

        player1.addCard(new Card(Rank.ACE, Suit.SPADE));
        player1.addCard(new Card(Rank.ACE, Suit.HEART));

        player2.addCard(new Card(Rank.KING, Suit.SPADE));
        player2.addCard(new Card(Rank.KING, Suit.HEART));

        // -------------------------
        // Create board
        // -------------------------
        Board board = new Board();

        board.addCard(new Card(Rank.TWO, Suit.CLUB));
        board.addCard(new Card(Rank.SEVEN, Suit.DIAMOND));
        board.addCard(new Card(Rank.NINE, Suit.HEART));
        board.addCard(new Card(Rank.JACK, Suit.SPADE));
        board.addCard(new Card(Rank.THREE, Suit.CLUB));

        // -------------------------
        // Print cards
        // -------------------------
        System.out.println("Player 1 Cards:");
        System.out.println(player1.getPlayerCards());

        System.out.println("\nPlayer 2 Cards:");
        System.out.println(player2.getPlayerCards());

        System.out.println("\nBoard:");
        System.out.println(board.getCommunityCards());

        // -------------------------
        // Evaluate hands
        // -------------------------
        HandEval eval1 = new HandEval(board, player1);
        HandEval eval2 = new HandEval(board, player2);

        HandValue value1 = eval1.handEval();
        HandValue value2 = eval2.handEval();

        // -------------------------
        // Print evaluations
        // -------------------------
        System.out.println("\nPlayer 1 Hand:");
        System.out.println(value1.getHandRank());

        System.out.println("Player 1 Used Cards / Tiebreakers:");
        System.out.println(value1.getTieBreakers());

        System.out.println("\nPlayer 2 Hand:");
        System.out.println(value2.getHandRank());

        System.out.println("Player 2 Used Cards / Tiebreakers:");
        System.out.println(value2.getTieBreakers());

        // -------------------------
        // Compare hands
        // -------------------------
        HandComp comp = new HandComp();

        int result = comp.compare(value1, value2);

        System.out.println("\nResult:");

        if(result == 1){
            System.out.println("Player 1 wins!");
        } else if(result == -1){
            System.out.println("Player 2 wins!");
        } else {
            System.out.println("Tie!");
        }
    }
}