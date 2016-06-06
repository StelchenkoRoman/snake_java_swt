import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.IIOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * this class keep SnakeSegment foodCordscoords,wallcoords
 * change and generate new Coords
 * @author green
 * @version 0.1
 */
public class Snake {
  private  Coords[] walls;
  private  Scanner repSc;
  private  FileChannel fSaveChannel;
  private  SeekableByteChannel fHelpChannel;
  private  ByteBuffer bufferRead;
  private  PrintWriter repFile;
  private  ArrayList<Coords> snakeCoords;      // список координат каждой ячейки змейки
  private  Coords foodCords;                   // координаты еды
  private  Coords directionValue;              // объект класса Coords хранящий текущее изменение координат ( x,y ) головы змейки
  private  final int keyUp = 16777217, keyDown = 16777218,    //коды  соотв-щие нажатым клавишам стрелок
	 	             keyRight = 16777220, keyLeft = 16777219;
  private  final int playerMode = 0 , botMode = 1, loadMode = 2, replayMode = 3; // моды
  private  int points;                         // количество набранных очков в игре
  private  int direction;                      // ось движения змейки ( 1- по горизонтали, 2 - по вертикали )
  private  int size, level, wallSize = 0;						   // длина змейки
  private  boolean life,endFileFlag = true;					   // переменная определяющая конец игры ( false - конец игры )
  public boolean keyFlag=true;
  private  int mode;
  private  String playerName,fileRepName,annotationString;
  private  Random random;                       // переменная для генерации рандомной позиции еды
  private  ScalaAnnotation annotation;
  
  Snake( ) {									   // конструктор Snake
	snakeCoords  =  new ArrayList<Coords>( );     // выделение памяти под список координат
	foodCords  =  new Coords( );				   // выделение памяти для координат еды
	directionValue  =  new Coords( );             // выделение памяти для переменной изменения координат головы змейка
	random  =  new Random( );	 				   // выделение памяти для переменной рандом
	walls = new Coords[66];
	annotation=new ScalaAnnotation();
  }
  /** this method get life flag */
  public boolean getLife( ) { 					// возврат переменной life
	return life;
  }
  /** this method get food coordinate */
  public Coords getFoodCoords( ) {
	return foodCords;
  }
  /** this method get change head snake */
  public Coords getDirectionValue( ) {		// возвращает текущее изменение координат головы змейки
    return directionValue;
  }
  /** this method get direction snake */
  public int getDirection( ) {            // возвращает текущеее направление движения змейкй
	return direction;
  }
  /** this method get points */
  public int getPoints( ) {				// возвращает кол-во набранных очков
	return points;
  }
  /** this method get size */
  public int getSize( ) {					// возвращает размер змейки
	return size;
  }
  public String getPlayerName() {
	  return playerName;
  }
  public int getLevel() {
	  return level;
  }
  /** this method get segment snake by index */
  public Coords getSnakeCoords( int index ) {// возвращает координаты элемента змейки с индексом index
	return snakeCoords.get( index );
  }
  /** this method set direction snake*/
  public void setDirection( int x ) {     // установка оси движения
	direction  =  x;
  }
  /** this method set change x direction head snake */
  public void setDirectionValueX( int x ) {  //установка измен-я координаты х
	directionValue.x  =  x;
  }
  /** this method set change y direction head snake*/
  public void setDirectionValueY( int y ) {  // установка изменнения координаты у
	directionValue.y  =  y;
  }
  /** this method increment points */
  public void setPoints( ) {   //увеличение кол-ва очков
	points++;
  }
  /** this method life flag */
  public void setLife( ) {  //установка флага life в false
	life  =  false;
  }
  /** this method set food coordinate */
  public void setFoodCoords( Coords c ) { //установка координаты еды
	foodCords  =  c;
  }
  /** this method set start vakue */
  public void startValue( int levelSel, int modeV,String name,String fileNameRep ) {	// метод со стартовыми настройки игры
	level = levelSel;   
	mode =modeV;
	life  =  true;
	playerName= name;
	endFileFlag = true;
	snakeCoords.clear( );			// очистка списка координат элементов змейки
	if ( mode == replayMode ) {
		replay(fileNameRep);
	} else {
	points  =  0;
    size  =  2;
    boolean flag;
    if ( random.nextInt( 2 )  ==  1 ) {  	// выбор оси движения
      direction  =  1;
      directionValue.x  =  20;
    }
    else {
      direction  =  2;
      directionValue.y  =  20;
	}
    setWallCoords();
	Coords tail,head  =  new Coords( );  		// для задания координат головы и хвоста змейки
	tail  =  new Coords( );
	if ( direction  ==  1 ) {
	  head.value( 40,20 );
	  tail.value( 20, 20 );
	} else {
		head.value( 20,40 );
		tail.value( 20, 20 );
	  }
	snakeCoords.add( head );
	snakeCoords.add( tail );
	while ( true ) {					// генерирование координат еды 
	  flag = true;
	  foodCords.x  =  ( 1+random.nextInt( 28 ) )*20;
      foodCords.y = ( 1+random.nextInt( 18 ) )*20;
	  if ( foodCords.x == head.x && foodCords.y == head.y )  // цикл выполяется пока координаты еды и головы змейки совпадают
	    continue;
	  for ( int i = 0; i<wallSize; i++ ) {      // сравнение координат еды с координатами дополнительных стенок карты
		if (  foodCords.x == walls[i].x && foodCords.y == walls[i].y ) {
	      flag = false;
		  break;
		}
	  }
	  if ( flag )
		  break;
	}
	File fName  =  new File( "files/names.txt" );
	int []levelNumbers=new int[3];
    try {
	    repSc = new Scanner(fName);
	    for(int i=0;i<3;i++)
	    levelNumbers[i]=repSc.nextInt();
	    repSc.close();
	}
	catch (IOException  e) { }
    levelNumbers[level]++;
   try {
		  repFile  =  new PrintWriter(fName.getAbsoluteFile());
		  for(int i=0;i<3;i++)
		  repFile.println(levelNumbers[i]);
		  repFile.close();
		}catch ( IOException ex ) {
		    throw new RuntimeException( ex );
		  }
   fileRepName="files/"+String.valueOf(level)+"_"+String.valueOf(levelNumbers[level]-1)+".txt";
   try {
	fSaveChannel = (FileChannel) Files.newByteChannel(Paths.get(fileRepName), StandardOpenOption.WRITE,
				  StandardOpenOption.CREATE);
	 ByteBuffer buffer = ByteBuffer.allocate(14+name.length()*2);
	  buffer.putInt(0);
	  for(int i=0;i<name.length();i++) {
	    buffer.putChar(name.toCharArray()[i]);
	  }
	  buffer.putChar('+');
	  buffer.putInt(foodCords.x);
	  buffer.putInt(foodCords.y);
	  buffer.flip();
	  fSaveChannel.write(buffer);
} catch (IOException e1) {
	e1.printStackTrace();
}
	}
  }
  /** this method change coords snake segments, and inspection сoncinence coords */
  public void animate( Label score ) { // метод обновления координат   
	if (mode == replayMode )
     replayAnimate(score);
    else {	
      int flag=0;
	  if ( snakeCoords.get( 0 ).x == foodCords.x && snakeCoords.get( 0 ).y == foodCords.y ) {// при совпадении координат еды с координатой головы змейки 		  	  
	    foodInspection(score);
	    flag=1;
	  }	  
	  try {
	    	 ByteBuffer buffer = ByteBuffer.allocate(8);
	 	    buffer.putInt(snakeCoords.get(0).x);
	 	   buffer.putInt(snakeCoords.get(0).y);	
	 	  buffer.flip();
	 	    fSaveChannel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  if(flag==1){
		  try {
		    	ByteBuffer buffer = ByteBuffer.allocate(8);
		 	    buffer.putInt(foodCords.x);
		 	    buffer.putInt(foodCords.y);
		 	    buffer.flip();
				fSaveChannel.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  flag=0;
	  }
		  for ( int i = size-1; i>0; i-- )    // координаты всех элементов змейки принимают значение предыдущих  элементов
	    snakeCoords.get( i ).value( snakeCoords.get( i-1 ).x, snakeCoords.get( i-1 ).y ); 
      if( mode == 1 )
    	botAnimate();
      else playerAnimate();
  	  for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
	    if ( snakeCoords.get( 0 ).x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
	      life = false;
	    }
      }
	  wallInspection();
	  }	  
  }
  /** this method react to key */
  public void keyPressed( int Code ) { // метод принимающий код нажатой клавиши
    if ( Code == keyRight && direction == 2 ) {  // если нажата клавиша вправо и движение змейки происходит по оси Y 
	  directionValue.x = 20;           // смещение координаты x  = 20  
	  direction = 1; 					 // движение происходит по оси X	
    }
	if ( Code == keyLeft && direction == 2 ) { // если нажата клавиша влево и движение змейки происходит по оси Y 
	  directionValue.x = -20;				// смещение координаты x  = -20  
	  direction = 1; 						// движение происходит по оси X
	}
	if ( Code == keyDown && direction == 1 ) { // если нажата клавиша вниз и движение змейки происходит по оси Х 
	  directionValue.y = 20;				// смещение координаты y  = 20  
	  direction = 2;  					// движение происходит по оси Y
  	}
	if ( Code == keyUp && direction == 1 )  {// если нажата клавиша вверх и движение змейки происходит по оси Х 
	  directionValue.y = -20;				// смещение координаты y  =  -20  
	  direction = 2; 						// движение происходит по оси Y
	}
  }
  /** this method set wall coords */
  public void setWallCoords() {  // в зависимости от выбранного уровня рисуется соот-щая карта в игре
	int i, j = 0;
	if ( level == 0 ) {
	  wallSize = 66;
	  for ( i = 0; i<21; i++, j+=2 ) {
        Coords temp = new Coords(), temp2 = new Coords();
        if ( i<7 ) {
    	  temp.x = 100 ;
    	  temp2.x = 260 ;
    	  temp.y = 160 + i*20;
          temp2.y = 160 + i*20;
        } else if ( i<14 ) {
        	temp.x = 320 ;
        	temp2.x = 480 ;
        	temp.y = 160 + (i-7)*20;
            temp2.y = 160 + (i-7)*20;
        } else {
        	temp.x = 180;
        	temp2.x = 400 ;
        	temp.y = 80 + (i-14)*20;
            temp2.y = 80 + (i-14)*20;
        }
    	walls[j] = temp;
    	walls[j+1] = temp2;
      }
	  for ( i = 0; i<7; i++, j+=2 ) {
	    Coords temp = new Coords(), temp2 = new Coords();
	  	temp.x = 120 + i*20;
	   	temp.y = 280;
	   	temp2.y = 280;
	   	temp2.x = 340 + i*20;
	   	walls[j] = temp;
	   	walls[j+1] = temp2;
	  }
	  for ( i = 0; i<10; i++, j++ ) {
	    Coords temp = new Coords();
	   	temp.x = 200 + i*20;
	   	temp.y = 80;
	   	walls[j] = temp;
	  }
	}
	if ( level == 1 ) {
	  wallSize = 26;
	  for ( i = 0; i < 9; i++, j+=2 ) {
		Coords temp = new Coords(), temp2 = new Coords();
	    temp.x = 140 + i*20 ;
		temp.y = 100;
		temp2.x = 280 + i*20 ;
		temp2.y = 300;
		walls[j] = temp;
		walls[j+1] = temp2;
	  }
	  for ( i = 0; i < 4; i++ , j+=2 ) {
		Coords temp = new Coords(), temp2 = new Coords();
		temp.x = 140 ;
		temp.y = 100 + i*20;
		temp2.x = 440 ;
		temp2.y = 240 + i*20;
		walls[j] = temp;
		walls[j+1] = temp2;
	  }
	}
    if ( level == 2){
      wallSize = 0;
    }
  }
  /** this method get wall size */
  public int getWallSize() {  // возвращет кол-во элементов стенок в карте
	return wallSize;
  }
  /** this method get wall coords by index*/
  public Coords getWallCoords( int index ) {  //возвращает координаты эл-та стенки с индексом index
    return walls[index];
  }
  /** this method inspection wall coods and head snake*/
  public void wallInspection() {  // проверка на совпадение головы змейки и стенок
	for ( int i = 0; i<wallSize; i++ )
	  if ( snakeCoords.get( 0 ).x == walls[i].x && snakeCoords.get( 0 ).y == walls[i].y )
	    life = false;
  }
  /** this method change direction bot  */
  public void botAnimate() {   // режим бота  
	if(direction ==1) {
	 if ( ( snakeCoords.get( 0 ).x+directionValue.x ) < 20 || ( snakeCoords.get( 0 ).x+directionValue.x ) > 560 ) {   
	   life = false;
	   return;
	 }
	 snakeCoords.get( 0 ).x +=  directionValue.x;
	 if ( snakeCoords.get( 0 ).x  == foodCords.x ||  (snakeCoords.get( 0 ).x < foodCords.x && directionValue.x==-20) || (snakeCoords.get( 0 ).x > foodCords.x && directionValue.x==20)  ) {
	   direction=2;
	 if (snakeCoords.get( 0 ).y  < foodCords.y )
	   directionValue.y=20;
	 else directionValue.y=-20;
	 if( snakeCoords.get( 0 ).y== 0)  {
  	   directionValue.y=20;
	 }
	 if (snakeCoords.get( 0 ).y== 360) {
		directionValue.y=-20;
	 }
     for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
       if ( snakeCoords.get( 0 ).x  == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y + directionValue.y == snakeCoords.get( i ).y )  {	          
	     direction=1;
	   }
     }
    }
   }
   else {
	 if ( ( snakeCoords.get( 0 ).y+directionValue.y ) < 20 || ( snakeCoords.get( 0 ).y+directionValue.y ) > 560 ) {   
       life = false;
	return;
  }
  snakeCoords.get( 0 ).y +=  directionValue.y;
  if( snakeCoords.get( 0 ).y  == foodCords.y || (snakeCoords.get( 0 ).y  < foodCords.y && directionValue.y==-20) ||(snakeCoords.get( 0 ).y  > foodCords.y && directionValue.y==20)) {	
	direction=1;
	if(snakeCoords.get( 0 ).x  < foodCords.x )
	  directionValue.x=20;
	else directionValue.x=-20;
	if ( snakeCoords.get( 0 ).x== 0) {
	  directionValue.x=20;
    }
    if(snakeCoords.get( 0 ).x== 560) {
  	  directionValue.x=-20;
    }
	for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
	if ( snakeCoords.get( 0 ).x + directionValue.x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
      direction=2;
	 }
    } 
   }
  }
  }
  /** reading from the file start settings last saved game*/
  public void foodInspection(Label score) {   // метод проверки на совпадение координат еды с частями змейки и стенками карты
	boolean flag;
	Coords c = new Coords( );
	c.value( snakeCoords.get( size-1 ).x, snakeCoords.get( size-1 ).y );
	snakeCoords.add( c );
	size++;          //увеличение размера змейки
	points++;			//увеличение кол-ва набранных очков
	if(score!=null)
	score.setText( "Score :  "+ points );  //вывод на экран кол-ва набраный очков
    while ( true ) { // цикл выполняющий генерирование новых коорднат еды
      flag = true;
      foodCords.x = ( 1+random.nextInt( 28 ) )*20;
      foodCords.y = ( 1+random.nextInt( 18 ) )*20;
      for ( int i = 0;i<size;i++ ) {
        if ( foodCords.x == snakeCoords.get( i ).x && foodCords.y == snakeCoords.get( i ).y ) {// цикл завершается когда координата еды не перекрывает ни один элем
          flag = false;
          break;
        }
      }
      if ( !flag )
        continue;
      for ( int i = 0; i<wallSize; i++ ) {     // сравнение координат еды с координатами дополнительных стенок карты
        if (  foodCords.x == walls[i].x && foodCords.y == walls[i].y ) {
    	  flag = false;
    	  break;
    	}
      }
      if ( flag )
        break;
    }
  }
  /** close replay file*/
  public void repFileClose() {
    try {  
	  fSaveChannel.position(0);
	  ByteBuffer buffer = ByteBuffer.allocate(4);
	  buffer.putInt(points);
	  buffer.flip();
	  fSaveChannel.write(buffer);
	  fSaveChannel.close();
    } catch (IOException e1) {
      } 
    try {
   	 fSaveChannel = (FileChannel) Files.newByteChannel(Paths.get("files/"+String.valueOf(level)+"_AllResults.txt"), StandardOpenOption.WRITE,
	 StandardOpenOption.APPEND,StandardOpenOption.CREATE	);
	 ByteBuffer buffer = ByteBuffer.allocate(playerName.length()*2+fileRepName.length()*2+10);
	 for(int i=0;i<fileRepName.length();i++) {
	   buffer.putChar(fileRepName.toCharArray()[i]);
	 }
	 buffer.putChar('+');
	 for(int i=0;i<playerName.length();i++) {
	   buffer.putChar(playerName.toCharArray()[i]);
	 }
	 buffer.putChar('+');
	 buffer.putInt(points);
	 buffer.putChar('\n');
	 buffer.flip();
	 fSaveChannel.write(buffer);
	 fSaveChannel.close();
   } catch (IOException e1) {
	   e1.printStackTrace();
	 }
  }
  /** animation in the replay mode*/
  public void replayAnimate(Label score) {
	  annotationString = annotation.convert( this,"Direction");
	  for ( int i = size-1; i>0; i-- )    // координаты всех элементов змейки принимают значение предыдущих  элементов
		  snakeCoords.get( i ).value( snakeCoords.get( i-1 ).x, snakeCoords.get( i-1 ).y );
	  
	if ( !bufferRead.hasRemaining() ) {
		endFileFlag = false;
		snakeCoords.get( 0 ).x += directionValue.x;
		snakeCoords.get( 0 ).y += directionValue.y;
		annotationString = annotation.convert( this,"End");
		return;
	  }
	 snakeCoords.get( 0 ).x = bufferRead.getInt();
	 snakeCoords.get( 0 ).y = bufferRead.getInt();
	 if(snakeCoords.get( 0 ).x > snakeCoords.get( 1 ).x) {
	   direction = 1;
	   directionValue.x = 20;
	 } 
	 if(snakeCoords.get( 0 ).x < snakeCoords.get( 1 ).x) {
		   direction = 1;
		   directionValue.x = -20;
	 } 
	 if(snakeCoords.get( 0 ).y > snakeCoords.get( 1 ).y) {
		   direction = 2;
		   directionValue.y = 20;
	 } 
	 if(snakeCoords.get( 0 ).y < snakeCoords.get( 1 ).y) {
			   direction = 2;
			   directionValue.y = -20;
	 } 
	 
	 if ( snakeCoords.get( 0 ).x == foodCords.x && snakeCoords.get( 0 ).y == foodCords.y ) {// при совпадении координат еды с координатой головы змейки 		  	  
		  points++;
		  Coords c = new Coords( );
		  c.value( snakeCoords.get( size-1 ).x, snakeCoords.get( size-1 ).y );
		  snakeCoords.add( c );
		
		  size++;
		  score.setText( "Score :  "+ points ); 
	      foodCords.x = bufferRead.getInt();
		  foodCords.y = bufferRead.getInt();
		  if ( !bufferRead.hasRemaining() ) {
				endFileFlag = false;
				return;
			  } 	
		  annotationString = annotation.convert( this,"Food");
		} 	
	for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
	  if ( snakeCoords.get( 0 ).x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
	    life = false;
  	  }
    }
    wallInspection();	
  }
  /** start settings in the replay mode*/
  public void replay(String FileNameRep) {   // считывание начальных координат в режиме "replay"
	try {	    
	  size=2;
	  points=0;
	  fHelpChannel = Files.newByteChannel(Paths.get(FileNameRep));
	  int fileSize = (int) fHelpChannel.size();
	  bufferRead = ByteBuffer.allocate(fileSize);	
	  fHelpChannel.read(bufferRead);
	  bufferRead.flip();
	  char temp;
	  playerName="";
	  int totalPoints=bufferRead.getInt();
	  while(true) {
		temp = bufferRead.getChar();
	    if(temp == '+')
	      break;
		playerName += String.valueOf(temp);
      }
	  Coords head = new Coords() ,tail =  new Coords( );
	  foodCords.x=bufferRead.getInt();
	  foodCords.y=bufferRead.getInt();
	  head.value(bufferRead.getInt(), bufferRead.getInt());
	  snakeCoords.add( head );
	  if ( head.y == 20 ) {
	    direction = 1;
	    directionValue.x = 20;
	    tail.value(head.x -20 , head.y);
	  } else {
	      direction = 2;
	      directionValue.y = 20;
		  tail.value(head.x, head.y-20);
	    }
		snakeCoords.add( tail );
	} catch (IOException e) {
    	e.printStackTrace();
      }
	annotationString = annotation.convert(this,"Start");
	
	setWallCoords();
  }
  /** animation in the real Player mode*/
  public void playerAnimate() {
	if ( direction  ==  1 ) { // если змейка движется по горизонтали  
      if ( ( snakeCoords.get( 0 ).x+directionValue.x ) < 20 || ( snakeCoords.get( 0 ).x+directionValue.x ) > 560 ) {   
		life = false;
		return;
      }
  	  snakeCoords.get( 0 ).x +=  directionValue.x; // координата х головы изменяется на значение  directionValue.x 	
    }
	if ( direction  ==  2 ) {
	  if ( ( snakeCoords.get( 0 ).y+directionValue.y ) < 20       || ( snakeCoords.get( 0 ).y+directionValue.y ) > 360 ) {
		life = false;
		return;
	  }
	  snakeCoords.get( 0 ).y +=  directionValue.y;
	}
  }
  /** return game mode*/
  public int getMode() {
	return mode;
  }
  /** return the state of the end of file*/
  public boolean getFileFlag() {
    return endFileFlag;
  }
  /**generate games*/
  public void genAnimate() {
	boolean flag;
	startValue( 2, botMode,"bot","");

	while(life) {
		if ( snakeCoords.get( 0 ).x == foodCords.x && snakeCoords.get( 0 ).y == foodCords.y ) {// при совпадении координат еды с координатой головы змейки 		  	  
	  Coords c = new Coords( );
	  c.value( snakeCoords.get( size-1 ).x, snakeCoords.get( size-1 ).y );
	  snakeCoords.add( c );
	  size++;          //увеличение размера змейки
	  points++;			//увеличение кол-ва набранных очков
	  while ( true ) { // цикл выполняющий генерирование новых коорднат еды
		flag = true;
		foodCords.x = ( 1+random.nextInt( 28 ) )*20;
		foodCords.y = ( 1+random.nextInt( 18 ) )*20;
		for ( int i = 0;i<size;i++ ) {
		  if ( foodCords.x == snakeCoords.get( i ).x && foodCords.y == snakeCoords.get( i ).y ) {// цикл завершается когда координата еды не перекрывает ни один элем
		    flag = false;
		    break;
		  }
		}
		if ( !flag )
		  continue;
		for ( int i = 0; i<wallSize; i++ ) {     // сравнение координат еды с координатами дополнительных стенок карты
		  if (  foodCords.x == walls[i].x && foodCords.y == walls[i].y ) {
		    flag = false;
		    break;
		  }
		}
		if ( flag )
		  break;
		}	
	    try {
		  ByteBuffer buffer = ByteBuffer.allocate(8);
		  buffer.putInt(snakeCoords.get(0).x);
		  buffer.putInt(snakeCoords.get(0).y);	
		  buffer.flip();
		  fSaveChannel.write(buffer);
		} catch (IOException e) {
		  e.printStackTrace();
		}
		if(flag){
		  try {
		  	ByteBuffer buffer = ByteBuffer.allocate(8);
		    buffer.putInt(foodCords.x);
		    buffer.putInt(foodCords.y);
		    buffer.flip();
		   fSaveChannel.write(buffer);
			} catch (IOException e) {
			e.printStackTrace();
			}
			 flag=false;
		  }
		for ( int i = size-1; i>0; i-- )    // координаты всех элементов змейки принимают значение предыдущих  элементов
		  snakeCoords.get( i ).value( snakeCoords.get( i-1 ).x, snakeCoords.get( i-1 ).y ); 
	    botAnimate();
	    for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
		  if ( snakeCoords.get( 0 ).x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
		    life = false;
		  }
	    }
	  }
    }
  }
  public String getAnnotationString()
  {
	  return annotationString;
  }
}