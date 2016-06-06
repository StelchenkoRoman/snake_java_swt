class Sorting {
  def SCsort(fileName: Array[String],playerName: Array[String],score: Array[Int]) {
    def swap(a: Int, b: Int) {
      val fileTemp = fileName(a); 
      fileName(a) = fileName(b)
      fileName(b) = fileTemp
      val playerTemp = playerName(a); 
      playerName(a) = playerName(b)
      playerName(b) = playerTemp;
      val scoreTemp = score(a)
      score(a) = score(b)
      score(b) = scoreTemp
    }
  def quickSort(begin: Int, end: Int) {
    val temp = score((begin + end) / 2)
    var i = begin
    var j = end
    while (i <= j) { 
      while (score(i) > temp) {
        i += 1
      }
      while (score(j) < temp) {
        j -= 1
      }
      if (i <= j) {
        swap(i, j)
        i += 1
        j -= 1
      }
    }
    if (begin < j) quickSort(begin, j)
      if (j < end) quickSort(i, end)
  }
  quickSort(0, score.length-1)
  }
  def AZsort(fileName: Array[String],playerName: Array[String],score: Array[Int]) {
    def swap(a: Int, b: Int) {
      val fileTemp = fileName(a); 
      fileName(a) = fileName(b)
      fileName(b) = fileTemp 
      val playerTemp = playerName(a); 
      playerName(a) = playerName(b)
      playerName(b) = playerTemp;
      val scoreTemp = score(a)
      score(a) = score(b)
      score(b) = scoreTemp
    }
  def quickSort(begin: Int, end: Int) {
    val temp = playerName((begin + end) / 2)
    var i = begin
    var j = end
    while (i <= j) { 
      while (playerName(i) < temp) {
        i += 1
      }
      while (playerName(j) > temp) {
        j -= 1
      }
      if (i <= j) {
        swap(i, j)
        i += 1
        j -= 1
      }
    }
    if (begin < j) quickSort(begin, j)
      if (j < end) quickSort(i, end)
    }
  quickSort(0, score.length-1)
  }
}