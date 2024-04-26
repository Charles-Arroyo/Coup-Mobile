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
//public class GameAssassinateTest {
//
//    @Test
//    public void testAssassinateReducesInfluenceAndCoins() {
//        // Initialize your game and players
//        List<Player> players = new ArrayList<>(); // Create an Array list of Players
//        Game game = new Game(players); //Pass in Deck and Array List
//        game.initGame("player1@example.com", "player2@example.com", "player3@example.com", "player4@example.com");
//
//        // Set up a scenario where the first player can assassinate the second player
//        Player assassin = game.getCurrentPlayer();
//        Player victim = game.getPlayer("player2@example.com");
//
//        // Ensure assassin has enough coins to perform assassination
//        assassin.addCoins(1); // Assuming starting coins is 2, so add 3 more to meet the assassination requirement
//
//        // Perform the "Assassinate" action
//        int assassinStartingCoins = assassin.getCoins();
//        int victimStartingLives = victim.getLives();
//        assassin.action("Assassinate", victim);
//
//        // Assert the victim's influence is reduced by checking if one of the cards is null
//        assertTrue(victim.getCardOne() == null || victim.getCardTwo() == null, "Victim should lose one card (influence)");
//
//        // Assert the assassin's coins are reduced by 3
//        assertEquals(assassinStartingCoins - 3, assassin.getCoins(), "Assassin's coins should be reduced by 3");
//
//        // Optionally, assert the victim's lives are reduced
//        assertEquals(victimStartingLives - 1, victim.getLives(), "Victim's lives should be reduced by 1");
//    }
//}
