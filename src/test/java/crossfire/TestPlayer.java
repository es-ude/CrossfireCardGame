package crossfire;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestPlayer {
    /*
     * Tests:
     *  - cleanup removes all cleared obstacles
     *  - cleanup removes all played cards
     */

    @Test
    public void cleanUpRemovesPlayedCards() {
        Player p = new Player();
        Card c = new Card("my card", Damage.RED, 0);
        p.play(c);
        p.cleanUp();
        assertEquals(Collections.emptyList(), p.cardsInPlay());
    }

    @Test
    public void cleanUpRemovesClearedObstacle() {
        Player p = new Player();
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
        Player p = new Player();
        p.draw();
        p.play(p.hand().get(0));
        assertEquals(0, p.hand().size());
    }

    @Test
    public void playingTwoCardsPutsBothIntoPlay() {
        Player p = new Player();
        p.draw();
        p.draw();
        var first = p.hand().get(0);
        var second = p.hand().get(1);
        p.play(first);
        p.play(second);
        assertEquals(List.of(first, second), p.cardsInPlay());
    }

    @Test
    public void twoDrawnCardsAreDifferent() {
        var p = new Player();
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
