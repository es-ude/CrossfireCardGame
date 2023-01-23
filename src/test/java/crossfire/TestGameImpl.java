package crossfire;

import crossfire.actions.Action;
import crossfire.actions.AssignCard;
import crossfire.actions.EndTurn;
import crossfire.actions.PlayCard;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGameImpl {
    PlayerBuilder emptyPlayerBuilder = new PlayerBuilder();
    @Test
    public void canGetAMapOfCardsInPlayAssociatedWithPlayers() {
        Player p = emptyPlayerBuilder.build();
        Card first = new Card("card", Damage.RED, 0);
        Card second = new Card("second card", Damage.GREEN, 0);
        p.play(first);
        p.play(second);
        GameImpl g = new GameImpl(List.of(p));
        assertEquals(Map.of(first, p, second, p), g.cardsInPlay());
    }

    @Test
    public void canGetAMapOfObstaclesInPlayAssociatedWithPlayers() {
        Player p = emptyPlayerBuilder.build();
        var first = new ClearedObstacle();
        var second = new ClearedObstacle();
        p.placeObstacle(first);
        p.placeObstacle(second);
        GameImpl g = new GameImpl(List.of(p));
        assertEquals(Map.of(first, p, second, p), g.obstaclesInPlay());
    }

    @Test
    public void recordAssignedCards() {
        GameImpl g = new GameImpl(List.of(emptyPlayerBuilder.build()));
        var obstacle = new ClearedObstacle();
        var card = new Card("card", Damage.GREEN, 0);
        g.assign(card, obstacle);
        assertEquals(Map.of(card, obstacle), g.assignedCards());
    }

    @Test
    public void assignedCardsAreAppliedToObstaclesAtEndOfTurn() {
        var obstacle = new ClearableObstacle(2);
        GameImpl g = new GameImpl(List.of(
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

    @Test
    public void withoutCardsInHandOnlyEndTurnIsAvailable() {
        GameImpl g = new GameImpl(List.of(
                new PlayerBuilder().build()
        ));
        List<Action> actions = List.of(new EndTurn(g));
        assertEquals(actions, g.getActions());
    }

    @Test
    public void getPlayCardAndEndTurnActionsWithCardInHand() {
        var p =  new PlayerBuilder().hand(Card.Street_Smarts(0)).build();
        GameImpl g = new GameImpl(List.of(
                p
        ));
        List<Action> actions = List.of(new EndTurn(g), new PlayCard(p, Card.Street_Smarts(0)));
        assertEquals(actions, g.getActions());
    }

    @Test
    public void getAssignActionsWithCardsAndObstaclesInPlay() {
        var obstacle = new ClearableObstacle(1);
        var p = new PlayerBuilder().cardsInPlay(Card.Street_Smarts(0)).obstacles(obstacle).build();
        GameImpl g = new GameImpl(List.of(p));
        List<Action> expected_actions = List.of(
                new EndTurn(g),
                new AssignCard(g, obstacle, Card.Street_Smarts(0)));
        assertEquals(expected_actions, g.getActions());
    }

    @Test
    public void getNoAssignActionWithoutObstaclesInPlay() {
        var p = new PlayerBuilder().cardsInPlay(Card.Street_Smarts(0)).build();
        GameImpl g = new GameImpl(List.of(p));
        List<Action> expected_actions = List.of(
                new EndTurn(g));
        assertEquals(expected_actions, g.getActions());
    }

    @Test
    public void assignActionsIncludeObstaclesFromAllPlayers() {
        var obstacle_one = new ClearableObstacle(1);
        var obstacle_two = new ClearableObstacle(1);
        var playerBuilder = new PlayerBuilder().obstacles(obstacle_two);
        var p2 = playerBuilder.build();
        var p1 = playerBuilder.cardsInPlay(Card.Street_Smarts(0)).obstacles(obstacle_one).build();
        var g = new GameImpl(List.of(p1, p2));
        assertEquals(List.of(
                new EndTurn(g),
                new AssignCard(g, obstacle_one, Card.Street_Smarts(0)),
                new AssignCard(g, obstacle_two, Card.Street_Smarts(0))
        ),
                g.getActions());
    }
}
