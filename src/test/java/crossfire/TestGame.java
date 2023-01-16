package crossfire;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGame {
    PlayerBuilder emptyPlayerBuilder = new PlayerBuilder();
    @Test
    public void canGetAMapOfCardsInPlayAssociatedWithPlayers() {
        Player p = emptyPlayerBuilder.build();
        Card first = new Card("card", Damage.RED, 0);
        Card second = new Card("second card", Damage.GREEN, 0);
        p.play(first);
        p.play(second);
        Game g = new Game(List.of(p));
        assertEquals(Map.of(first, p, second, p), g.cardsInPlay());
    }

    @Test
    public void canGetAMapOfObstaclesInPlayAssociatedWithPlayers() {
        Player p = emptyPlayerBuilder.build();
        var first = new ClearedObstacle();
        var second = new ClearedObstacle();
        p.placeObstacle(first);
        p.placeObstacle(second);
        Game g = new Game(List.of(p));
        assertEquals(Map.of(first, p, second, p), g.obstaclesInPlay());
    }

    @Test
    public void recordAssignedCards() {
        Game g = new Game(List.of(emptyPlayerBuilder.build()));
        var obstacle = new ClearedObstacle();
        var card = new Card("card", Damage.GREEN, 0);
        g.assign(card, obstacle);
        assertEquals(Map.of(card, obstacle), g.assignedCards());
    }

    @Test
    public void assignedCardsAreAppliedToObstaclesAtEndOfTurn() {

        var obstacle = new ClearableObstacle(2);
        Game g = new Game(List.of(
                new PlayerBuilder()
                        .deck(
                                Card.Street_Smarts(0),
                                Card.Street_Smarts(1)
                        )
                        .obstacles(obstacle)
                        .build()
        ));
        var first = new Card("card", Damage.GREEN, 0);
        var second = new Card("", Damage.GREEN, 1);
        g.assign(first, obstacle);
        g.assign(second, obstacle);
        g.endTurn();
        assertTrue(obstacle.cleared());
    }
}
