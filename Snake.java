import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.widgets.Label;


public class Snake {
  private  ArrayList<Coords> snakeCoords;      // список координат каждой ячейки змейки
  private  Coords foodCords;                   // координаты еды
  private  Coords directionValue;              // объект класса Coords хранящий текущее изменение координат ( x,y ) головы змейки
  private final int keyUp = 16777217, keyDown = 16777218,    //коды  соотв-щие нажатым клавишам стрелок
	 	            keyRight = 16777220, keyLeft = 16777219;  
  private  int points;                         // количество набранных очков в игре
  private  int direction;                      // ось движения змейки ( 1- по горизонтали, 2 - по вертикали )
  private  int size;						   // длина змейки
  private  boolean life;					   // переменная определяющая конец игры ( false - конец игры )
  private Random random;                       // переменная для генерации рандомной позиции еды
  Snake( ) {									   // конструктор Snake
	snakeCoords  =  new ArrayList<Coords>( );     // выделение памяти под список координат
	foodCords  =  new Coords( );				   // выделение памяти для координат еды
	directionValue  =  new Coords( );             // выделение памяти для переменной изменения координат головы змейка
	random  =  new Random( );	 				   // выделение памяти для переменной рандом
  }
  public boolean getLife( ) { 					// возврат переменной life
	return life;
  }
  public Coords getFoodCoords( ) {
	return foodCords;
  }
  public Coords getDirectionValue( ) {		// возвращает текущее изменение координат головы змейки
    return directionValue;
  }
  public int getDirection( ) {            // возвращает текущеее направление движения змейкй
	return direction;
  }
  public int getPoints( ) {				// возвращает кол-во набранных очков
	return points;
  }
  public int getSize( ) {					// возвращает размер змейки
	return size;
  }
  public Coords getSnakeCoords( int index ) {// возвращает координаты элемента змейки с индексом index
	return snakeCoords.get( index );
  }
  public void setDirection( int x ) {     // установка оси движения
	direction  =  x;
  }
  public void setDirectionValueX( int x ) {  //установка измен-я координаты х
	directionValue.x  =  x; 
  }
  public void setDirectionValueY( int y ) {  // установка изменнения координаты у
	directionValue.y  =  y;
  }
  public void setPoints( ) {   //увеличение кол-ва очков
	points++;  
  }
  public void setLife( ) {  //установка флага life в false
	life  =  false; 
  }
  public void setFoodCoords( Coords c ) { //установка координаты еды
	foodCords  =  c;
  }
  public void setSnakeCoords( ) {   
  }
  public void startValue( ) {		// метод со стартовыми настройки игры
	points  =  0; 					
    size  =  2;
    life  =  true;
    if ( random.nextInt( 2 )  ==  1 ) {  	// выбор оси движения
      direction  =  1;
      if ( random.nextInt( 2 )  ==  1 )      // выбор направления движения по оси X
    	directionValue.x  =  20;
      else directionValue.x  =  -20;
    }    
    else { 
      direction  =  2;      
      if ( random.nextInt( 2 )  ==  1 )      // выбор направления движения по оси Y
	    directionValue.y  =  20;
	  else directionValue.y  =  -20;
    }
    snakeCoords.clear( );			// очистка списка координат элементов змейки

	Coords tail,head  =  new Coords( );  		// объект класса Coords
	head.value( ( 1+random.nextInt( 28 ) )*20, ( 1+random.nextInt( 18 ) )*20 );  // рандомный выбор позиции головы змеки 
	snakeCoords.add( head );          // добавление головы змейки в список snakeCoord
	tail  =  new Coords( );
	if ( direction  ==  1 ) {
	  tail.value( head.x+directionValue.x, head.y );
	}
	else tail.value( head.x, head.y+directionValue.y );
	snakeCoords.add( tail );
	while ( true ) {					// генерирование координат еды 
	  foodCords.x  =  ( 1+random.nextInt( 28 ) )*20;
      foodCords.y = ( 1+random.nextInt( 18 ) )*20;
	if ( foodCords.x!= head.x && foodCords.y!= head.y )  // цикл выполяется пока координаты еды и головы змейки совпадают
	  break;
	}
  }
  public void animate( Label score ) { // метод обновления координат   
    if ( snakeCoords.get( 0 ).x == foodCords.x && snakeCoords.get( 0 ).y == foodCords.y ) {// при совпадении координат еды с координатой головы змейки 		  	  
	  Coords c = new Coords( );
	  c.value( snakeCoords.get( 0 ).x, snakeCoords.get( 0 ).y );
	  snakeCoords.add( c );
	  size++;          //увеличение размера змейки
	  points++;			//увеличение кол-ва набранных очков
	  score.setText( "Score :  "+ points );  //вывод на экран кол-ва набраный очков
      while ( true ) { // цикл выполняющий генерирование новых коорднат еды
        boolean r = false;
        foodCords.x = ( 1+random.nextInt( 28 ) )*20;
        foodCords.y = ( 1+random.nextInt( 18 ) )*20;
        for ( int i = 0;i<size;i++ ) 
          if ( foodCords.x!= snakeCoords.get( i ).x && foodCords.y!= snakeCoords.get( i ).x ) {// цикл завершается когда координата еды не перекрывает ни один элемент змейки   
            r = true;
            break;
          }
        if ( r )
      	  break;
      }
	}
	 
	for ( int i = size-1;i>0;i-- )    // координаты всех элементов змейки принимают значение предыдущих  элементов
	  snakeCoords.get( i ).value( snakeCoords.get( i-1 ).x, snakeCoords.get( i-1 ).y ); 
	 
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
	for ( int i = 1;i<size;i++ ) {   //при совпадении координат головы змейки и одного из эл-тов змейки игра прекращается
	  if ( snakeCoords.get( 0 ).x == snakeCoords.get( i ).x && snakeCoords.get( 0 ).y == snakeCoords.get( i ).y )  {	          
	    life = false;   			                	
	  }
    }
  }
  
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
  
}
