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
//public class GameTurnTest {
//
//    @Test
//    public void testNextTurnCyclesThroughPlayers() {
//        // Setup
//
//
//        List<Player> players = new ArrayList<>(); // Create an Array list of Players
//        Game game = new Game(players); //Pass in Deck and Array List
//        game.initGame("player1@example.com", "player2@example.com", "player3@example.com", "player4@example.com");
//
//        // Initial currentPlayer should be the first player
//        String initialPlayerEmail = game.getCurrentPlayer().getUserEmail();
//
//        // Test cycling through each player
//        for (int i = 1; i <= players.size(); i++) {
//            game.nextTurn();
//            String currentPlayerEmail = game.getCurrentPlayer().getUserEmail();
//            // Assert that the currentPlayer is correct for each turn
//            String p = players.get(i % players.size()).getUserEmail();
//            assertEquals(players.get(i % players.size()).getUserEmail(), currentPlayerEmail);
//        }
//
//        // Finally, ensure that after cycling through all players, we're back at the first player
//        // This call should cycle back to the first player
//        assertEquals(initialPlayerEmail, game.getCurrentPlayer().getUserEmail(), "The turn should cycle back to the first player after the last player's turn.");
//
//    }
//}
