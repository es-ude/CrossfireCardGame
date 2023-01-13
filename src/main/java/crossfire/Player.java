package crossfire;

import java.util.*;

public class Player {
    private final List<Card> cardsInPlay;
    private final List<Card> discardPile;
    private final List<Card> hand = new LinkedList<>();
    private final List<Obstacle> obstaclesInPlay;
    private final Deque<Card> deck;

    public Player(List<Card> deck) {
        this((Deque<Card>) new LinkedList<>(deck));
    }

    public Player(Deque<Card> deck) {
        this(deck, Collections.emptyList());
    }


    private Player(Deque<Card> deck, List<Card> cardsInPlay) {
        this(deck, cardsInPlay, Collections.emptyList(), Collections.emptyList());
    }

    private Player(Deque<Card> deck,
                   List<Card> cardsInPlay,
                   List<Card> discardPile,
                   List<Obstacle> obstaclesInPlay) {
        this.deck = new LinkedList<>(deck);
        this.cardsInPlay = new LinkedList<>(cardsInPlay);
        this.obstaclesInPlay = new LinkedList<>(obstaclesInPlay);
        this.discardPile = new LinkedList<>(discardPile);
    }


    public void draw() {
        hand.add(this.deck.pop());
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
        discardPile.addAll(cardsInPlay);
        cardsInPlay.clear();
        var clearedObstacles = obstaclesInPlay.stream().filter(Obstacle::cleared).toList();
        for (var obstacle : clearedObstacles) {
            obstaclesInPlay.remove(obstacle);
        }
    }

    public int deckSize() {
        return deck.size();
    }

    public List<Card> discardPile() {
        return discardPile;
    }
}
