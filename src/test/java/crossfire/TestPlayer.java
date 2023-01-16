package crossfire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {

    private PlayerBuilder playerBuilder;

    @BeforeEach
    public void resetPlayerBuilder() {
        playerBuilder = new PlayerBuilder();
    }

    private Player createPlayer() {
        return playerBuilder.deck(Card.Street_Smarts(0), Card.Street_Smarts(1)).build();
    }

    @Test
    public void cleanUpRemovesPlayedCards() {
        Player p = playerBuilder.cardsInPlay(Card.Street_Smarts(0), Card.Street_Smarts(1)).build();
        p.cleanUp();
        assertEquals(Collections.emptyList(), p.cardsInPlay());
    }

    @Test
    public void cleanUpPutsCardsOntoDiscardPile() {
        List<Card> playedCards = List.of(Card.Street_Smarts(33));
        Player p = playerBuilder.cardsInPlay(playedCards).build();
        p.cleanUp();
        assertEquals(
                playedCards,
                p.discardPile()
        );
    }

    @Test
    public void canGetDeckSizeOf2() {
        Player p = createPlayer();
        assertEquals(2, p.deckSize());
    }

    @Test
    public void canGetDeckSizeOf1() {
        Player p = playerBuilder.deck(
                Card.Street_Smarts(0)
        ).build();
        assertEquals(1, p.deckSize());
    }

    @Test
    public void drawingCardReducesDeckSize() {
        Player p = playerBuilder.deck(
                Card.Street_Smarts(0),
                Card.Street_Smarts(1)
        ).build();
        p.draw();
        assertEquals(1, p.deckSize());
    }

    @Test
    public void drawingCardPutsTopOfDeckIntoHand() {
        Player p = playerBuilder.deck(
                Card.Street_Smarts(55)
        ).build();
        p.draw();
        assertEquals(Card.Street_Smarts(55), p.hand().get(0));
    }

    @Test
    public void drawingCardFromEmptyDeckPutsDiscardOntoDeckAndDraws() {
        Player p = playerBuilder.discardPile(Card.Street_Smarts(314)).build();
        p.draw();
        assertEquals(List.of(Card.Street_Smarts(314)), p.hand());
    }

    @Test
    public void cleanUpRemovesClearedObstacle() {
        Obstacle cleared = new ClearedObstacle();
        Obstacle uncleared = new UnclearedObstacle();
        var p = playerBuilder.obstacles(cleared, uncleared, cleared).build();
        p.cleanUp();
        assertEquals(List.of(uncleared), p.obstaclesInPlay());
    }

    @Test
    public void playingCardRemovesItFromHandIfPresent() {
        Player p = playerBuilder.hand(Card.Street_Smarts(0)).build();
        p.play(0);
        assertEquals(0, p.hand().size());
    }

    @Test
    public void playingTwoCardsPutsBothIntoPlay() {
        Player p = playerBuilder.hand(Card.Street_Smarts(0), Card.Street_Smarts(1)).build();
        var first = p.hand().get(0);
        var second = p.hand().get(1);
        p.play(first);
        p.play(second);
        assertEquals(List.of(first, second), p.cardsInPlay());
    }

    @Test
    public void twoDrawnCardsAreDifferent() {
        var p = playerBuilder.deck(Card.Street_Smarts(0), Card.Street_Smarts(1)).build();
        p.draw();
        p.draw();
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
