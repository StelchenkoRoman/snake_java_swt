/**
 * describes coordinate x and y class
 * @author green
 * @version 0.1
 */
public class Coords {   // класс с координатами x и y
/** Coordinate x. */
  public  int x;
  /** Coordinate y. */
  public  int y;
  Coords () {}
  Coords ( int xValue, int yValue ) {
		x = xValue;
		y = yValue;	  
  }
  /** this method set value x and y . */
  public void value( int xValue, int yValue ) {   //метод установки координат
	x = xValue;
	y = yValue;
  }
}
