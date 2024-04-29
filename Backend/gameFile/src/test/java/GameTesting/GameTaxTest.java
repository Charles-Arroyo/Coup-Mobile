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
//public class GameTaxTest {
//
//        @Test
//        public void testIncomeFeatureIncreasesCoinsByThreeForEachPlayer() {
//            // Initialize your game and players
//            List<Player> players = new ArrayList<>(); // Create an Array list of Players
//            Game game = new Game(players); //Pass in Deck and Array List
//            game.initGame("player1@example.com", "player2@example.com", "player3@example.com", "player4@example.com");
//
//            // Perform the "Income" action for each player and assert the coin increase
//            for (int i = 0; i < players.size(); i++) {
//                int currentPlayerCoins = game.getCurrentPlayer().getCoins();
//                game.getCurrentPlayer().action("Tax", game.getCurrentPlayer());
//                assertEquals(currentPlayerCoins + 3,game.getCurrentPlayer().getCoins());
//                game.nextTurn(); // Move to the next player
//            }
//        }
//
//}
