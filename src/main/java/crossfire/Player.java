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


    public Player draw() {
        if (deck.isEmpty()) {
            deck.addAll(discardPile);
            discardPile.clear();
        }
        hand.add(deck.pop());
        return this;
    }

    public Player draw(int numberOfCards) {
        for (; numberOfCards > 0; numberOfCards--) {
            this.draw();
        }
        return this;
    }

    public Player play(Card played) {
        cardsInPlay.add(played);
        hand.remove(played);
        return this;
    }

    public Player play(int handIndex) {
        var card = hand.remove(handIndex);
        cardsInPlay.add(card);
        return this;
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

    public Player cleanUp() {
        discardPile.addAll(cardsInPlay);
        cardsInPlay.clear();
        var clearedObstacles = obstaclesInPlay.stream().filter(Obstacle::cleared).toList();
        for (var obstacle : clearedObstacles) {
            obstaclesInPlay.remove(obstacle);
        }
        return this;
    }

    public int deckSize() {
        return deck.size();
    }

    public List<Card> discardPile() {
        return discardPile;
    }
}
