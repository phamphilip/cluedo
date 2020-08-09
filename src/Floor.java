/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5092.1e2e91fc6 modeling language!*/


import java.awt.*;

// line 31 "model.ump"
// line 107 "model.ump"
public class Floor extends Location
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Floor(Item aItem, Point point)
  {
    super(aItem, point);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

  public String toString() {
    if (this.getItem() != null) return this.getItem().getName();
    return "_";
  }

}