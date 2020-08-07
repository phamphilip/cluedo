/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5092.1e2e91fc6 modeling language!*/


import java.awt.*;
import java.util.*;
import java.util.List;

// line 41 "model.ump"
// line 117 "model.ump"
public class Player extends Item {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Player Associations
    private List<Card> cards;
    private int steps;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Player(String aName, Point aLocation) {
        super(aName, aLocation);
        cards = new ArrayList<Card>();
        steps = 0;
    }

    //------------------------
    // INTERFACE
    //------------------------
    public int getSteps() {
        return this.steps;
    }

    public void setSteps(int numOfSteps) {
        this.steps = numOfSteps;
    }

    public void move(Board board, int x, int y) {
        if (isValidMove(board, x, y)) {
            Point newPoint = new Point(this.getLocation().x + x, this.getLocation().y + y);
            board.getLocations()[this.getLocation().y][this.getLocation().x].setItem(null);
            this.setLocation(newPoint);
            board.getLocations()[this.getLocation().y][this.getLocation().x].setItem(this);
            this.steps--;
            System.out.print(board.toString());
        }
    }

    private boolean isValidMove(Board board, int x, int y) {
        int yPos = this.getLocation().y;
        int xPos = this.getLocation().x;
        if (yPos+y < 0 || yPos+y >= Board.boardHeight || xPos+x < 0 || xPos+x >= Board.boardWidth) return false;
        Location start = board.getLocations()[yPos][xPos];
        Location end = board.getLocations()[yPos + y][xPos + x];
        boolean valid = true;
        if (end.getItem() != null) {
            System.out.println("There is another object already on this tile.");
            return false;
        }
        if (end instanceof Wall) valid = false;
        if (start instanceof Floor && end instanceof Room) {
            if (!isValidMove(board, end, x)) valid = false;
        }
        if (end instanceof Floor && start instanceof Room) {
            if (!isValidMove(board, start, x)) valid = false;
        }
        if (!valid) System.out.println("There is a wall blocking your path, try a different direction.");
        return valid;
    }

    private boolean isValidMove(Board board, Location room, int x) {
        if (((Room) room).isADoor()) {
            if (x != 0 && (board.getLocations()[this.getLocation().y+1][this.getLocation().x+x] instanceof Floor
                    || board.getLocations()[this.getLocation().y-1][this.getLocation().x+x] instanceof Floor)) return false;
        } else return false;
        return true;
    }

    /* Code from template association_GetMany */
    public Card getCard(int index) {
        Card aCard = cards.get(index);
        return aCard;
    }

    public List<Card> getCards() {
        List<Card> newCards = Collections.unmodifiableList(cards);
        return newCards;
    }

    public int numberOfCards() {
        int number = cards.size();
        return number;
    }

    public boolean hasCards() {
        boolean has = cards.size() > 0;
        return has;
    }

    public int indexOfCard(Card aCard) {
        int index = cards.indexOf(aCard);
        return index;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfCards() {
        return 0;
    }

    /* Code from template association_AddUnidirectionalMany */
    public boolean addCard(Card aCard) {
        boolean wasAdded = false;
        if (cards.contains(aCard)) {
            return false;
        }
        cards.add(aCard);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeCard(Card aCard) {
        boolean wasRemoved = false;
        if (cards.contains(aCard)) {
            cards.remove(aCard);
            wasRemoved = true;
        }
        return wasRemoved;
    }

}