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
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//public class GameCoupTest {
//    @Test
//    public void testCoupReducesInfluenceAndCoins() {
//        List<Player> players = new ArrayList<>(); // Create an Array list of Players
//        Game game = new Game(players); //Pass in Deck and Array List
//        game.initGame("player1@example.com", "player2@example.com", "player3@example.com", "player4@example.com");
//
//        // Set up a scenario where the first player can coup the second player
//        Player couper = game.getCurrentPlayer();
//        Player victim = game.getPlayer("player2@example.com");
//
//        // Ensure assassin has enough coins to perform assassination
//        couper.addCoins(5); // Assuming starting coins is 2, so add 5 more to meet the coup requirement
//
//        // Perform the "Assassinate" action
//        int coupStartingCoins = couper.getCoins();
//        int victimStartingLives = victim.getLives();
//        couper.action("Coup", victim);
//
//        // Assert the victim's influence is reduced by checking if one of the cards is null
//        assertTrue(victim.getCardOne() == null || victim.getCardTwo() == null, "Victim should lose one card (influence)");
//
//        // Assert the assassin's coins are reduced by 3
//        assertEquals(coupStartingCoins - 7, couper.getCoins(), "Assassin's coins should be reduced by 7");
//
//        // Optionally, assert the victim's lives are reduced
//        assertEquals(victimStartingLives - 1, victim.getLives(), "Victim's lives should be reduced by 1");
//
//    }
//}
