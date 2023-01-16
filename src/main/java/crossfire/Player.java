package crossfire;

import java.util.*;

public class Player {
    private final List<Card> cardsInPlay;
    private final List<Card> discardPile;
    private final List<Card> hand;
    private final List<Obstacle> obstaclesInPlay;
    private final Deque<Card> deck;



    public Player(Deque<Card> deck,
                   List<Card> hand,
                   List<Card> cardsInPlay,
                   List<Card> discardPile,
                   List<Obstacle> obstaclesInPlay) {
        this.deck = new LinkedList<>(deck);
        this.hand = new LinkedList<>(hand);
        this.cardsInPlay = new LinkedList<>(cardsInPlay);
        this.obstaclesInPlay = new LinkedList<>(obstaclesInPlay);
        this.discardPile = new LinkedList<>(discardPile);
    }


    public void draw() {
        if (deck.isEmpty()) {
            deck.addAll(discardPile);
            discardPile.clear();
        }
        hand.add(deck.pop());
    }

    public void play(Card played) {
        cardsInPlay.add(played);
        hand.remove(played);
    }

    public void play(int handIndex) {
        var card = hand.remove(handIndex);
        cardsInPlay.add(card);
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
