//package GameTesting;
//
//import database.Game.Game;
//import database.Game.Player;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class GameRevealCardTest {
//
//    private Game game;
//
//    @BeforeEach
//    public void setUp() {
//        List<Player> players = new ArrayList<>(); // Create an Array list of Players
//        game = new Game(players); //Pass in Deck and Array List
//        game.initGame("player1@example.com", "player2@example.com", "player3@example.com", "player4@example.com");
//    }
//
//    @Test
//    public void testIfPlayerhascard(){
//        Player currentPlayer = game.getCurrentPlayer();
//        Player bluffCaller = game.getPlayer("player2@example.com");
//        currentPlayer.setCardOne("Duke");
//        currentPlayer.setCardTwo("Captain");
//
//      String card =  currentPlayer.revealCard("Duke",currentPlayer);
//
//        assertEquals(card,currentPlayer.getCardOne());
//    }
//}
