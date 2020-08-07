/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5092.1e2e91fc6 modeling language!*/


import java.awt.*;

// line 36 "model.ump"
// line 112 "model.ump"
public class Room extends Location
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  private String name;
  private String id;
  private Boolean isDoor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(String name, String id, Item item, Boolean isDoor)
  {
    super(item);
    this.id = id;
    this.name = name;
    this.isDoor = isDoor;
  }

  public Boolean isADoor() {
    return isDoor;
  }

  public String getName() {
    return this.name;
  }

  public void delete()
  {
    super.delete();
  }

  public String toString() {
    if (this.getItem() != null) return this.getItem().getName();
    if (isDoor) return this.id.toUpperCase();
    return this.id;
  }

}