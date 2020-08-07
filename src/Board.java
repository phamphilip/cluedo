/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5092.1e2e91fc6 modeling language!*/


import java.lang.reflect.Array;
import java.util.*;

// line 7 "model.ump"
// line 84 "model.ump"
public class Board {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Board Associations
    private Location[][] locations;
    private List<Item> items;
    static int boardWidth = 24;
    static int boardHeight = 25;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Board(Location[][] tiles) {
        locations = tiles;
        items = new ArrayList<Item>();

    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetMany */
    public Location getLocation(int x, int y) {
        return locations[y][x];
    }

    public Location[][] getLocations() {
        return locations;
    }

    public int numberOfLocations() {
        return locations.length * locations[0].length;
    }

    public int indexOfLocation(Location aLocation) {
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (locations[y][x] == aLocation) return x + y;
            }
        }
        return -1;
    }

    /* Code from template association_GetMany */
    public Item getItem(int index) {
        return items.get(index);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int numberOfItems() {
        return items.size();
    }

    public boolean hasItems() {
        return items.size() > 0;
    }

    public int indexOfItem(Item aItem) {
        return items.indexOf(aItem);
    }

    public boolean addLocation(Location aLocation, int x, int y) {
        if (locations[y][x] == aLocation) return false;
        locations[y][x] = aLocation;
        return true;
    }

    public boolean removeLocation(Location aLocation, int x, int y) {
        boolean wasRemoved = false;
        if (locations[y][x] == aLocation) {
            locations[y][x] = null;
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddUnidirectionalMany */
    public boolean addItem(Item aItem) {
        if (items.contains(aItem)) return false;
        items.add(aItem);
        return true;
    }

    public boolean removeItem(Item aItem) {
        boolean wasRemoved = false;
        if (items.contains(aItem)) {
            items.remove(aItem);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    public void delete() {
        locations = new Location[boardHeight][boardWidth];
        items.clear();
    }

    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                boardString.append("|").append(locations[y][x].toString());
            }
            boardString.append("|\n");
        }
        return boardString.toString();
    }

}