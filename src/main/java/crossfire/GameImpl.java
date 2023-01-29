package crossfire;

import crossfire.actions.Action;
import crossfire.actions.AssignCard;
import crossfire.actions.EndTurn;
import crossfire.actions.PlayCard;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class GameImpl implements Game {
    List<Player> players;
    int activePlayer = 0;
    Map<Card, Obstacle> assignedCards = new HashMap<>();

    public GameImpl(List<Player> players) {
        this.players = players;
    }



    public List<Action> getActions() {
        List<Action> actions = new LinkedList<>();
        actions.add(new EndTurn(this));
        actions.addAll(collectAvailablePlayCardActions());
        actions.addAll(collectAvailableAssignCardActions());
        return actions;
    }

    private List<Action> collectAvailablePlayCardActions() {
        List<Action> actions = new LinkedList<>();
        for (var card: getActivePlayerInstance().hand()) {
            actions.add(new PlayCard(getActivePlayerInstance(), card));
        }
        return actions;
    }

    private List<Action> collectAvailableAssignCardActions() {
        List<Action> actions = new LinkedList<>();
        List<Obstacle> obstaclesInPlay = new LinkedList<>();
        for (var player: players) {
            obstaclesInPlay.addAll(player.obstaclesInPlay());
        }
        for (var cardInPlay: getActivePlayerInstance().cardsInPlay()) {
            for (var obstacleInPlay: obstaclesInPlay) {
                actions.add(new AssignCard(this, obstacleInPlay, cardInPlay));
            }
        }
        return actions;
    }

    private Player getActivePlayerInstance(){
        return players.get(activePlayer);
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

    public void saveGame(List<Integer> userInputs) throws IOException {

        File file = new File("saveGame.txt");
        FileWriter fw = new FileWriter(file);
        for (int input: userInputs) {
            fw.write(input + ",");
        }
        fw.close();

    }
}
