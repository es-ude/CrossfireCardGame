package crossfire;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Game {
    List<Player> players;
    int activePlayer = 0;
    Map<Card, Obstacle> assignedCards = new HashMap<>();

    public Game(List<Player> players) {
        this.players = players;
    }


    public void endTurn() {
        for (var player: players) {
            player.cleanUp();
        }
        players.get(activePlayer).draw();
        players.get(activePlayer).draw();
        activePlayer = (activePlayer + 1) % players.size();
        for (var entry : assignedCards.entrySet()) {
            var card = entry.getKey();
            var obstacle = entry.getValue();
            var damage = card.damage();
            obstacle.assign(damage);
        }
        for (var obstacles: new HashSet<>(assignedCards.values())) {
            obstacles.applyAssignedDamage();
        }
    }

    public Map<Card, Player> cardsInPlay() {
        return collectAcrossPlayers(Player::cardsInPlay);
    }

    public Player activePlayer() {
        return players.get(activePlayer);
    }

    public void assign(Card c, Obstacle o) {
        assignedCards.put(c, o);
    }

    public Map<Card, Obstacle> assignedCards() {
        return Map.copyOf(assignedCards);
    }

    private <T> Map<T, Player> collectAcrossPlayers(Function<Player, Iterable<T>> collector) {
        var collected  = new HashMap<T, Player>();
        for (var player: players) {
            for (var item: collector.apply(player)) {
                collected.put(item, player);
            }
        }
        return collected;
    }

    public Map<Obstacle, Player> obstaclesInPlay() {
        return collectAcrossPlayers(Player::obstaclesInPlay);
    }
}
