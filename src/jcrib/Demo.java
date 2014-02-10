/*
Copyright (c) 2013, Matthew Malensek
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

This software is provided by the copyright holders and contributors "as is" and
any express or implied warranties, including, but not limited to, the implied
warranties of merchantability and fitness for a particular purpose are
disclaimed. In no event shall the copyright holder or contributors be liable for
any direct, indirect, incidental, special, exemplary, or consequential damages
(including, but not limited to, procurement of substitute goods or services;
loss of use, data, or profits; or business interruption) however caused and on
any theory of liability, whether in contract, strict liability, or tort
(including negligence or otherwise) arising in any way out of the use of this
software, even if advised of the possibility of such damage.
*/
package jcrib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcrib.cards.Card;
import jcrib.cards.Face;
import jcrib.cards.Hand;
import jcrib.cards.Suit;

/**
 * Demo Cribbage application that uses the library.
 *
 * @author malensek
 */
public class Demo {

    private static GameStateMachine game;
    private static BufferedReader commandLine;
    private static int stateToken = 0;

    public static void main(String[] args)
    throws Exception {

        /* Quiet the logger for the Demo */
        final Logger logger = Logger.getLogger("jcrib");
        logger.setLevel(Level.WARNING);

        /* Use fancy characters instead of just suit letters */
        Suit.enableFancyChars();

        InputStreamReader inputStream = new InputStreamReader(System.in);
        commandLine = new BufferedReader(inputStream);

        /* Get player details */
        String player1, player2;
        if (args.length >= 2) {
            player1 = args[0];
            player2 = args[1];
        } else {
            System.out.print("Player 1 Name: ");
            player1 = commandLine.readLine();
            System.out.print("Player 2 Name: ");
            player2 = commandLine.readLine();
        }
        System.out.println();
        System.out.println("Hello, " + player1 + " and " + player2 + "!");

        game = new GameStateMachine(new Player(player1), new Player(player2));

        /* Determine who goes first */
        cutDeck();
        System.out.println(game.getDealer().getName() + " deals first!");

        while (true) {
            /* Have players place cards into the crib */
            for (Player player : game.getPlayers()) {
                crib(player, 2);
            }

            System.out.println("Beginning play.");
            Card starter = game.getStarterCard();
            System.out.println("Starter Card: " + starter);
            if (starter.getFace() == Face.Jack) {
                System.out.println("Starter is a Jack.  Dealer scores 2 for "
                        + "his heels!");
            }

            while (true) {
                Player currentPlayer = game.getCurrentPlayer();
                Card[] hand = currentPlayer.getHand().toCardArray();
                Arrays.sort(hand);
                System.out.println("Turn: " + currentPlayer.getName() + " ("
                        + currentPlayer.getPoints() + ")");
                printHand(hand);

                /* Find the largest card that can be played */
                int maxCard = game.getLargestPlayableCard();
                int maxLength = 0;
                for (Card card : hand) {
                    if (card.getValue() > maxCard) {
                        break;
                    }
                    maxLength++;
                }

                int index = printPrompt(currentPlayer, 0, maxLength);
                int handIndex = currentPlayer.getHand().getIndex(hand[index]);
                Result result = game.executeAction(
                        currentPlayer, GameState.Play, stateToken, handIndex);

                if (result.hasScore()) {
                    for (Score score : result.getScores()) {
                        System.out.println(score);
                    }
                }

                if (result.stateChanged()) {
                    break;
                }

                printPlay();
            }

            System.out.println("Finished pegging:");
            for (Player player : game.getPlayers()) {
                System.out.println(player.getName() + " " + player.getPoints());
            }

            List<Score> cribScore = game.getCribScore();
            Map<String, List<Score>> handScores = game.getHandScores();

            for (String playerName : handScores.keySet()) {
                System.out.println(playerName + "'s hand score:");
                for (Score score : handScores.get(playerName)) {
                    System.out.println(score);
                }
                if (playerName.equals(game.getDealer().getName())) {
                    System.out.println("Crib score:");
                    for (Score score : cribScore) {
                        System.out.println(score);
                    }
                }
            }

            game.finishRound();

            System.out.println("Current score:");
            for (Player player : game.getPlayers()) {
                System.out.println(player.getName() + " " + player.getPoints());
            }
        }
    }

    public static void printPlay() {
        String play = " ";
        for (Card card : game.getCardsInPlay()) {
            play += card + " ";
        }
        System.out.println("[" + play + "]" + " - "
                + game.getCurrentPlayTotal());
    }

    public static int printPrompt(Player player) {
        return printPrompt(player, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int printPrompt(Player player, int min, int max) {
        System.out.print(player.getName() + "> ");
        String line = "";
        int num = 0;
        try {
            line = commandLine.readLine();
            num = Integer.parseInt(line);

            if (num < min || num >= max) {
                /* Try again */
                return printPrompt(player, min, max);
            }
        } catch (Exception e) {
            if (line.equals("quit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            System.out.println("Invalid number!");
            return printPrompt(player, min, max);
        }
        return num;
    }

    public static void printHand(Card[] hand) {
        System.out.print(" ");
        for (Card card : hand) {
            /* Add extra padding for 10s to keep spacing equal */
            if (card.getOrdinal() != 10) {
                System.out.print(" ");
            }

            System.out.print(card + " ");
        }
        System.out.println();

        for (int i = 0; i < hand.length; ++i) {
            System.out.print("  " + i + " ");
        }
        System.out.println();
    }

    public static void cutDeck() throws IllegalPlayException,
           InvalidStateException, InvalidStateTokenException {

        Result result = null;

        System.out.println("Please cut the deck (enter a number 0-51):");

        for (Player player : game.getPlayers()) {
            int card = printPrompt(player, 0, 52);
            result = game.executeAction(player,
                    GameState.Cut, stateToken, card);
            System.out.println(player.getName()
                    + " drew " + player.getCutCard());
        }

        if (!result.stateChanged()) {
            System.out.println("Tie!  Please cut the deck again.");
            cutDeck();
        }
    }

    public static void crib(Player player, int numCards)
    throws IllegalPlayException, InvalidStateException, 
                      InvalidStateTokenException {
        System.out.println("Move " + numCards + " cards to the crib.");
        for (int i = 0; i < numCards; ++i) {
            System.out.println(player.getName() + "'s cards:");
            Hand hand = player.getHand();
            Card[] cards = hand.toCardArray();
            Arrays.sort(cards);
            printHand(cards);
            int index = printPrompt(player, 0, hand.getCards().size());
            int handIndex = hand.getIndex(cards[index]);
            game.executeAction(player, GameState.Crib, stateToken, handIndex);
        }
    }
}
