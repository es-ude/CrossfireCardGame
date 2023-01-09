package crossfire;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private List<Card> hand = new LinkedList<>();
    private List<Card> cardsInPlay = new LinkedList<>();
    private List<Obstacle> obstaclesInPlay = new LinkedList<>();
    private int nextCardId = 0;

    public void draw() {
        hand.add(new Card("Street Smarts", Damage.RED, nextCardId));
        nextCardId++;
    }

    public void play(Card played) {
        cardsInPlay.add(played);
        hand.remove(played);
    }

    public List<Card> cardsInPlay() {
        return List.copyOf(cardsInPlay);
    }

    public List<Card> hand() {
        return List.copyOf(hand);
    }

    public List<Obstacle> obstaclesInPlay() {
        return List.copyOf(obstaclesInPlay);
    }

    public void placeObstacle(Obstacle obstacle) {
        obstaclesInPlay.add(obstacle);
    }

    public void cleanUp() {
        cardsInPlay.clear();
        var clearedObstacles = obstaclesInPlay.stream().filter(Obstacle::cleared).toList();
        for (var obstacle : clearedObstacles) {
            obstaclesInPlay.remove(obstacle);
        }
    }
}
