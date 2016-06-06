public class SortingJ {
  public static void SCsort(int[] score,String[] fileName,String[] playerName, int l, int r) {
    int i = l;
	int j = r;
	int x = score[(r + l)/2];
	while ( i <= j ) {
	  while ( score[i] > x ) {
	    i++;
	  }
	  while ( score[j] < x ) {
   	    j--;
	  }
	  if ( i <= j ) {
	    int temp = score[i];
	    score[i] = score[j];
	    score[j] = temp;
	    String temp1 = fileName[i];
	    fileName[i] = fileName[j];
	    fileName[j] = temp1;	
	    String temp2 = playerName[i];
	    playerName[i] = playerName[j];
	    playerName[j] = temp2;
   	    i++;
	    j--;
	  }
	}
	if (l < j) {
	  SCsort(score, fileName,playerName, l, j);
	}
	if (j < r) {
	  SCsort(score, fileName,playerName, i, r);
	}
  }
  public static void AZSort(int[] score,String[] fileName,String[] playerName, int l, int r) {
	int i = l;
	int j = r;
	String x = playerName[(r + l)/2];
	while (i <= j) {
	  while (playerName[i].compareTo(x)>0) {
		i++;
	  }
	  while (playerName[j].compareTo(x)<0) {
		j--;
	  }
	  if (i <= j) {
	    int temp = score[i];
		score[i] = score[j];
		score[j] = temp;
		String temp1 = fileName[i];
		fileName[i] = fileName[j];
		fileName[j] = temp1;
		String temp2 = playerName[i];
		playerName[i] = playerName[j];
		playerName[j] = temp2;
		i++;
		j--;
      }
	}
	if (l < j) {
	  AZSort(score, fileName,playerName, l, j);
	}
	if (j < r) {
	  AZSort(score, fileName,playerName, i, r);
	}
  }
  public static void sortStat(int[] score, int l, int r) {
	int i = l;
	int j = r;
	int x = score[(r + l)/2];
	while (i <= j) {
	  while (score[i] > x) {
	    i++;
   	  }
	  while (score[j] < x) {
	    j--;
      }
   	  if (i <= j) {
  	    int temp = score[i];
	    score[i] = score[j];
	    score[j] = temp;
	    i++;
   	    j--;
	  }
    }
	if (l < j) {
	  sortStat(score, l, j);
	}
	if (j < r) {
  	  sortStat(score, i, r);
	}
  }
}

