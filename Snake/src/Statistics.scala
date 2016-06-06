import java.util
import scala.collection.mutable.ListBuffer

import scala.collection.JavaConversions
import scala.collection.mutable.MutableList

class Statistics {
 def get(gameCount:util.ArrayList[Coords]): Array[Array[Int]] ={
   var retList :Array[Array[Int]] = Array.ofDim(28, 18)
     for(i <-0 to (gameCount.size()-1)) retList(gameCount.get(i).x/20-1)(gameCount.get(i).y/20-1)+=1  
     retList
 }
 def maxCount(fieldCounts:Array[Array[Int]] , maxCoords:Coords): Int ={
   var maxLines: ListBuffer[Int] = new ListBuffer[Int]() 
   var maxCoordsY: ListBuffer[Int]= new ListBuffer[Int]() 
   for(i<- 0 to 27) {
     maxCoordsY +=fieldCounts(i).zipWithIndex.max._2
     maxLines +=fieldCounts(i).zipWithIndex.max._1 
   }
   maxCoords.x = maxLines.zipWithIndex.max._2
   maxCoords.y = maxCoordsY(maxCoords.x)
  maxLines.zipWithIndex.max._1
}
}