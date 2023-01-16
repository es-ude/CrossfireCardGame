
# Cooperative Card Game

## 0 - Basic Specification 
After setup, the game is played over a series of turns.
The game is played by two players.
During setup, determine a starting player.
Additionally, each player starts with an obstacle in their play area.
Further each player starts the game without cards in hand.
Then players take alternating turns.
As the active player you take the following steps in order:

1. **play cards**: put zero or more cards from your hand into your play area
2. **assign damage**: assign damage from cards in play to any of the obstacles in play
3. **end**:
   - *draw*: add two new cards to your hand
   - *clean up*: remove played cards and cleared obstacles
   - *activate next player*

The players win as soon as all obstacles are cleared.
That means the players cannot lose.

#### Cards
Cards have a name and an amount of damage they can deal to
an obstacle. Two cards with the same name feature the same amount of damage.

#### Obstacles
Obstacles feature a track of damage levels.
An obstacle is cleared once all levels have been cleared.
A level is cleared once its preceding level is cleared and the specified
amount of damage has been assigned to it.

#### Design
```mermaid
classDiagram

class Player {
  cardsInPlay() List~Card~
  hand() List~Card~
  obstaclesInPlay() List~Obstacle~
  placeObstacle(Obstacle o)
  play(Card c)
  draw()
  cleanup()
}

class Game {
  players() List~Player~
  assign(Card c, Obstacle o)
  assignedCards() Map~Card_Obstacle~
  cardsInPlay() Map~Card_Player~
  obstaclesInPlay() Map~Obstacle_Player~
  play(Card c)
  endTurn()
}

class Obstacle {
  applyAssignedDamage()
  assignDamage(Damage damage)
  levels() List~Damage~
  cleared() boolean
  currentLevel() int
}

Game --> Player
UI --> Game
UI --> Player
UI --> Obstacle
UI --> Card
  
  
```

# Feature Additions

## 1 - Decks of Cards
- Each Player starts the game with a deck of 7 cards and 3 cards in hand
- At the end of turn, the active player draws two cards from her deck
- Each card in the game has a unique numeric id
- At the end of turn, cards played by a player are placed onto that players discard pile 
- If a player drew from an empty deck the discard pile (is shuffled and) forms a new deck that remaining cards are drawn from

The cards have the following attributes:

| Name          | Damage                                        |
|---------------|-----------------------------------------------|
| Street Smarts |  Red: 1, Black: 0, Blue: 0, Green: 0, Gray: 0 |

During implementation keep an eye out for the impact that these changes have on
your tests as well. Don't forget to treat the tests with the same care as the production
code. Thus the tests may be subject to refactoring and application of design
patterns just as well as the production code.

**Hint**: You will probably need to change
the constructor for `Player`, making it more
complicated. You can use a creational pattern to
ease creation of new players.


## 2 - Only Assign Damage with Cards in Play
Up to this stage you most probably have been presenting the available actions for the UI
in the main loop. Using a state machine the actions might look more or less like this:

```mermaid
stateDiagram
    [*] --> Turn
    state Turn {
      [*] --> selectAction
      selectAction --> selectCard: play card
      selectCard --> selectAction
      selectAction --> selectPlayedCard: assign damage
      selectPlayedCard --> selectObstacle
      selectObstacle --> selectAction
      selectAction --> [*]: end turn
    }
    Turn --> Turn: activate next player  
```

This way the UI will present meaningless actions to the user though.
With every iteration of the main loop the user can select between: **end turn**, **play card** and **assign damage**.
But at the start of the turn the **assign damage** action is useless, since
there are no played cards.
Only present the action **assign damage** to the user if there is at least a single
card in play.

Now we want to restrict the assign damage action to its own game phase as follows:

```mermaid
stateDiagram
  [*] --> Turn
  state Turn {
    [*] --> playCard
    playCard --> playCardWithCardsInPlay : play card
    playCardWithCardsInPlay --> playCardWithCardsInPlay: play card
    playCardWithCardsInPlay --> [*]: end turn
    playCard --> [*] : end turn
    playCardWithCardsInPlay --> assignDamage: assign damage
    assignDamage --> assignDamage: assign damage
    assignDamage --> [*] : end turn
  }
  Turn --> Turn : activateNextPlayer
```

If we changed the UI code to present only the meaningful actions we
would introduce a lot of knowledge about the game logic into the code
that should only responsible for passing user input to the `Game` and
presenting the current game state to the user. Use a simple behavioural
pattern to decouple the game logic from the UI. **Hint**: If this leads
to you game logic becoming cluttered with if-else/switch-case statements,
consider using a state pattern to make the code of the `Game` more expressive.