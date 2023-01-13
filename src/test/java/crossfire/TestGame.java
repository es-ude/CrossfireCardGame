package crossfire;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGame {
    private Player createEmptyDeckPlayer() {
        return new Player(Collections.emptyList());
    }

    private Player createPlayer() {
        return new Player(List.of(
                Card.Street_Smarts(0),
                Card.Street_Smarts(1),
                Card.Street_Smarts(2),
                Card.Street_Smarts(3)
        ));
    }
    @Test
    public void canGetAMapOfCardsInPlayAssociatedWithPlayers() {
        Player p = createEmptyDeckPlayer();
        Card first = new Card("card", Damage.RED, 0);
        Card second = new Card("second card", Damage.GREEN, 0);
        p.play(first);
        p.play(second);
        Game g = new Game(List.of(p));
        assertEquals(Map.of(first, p, second, p), g.cardsInPlay());
    }

    @Test
    public void canGetAMapOfObstaclesInPlayAssociatedWithPlayers() {
        Player p = createEmptyDeckPlayer();
        var first = new ClearedObstacle();
        var second = new ClearedObstacle();
        p.placeObstacle(first);
        p.placeObstacle(second);
        Game g = new Game(List.of(p));
        assertEquals(Map.of(first, p, second, p), g.obstaclesInPlay());
    }

    @Test
    public void recordAssignedCards() {
        Game g = new Game(List.of(createEmptyDeckPlayer()));
        var obstacle = new ClearedObstacle();
        var card = new Card("card", Damage.GREEN, 0);
        g.assign(card, obstacle);
        assertEquals(Map.of(card, obstacle), g.assignedCards());
    }

    @Test
    public void assignedCardsAreAppliedToObstaclesAtEndOfTurn() {
        Game g = new Game(List.of(createPlayer()));
        var obstacle = new ClearableObstacle(2);
        var first = new Card("card", Damage.GREEN, 0);
        var second = new Card("", Damage.GREEN, 1);
        g.assign(first, obstacle);
        g.assign(second, obstacle);
        g.endTurn();
        assertTrue(obstacle.cleared());
    }
}
