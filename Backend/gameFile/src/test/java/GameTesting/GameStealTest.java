//package GameTesting;
//
//import database.Game.Game;
//import database.Game.Player;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//    public class GameStealTest {
//
//        @Test
//        public void testStealActionTransfersCoinsCorrectly() {
//            List<Player> players = new ArrayList<>(); // Create an Array list of Players
//            Game game = new Game(players); //Pass in Deck and Array List
//            game.initGame("player1@example.com", "player2@example.com", "player3@example.com", "player4@example.com");
//
//            // Set up a scenario where the first player can steal from the second player
//            Player thief = game.getCurrentPlayer();
//            Player victim = game.getPlayer("player2@example.com"); // Adjust according to actual method to get player by email
//
//            // Ensure victim has a specific amount of coins to test different scenarios
//            victim.setCoins(2); // Victim has 2 coins, adjust this value to test different scenarios (e.g., 1 or 0 coins)
//
//            // Record the starting coins of both the thief and the victim
//            int thiefStartingCoins = thief.getCoins();
//            int victimStartingCoins = victim.getCoins();
//
//            // Perform the "Steal" action
//            thief.action("Steal", victim);
//
//            // Assert the correct amount of coins has been transferred
//            assertEquals(Math.min(2, victimStartingCoins), thief.getCoins() - thiefStartingCoins, "Thief should gain at most 2 coins");
//            assertEquals(victimStartingCoins - Math.min(2, victimStartingCoins), victim.getCoins(), "Victim should lose at most 2 coins");
//        }
//
//
//    }
//
//
