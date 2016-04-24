import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.IIOException;

import org.eclipse.swt.widgets.Label;
/**
 * this class keep SnakeSegment foodCordscoords,wallcoords
 * change and generate new Coords
 * @author green
 * @version 0.1
 */
public class Snake {
  private  Coords[] walls;
  private  Scanner repSc;
  private  PrintWriter repFile;
  private  ArrayList<Coords> snakeCoords;      // список координат каждой ячейки змейки
  private  Coords foodCords;                   // координаты еды
  private  Coords directionValue;              // объект класса Coords хранящий текущее изменение координат ( x,y ) головы змейки
  private  final int keyUp = 16777217, keyDown = 16777218,    //коды  соотв-щие нажатым клавишам стрелок
	 	             keyRight = 16777220, keyLeft = 16777219; 
  private  final int playerMode = 0 , botMode = 1, loadMode = 2, replayMode = 3; // моды
  private  int points;                         // количество набранных очков в игре
  private  int direction;                      // ось движения змейки ( 1- по горизонтали, 2 - по вертикали )
  private  int size, level, wallSize=0;						   // длина змейки
  private  boolean life;					   // переменная определяющая конец игры ( false - конец игры )
  private  int mode;
  private  Random random;                       // переменная для генерации рандомной позиции еды
  Snake( ) {									   // конструктор Snake
	snakeCoords  =  new ArrayList<Coords>( );     // выделение памяти под список координат
	foodCords  =  new Coords( );				   // выделение памяти для координат еды
	directionValue  =  new Coords( );             // выделение памяти для переменной изменения координат головы змейка
	random  =  new Random( );	 				   // выделение памяти для переменной рандом
	walls = new Coords[66];
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
  public void startValue( int levelSel, int modeV ) {	// метод со стартовыми настройки игры
	mode =modeV;
	life  =  true;
	snakeCoords.clear( );			// очистка списка координат элементов змейки
	if ( mode == replayMode ) {
		replay();
	} else if ( mode == loadMode ) {
		saveStartSettings();
	} else {
	points  =  0; 					
    size  =  2;
    level = levelSel;
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
	File file  =  new File( "replay.txt" );  // запись начальных настроек в файл для реплея
	try {    		 
	  repFile  =  new PrintWriter(file.getAbsoluteFile());  
	}catch ( IOException ex ) {
	    throw new RuntimeException( ex ); 
	  }	
	repFile.println(level);
	repFile.print(foodCords.x);
	repFile.print(' ');
	repFile.print(foodCords.y);
	repFile.print(' ');
	repFile.print(snakeCoords.get(0).x);
	repFile.print(' ');
	repFile.print(snakeCoords.get(0).y);
	repFile.print(' ');
    }
  }
  /** this method change coords snake segments, and inspection сoncinence coords */
  public void animate( Label score ) { // метод обновления координат   
	if (mode == replayMode ) 
     replayAnimate(score);
    else {	  
	  if ( snakeCoords.get( 0 ).x == foodCords.x && snakeCoords.get( 0 ).y == foodCords.y ) {// при совпадении координат еды с координатой головы змейки 		  	  
	    foodInspection(score);
	    repFile.print(foodCords.x);  // запись в файл координат еды для возможности реплея
        repFile.print(' ');
	    repFile.print(foodCords.y);
	    repFile.print(' ');	
	  }
	  repFile.print(direction);
	  repFile.print(' ');
	  if ( direction ==1 )
		repFile.print(directionValue.x); 
	  else repFile.print(directionValue.y);
	    repFile.print(' ');  
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
	  if(direction ==1)
	 {		 
		 if ( ( snakeCoords.get( 0 ).x+directionValue.x ) < 20 || ( snakeCoords.get( 0 ).x+directionValue.x ) > 560 ) {   
			  life = false;
			  return;
			}
			
		 snakeCoords.get( 0 ).x +=  directionValue.x; 	
		 if( snakeCoords.get( 0 ).x  == foodCords.x ||  (snakeCoords.get( 0 ).x < foodCords.x && directionValue.x==-20) || (snakeCoords.get( 0 ).x > foodCords.x && directionValue.x==20)  ) {	
			  direction=2;
			  if(snakeCoords.get( 0 ).y  < foodCords.y )
				  directionValue.y=20;
			  else directionValue.y=-20;
			  if( snakeCoords.get( 0 ).y== 0)
		      {
		    	  directionValue.y=20;
		      }
		      if(snakeCoords.get( 0 ).y== 360)
		      {
		    	  directionValue.y=-20;
		      } 
		      for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
				  if ( snakeCoords.get( 0 ).x  == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y + directionValue.y == snakeCoords.get( i ).y )  {	          
				  direction=1; 
				  } 
			    }	
		 }
	 }
	 else
	 {
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
			  if( snakeCoords.get( 0 ).x== 0)
		      {
		    	  directionValue.x=20;
		      }
		      if(snakeCoords.get( 0 ).x== 560)
		      {
		    	  directionValue.x=-20;
		      }  
		      for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
				  if ( snakeCoords.get( 0 ).x + directionValue.x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
				  direction=2; 
				  } 
			    }
			   } 
	 }
	 /*
	 
	 if ( direction  ==  1 ) {   
	  snakeCoords.get( 0 ).x +=  directionValue.x; 	
	  if ( directionValue.x >0 && snakeCoords.get( 0 ).x + directionValue.x > 560 ) {
		direction = 2;
		directionValue.y = 20;
		directionValue.x = -directionValue.x;
	  } 
	  if ( directionValue.x <0 ) {
   	    direction =2;
	  }
	  return;
	}
	if ( direction  ==  2 ) {
	  snakeCoords.get( 0 ).y +=  directionValue.y;  
	  if ( snakeCoords.get( 0 ).y + directionValue.y > 360 ) {
	    direction = 1;  
		directionValue.y = -20;
		directionValue.x = -20;
	  }
	  if ( snakeCoords.get( 0 ).x ==20 &&  snakeCoords.get( 0 ).y == 20) { 
		directionValue.x = 20;
		direction = 1;
	  }
	  if (  snakeCoords.get( 0 ).x !=20 && directionValue.x !=560 &&  snakeCoords.get( 0 ).y + directionValue.y < 40 ) {
		directionValue.y = 20;
		direction = 1;
	  }
    }*/
  }
  
  public void save(String name) {   // сохранение в файл последних координат змейки и еды
	File file  =  new File( "names.txt" ); 
	try {    		 
	  PrintWriter out  =  new PrintWriter( file.getAbsoluteFile( ) ); 
	  try {
	    out.println(name);
		out.println(level);
		out.print(foodCords.x);
		out.print(' ');
		out.println(foodCords.y);
		out.print(direction);
		out.print(' ');
		out.print(directionValue.x);
		out.print(' ');
		out.println(directionValue.y);    
		out.println(size);
		for ( int i = 0; i<size; i++ ) {   
		  out.print(snakeCoords.get(i).x);
		  out.print(' ');
		  out.print(snakeCoords.get(i).y);
		  out.print(' ');
	    }
	  } finally {
	      out.close( );    //закрытие файла
   	    }
	} catch ( IOException ex ) {
	    throw new RuntimeException( ex ); 
	  }	 
  }
  public void saveStartSettings() {	  // считывание из файла стартовый настроек последней сохраненной игры
	try {    		 
	  repFile  =  new PrintWriter(new OutputStreamWriter(new FileOutputStream("replay.txt", true)));   
	}catch ( IOException ex ) {
	   throw new RuntimeException( ex ); 
	 }  
	File file  =  new File( "names.txt" ); 		 
	try { 
	  Scanner sc = new Scanner(file);
	  String name = sc.nextLine();
	  level = sc.nextInt();
	  foodCords.x = sc.nextInt();
	  foodCords.y = sc.nextInt();
	  direction = sc.nextInt();
	  directionValue.value(sc.nextInt(), sc.nextInt());
	  size  = sc.nextInt();
	  for ( int i = 0; i<size; i++ ) {
	  	Coords temp =  new Coords( );  		
	  	temp.value(sc.nextInt(), sc.nextInt());
	  	snakeCoords.add( temp );
	  }
	  sc.close();
	} 
	catch (IOException  e) {
	  e.printStackTrace();
	}
	points  =  size-2; 					
	mode = 0; 
	setWallCoords();
  }

  public void foodInspection(Label score) {   // метод проверки на совпадение координат еды с частями змейки и стенками карты
	boolean flag;  
	Coords c = new Coords( );
	c.value( snakeCoords.get( size-1 ).x, snakeCoords.get( size-1 ).y );
	snakeCoords.add( c );
	size++;          //увеличение размера змейки
	points++;			//увеличение кол-ва набранных очков
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
  public void repFileClose() { 
  repFile.close();
  }
  public void replayAnimate(Label score) {
	if ( snakeCoords.get( 0 ).x == foodCords.x && snakeCoords.get( 0 ).y == foodCords.y ) {// при совпадении координат еды с координатой головы змейки 		  	  
	  foodInspection(score);
	  foodCords.x = repSc.nextInt();
	  foodCords.y = repSc.nextInt();    
	} 
	for ( int i = size-1; i>0; i-- )    // координаты всех элементов змейки принимают значение предыдущих  элементов
	  snakeCoords.get( i ).value( snakeCoords.get( i-1 ).x, snakeCoords.get( i-1 ).y ); 
	direction = repSc.nextInt();	
	if ( direction  ==  1 ) { // если змейка движется по горизонтали  
	  directionValue.x = repSc.nextInt();
	  if ( ( snakeCoords.get( 0 ).x + directionValue.x ) < 20 || ( snakeCoords.get( 0 ).x + directionValue.x ) > 560 ) {   
	    life = false;
		return;
      }
	 snakeCoords.get( 0 ).x += directionValue.x; // координата х головы изменяется на значение  directionValue.x 	
	}
	if ( direction  ==  2 ) {
	  directionValue.y = repSc.nextInt();
	  if ( ( snakeCoords.get( 0 ).y + directionValue.y )  < 20       || ( snakeCoords.get( 0 ).y + directionValue.y ) > 360 ) {
		life = false;
		return;
	  }
	  snakeCoords.get( 0 ).y +=  directionValue.y; 
	}		 
	for ( int i = 1; i<size; i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
	  if ( snakeCoords.get( 0 ).x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
	    life = false;   			                	
  	  } 
    }
	wallInspection();   
  }
  public void replay() {   // считывание начальных координат в режиме "replay"
	points = 0;
	File file  =  new File( "replay.txt" ); 		 
	  try { 
	    repSc = new Scanner(file);
	    size = 2;
	    level = repSc.nextInt();
	    foodCords.x = repSc.nextInt();
	    foodCords.y = repSc.nextInt();
	    Coords head = new Coords() ,tail =  new Coords( );  		
	    head.value(repSc.nextInt(), repSc.nextInt());
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
	    } 
	    catch (IOException  e) {}
	    setWallCoords();	
  }
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
  public int getMode() {
	  return mode;
	  }
}
  