/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5092.1e2e91fc6 modeling language!*/


import java.awt.*;

// line 14 "model.ump"
// line 92 "model.ump"
public class Location {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Location Attributes
    private Point location;
    private Item item;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Location(Item item) {
        this.item = item;
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setLocation(Point aLocation) {
        boolean wasSet = false;
        location = aLocation;
        wasSet = true;
        return wasSet;
    }

    public boolean setItem(Item aItem) {
        boolean wasSet = false;
        item = aItem;
        wasSet = true;
        return wasSet;
    }

    public Point getLocation() {
        return location;
    }

    public Item getItem() {
        return item;
    }

    public void delete() {
    }

}