package crossfire;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {

    private Player createPlayer() {
        return new Player(List.of(
                Card.Street_Smarts(0),
                Card.Street_Smarts(1)
        ));
    }

    @Test
    public void cleanUpRemovesPlayedCards() {
        Player p = new Player(List.of(new Card("my card", Damage.RED, 0)))
                .draw().play(0).cleanUp();
        assertEquals(Collections.emptyList(), p.cardsInPlay());
    }

    @Test
    public void cleanUpPutsCardsOntoDiscardPile() {
        Player p = new Player(List.of(Card.Street_Smarts(33)))
                .draw().play(0)
                .cleanUp();
        assertEquals(
                List.of(Card.Street_Smarts(33)),
                p.discardPile()
        );
    }

    @Test
    public void canGetDeckSizeOf2() {
        Player p = new Player(List.of(
                Card.Street_Smarts(0),
                Card.Street_Smarts(1)
        ));
        assertEquals(2, p.deckSize());
    }

    @Test
    public void canGetDeckSizeOf1() {
        Player p = new Player(List.of(
                Card.Street_Smarts(0)
        ));
        assertEquals(1, p.deckSize());
    }

    @Test
    public void drawingCardReducesDeckSize() {
        Player p = new Player(List.of(
                Card.Street_Smarts(0),
                Card.Street_Smarts(1)
        )).draw();
        assertEquals(1, p.deckSize());
    }

    @Test
    public void drawingCardPutsTopOfDeckIntoHand() {
        Player p = new Player(List.of(
                Card.Street_Smarts(55)
        )).draw();
        assertEquals(Card.Street_Smarts(55), p.hand().get(0));
    }

    @Test
    public void drawingCardFromEmptyDeckPutsDiscardOntoDeckAndDraws() {
        Player p = new Player(List.of(Card.Street_Smarts(314)))
                .draw().play(0).cleanUp()
                .draw();
        assertEquals(List.of(Card.Street_Smarts(314)), p.hand());
    }

    @Test
    public void cleanUpRemovesClearedObstacle() {
        Player p = createPlayer();
        Obstacle cleared = new ClearedObstacle();
        Obstacle uncleared = new UnclearedObstacle();
        p.placeObstacle(cleared);
        p.placeObstacle(uncleared);
        p.placeObstacle(cleared);
        p.cleanUp();
        assertEquals(List.of(uncleared), p.obstaclesInPlay());
    }

    @Test
    public void playingCardRemovesItFromHandIfPresent() {
        Player p = createPlayer().draw().play(0);
        assertEquals(0, p.hand().size());
    }

    @Test
    public void playingTwoCardsPutsBothIntoPlay() {
        Player p = createPlayer().draw(2);
        var first = p.hand().get(0);
        var second = p.hand().get(1);
        p.play(first);
        p.play(second);
        assertEquals(List.of(first, second), p.cardsInPlay());
    }

    @Test
    public void twoDrawnCardsAreDifferent() {
        var p = createPlayer().draw().draw();
        assertNotEquals(p.hand().get(0), p.hand().get(1));
    }


    private static class UnclearedObstacle implements Obstacle {

        @Override
        public boolean cleared() {
            return false;
        }

        @Override
        public void assign(Damage damage) {

        }

        @Override
        public void applyAssignedDamage() {

        }

        @Override
        public List<Damage> levels() {
            return Collections.emptyList();
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public int currentLevel() {
            return 0;
        }
    }
}
