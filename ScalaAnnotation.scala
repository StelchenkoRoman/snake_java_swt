class ScalaAnnotation {
  def convert(MySnake: Snake,what: String): String = {
    var retString:String=""
    what match {
      case "Start" =>
        retString+=beginString(MySnake)
      case "Direction" =>
        retString+=directionString(MySnake)
      case "Food" =>
        retString+=foodEaten(MySnake)
      case "End" =>
        retString+=endString(MySnake)
    }
    retString+="\n-------------------------------------------------"
    retString
  }
  def endString(MySnake: Snake):String= {
    var result: String = "\nGame over"  
    MySnake.wallInspection()
    MySnake.getLife() match {  
      case true =>{
        val mapInspection: Boolean = MySnake.getSnakeCoords(0).x>=560 || MySnake.getSnakeCoords(0).x<=0 ||MySnake.getSnakeCoords(0).y<=0 || MySnake.getSnakeCoords(0).y>=360
        mapInspection match{
          case false =>
            result+="\nSnake bump into itself"
          case true =>
            result+="\nSnake bump in the map border"   
         }
      }
      case false =>
        result+="\nSnake bump in the wall border"
    }
    result+="\nTotal points : "+MySnake.getPoints().toString()
    result
  }
  def beginString(MySnake: Snake):String= {
    var result: String = "Game is starting "
    result+= "\nName of player : " + MySnake.getPlayerName()
    result+= "\nLevel of game : "
    MySnake.getLevel()  match {
      case 0 =>
        result += "HARD"
      case 1 =>
        result += "NORMAL"
      case 2 =>
        result += "EASY"
    }
    result+="\nStart position snake : {" + MySnake.getSnakeCoords(0).x.toString() +";"+ MySnake.getSnakeCoords(0).y.toString()+"}"
    result+="\nStart position food  : {" + MySnake.getFoodCoords().x.toString() +";"+ MySnake.getFoodCoords().y.toString()+"}"   
    result
  }
  def directionString(MySnake: Snake):String= {
    var result: String = "\nSnake moves  "
    MySnake.getDirection()  match {
      case 1 =>
        result += directionXString(MySnake)
      case 2 =>
        result += directionYString(MySnake)
    }
    result+="\nHead position : {" + MySnake.getSnakeCoords(0).x.toString() +";"+ MySnake.getSnakeCoords(0).y.toString()+"}"
    result
  }
  def directionYString(MySnake: Snake):String= {
    MySnake.getDirectionValue.y  match {
      case 20 =>
        "Down"
      case -20 =>
        "Up"
    }
  }
  def directionXString(MySnake: Snake):String= {
    MySnake.getDirectionValue.x  match {
      case 20 =>
        "Right"
      case -20 =>
        "Left"
    }
  }
  def foodEaten(MySnake: Snake):String= {
    var result: String = "\nSnake eat the food "
    result+="\nNew position food  : {" + MySnake.getFoodCoords().x.toString() +";"+ MySnake.getFoodCoords().y.toString()+"}"   
    result+= "\nPoints : "+MySnake.getPoints().toString()
    result
  }
      
}