package crossfire;

import java.util.*;

public class PlayerBuilder {
    private List<Card> cardsInPlay = Collections.emptyList();
    private List<Card> discardPile = Collections.emptyList();
    private  List<Card> hand = Collections.emptyList();
    private List<Obstacle> obstaclesInPlay = Collections.emptyList();
    private Deque<Card> deck = new LinkedList<>();

    public PlayerBuilder cardsInPlay(List<Card> cards) {
        this.cardsInPlay = cards;
        return this;
    }

    private LinkedList<Card> collectCards(Card... cards) {
        return Arrays.stream(cards).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    public PlayerBuilder cardsInPlay(Card... cards) {
        this.cardsInPlay = collectCards(cards);
        return this;
    }

    public PlayerBuilder deck(List<Card> deck) {
        this.deck = new LinkedList<>(deck);
        return this;
    }

    public PlayerBuilder deck(Card... cards) {
        this.deck = collectCards(cards);
        return this;
    }


    public PlayerBuilder discardPile(Card... cards) {
        this.discardPile = collectCards(cards);
        return this;
    }

    public Player build() {
        return new Player(deck, hand, cardsInPlay, discardPile, obstaclesInPlay);
    }

    public PlayerBuilder hand(Card... cards) {
        this.hand = collectCards(cards);
        return this;
    }

    public PlayerBuilder obstacles(Obstacle... obstacles) {
        obstaclesInPlay = Arrays.asList(obstacles);
        return this;
    }
}
