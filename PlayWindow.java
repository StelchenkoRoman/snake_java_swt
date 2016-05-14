import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * this class describes Play window
 * @author green
 * @version 0.1
 */
public class PlayWindow {         //игровое окно
  private final int IMAGE_WIDTH  =  20;
  private int keyPause = 262144, TIMER_INTERVAL = 100;
  private Label scoreLabel;
  private Canvas canvas;
  private Shell shell, mainShell;
  private Display display;
  private String playerName, nameFile;
  private Button pauseButton, newGameButton;
  private boolean pauseFlag, newGameFlag;
  private Snake MySnake = new Snake( );    //объект класса Snake
  /**  Thread-server class*/
  class SnakeAnimate extends Thread
  {
  	/** here is the calculation of the positions of the snake segments and the food*/
  	public void run() {
  		MySnake.animate( scoreLabel );
  	}
  }
  Device device  =  Display.getCurrent ( );
  PlayWindow ( ) {}
  PlayWindow ( final Shell mainShell1, final Display display1 ) {   //настройка игрового окна
    display = display1;
    mainShell = mainShell1;
    shell  =  new Shell( mainShell1, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM );
    shell.setText( "Snake" );
    shell.setSize( 620, 470 );
    shell.setMinimumSize( 620, 470 );
    pauseFlag = false;
    Color yellow  =  new Color ( device, 255, 250, 205 );
    shell.setBackground( yellow );
  }
  /** Client-thread, here is updated graphical field, and checks the end of the game*/
  final Runnable runnable  =  new Runnable( ) {
	public void run( ) {
      if ( !shell.isDisposed( ) )
	    if ( !pauseFlag ) {
		  SnakeAnimate sA = new SnakeAnimate();
		  sA.run();
		  try {
			sA.join();
		  } catch (InterruptedException e) {
			e.printStackTrace();
			};
	      if ( !MySnake.getLife( ) ) {                     //если конец игры
		    gameOver( shell, display, String.valueOf( MySnake.getPoints( ) ), mainShell );
		    return;
	      }
	      if ( MySnake.getMode() == 3 && !MySnake.getFileFlag() ) {
	    	pauseButton.setVisible(false);
	    	newGameButton.setVisible(true);
	    	return;
	      }
	      canvas.redraw();
	    }
	  display.timerExec( TIMER_INTERVAL, this );
	}
  };
  /** displays in the window play field */
  public  void open( final Shell mainShell, final Display display, final String name, final int level,final int mode ) {
	for ( Control kid : shell.getChildren( ) ) {
	  kid.dispose( );
	}
	shell.setText( "Snake" );
	newGameFlag = false;
	MySnake.startValue( level,mode );
	playerName = name;
	if ( level == 0 ) {
	  nameFile = "HARD.txt";
    }
	if ( level == 1 ) {
	  nameFile = "NORMAL.txt";
	}
	if ( level == 2 ) {
	  nameFile = "EASY.txt";
	}
	Color red  =  new Color ( device, 250, 128, 114 );
	Color white  =  new Color ( device, 255, 255, 255 );
	Color black  =  new Color ( device, 0, 0, 0 );
    Button exitButton  =  new Button( shell, SWT.PUSH );
	exitButton.setText( "EXIT" );
	exitButton.setBackground( red );
	exitButton.setForeground( black );
	exitButton.setBounds( 500, 414, 100, 25 );
	
	final Button continueButton  =  new Button( shell, SWT.PUSH );
	continueButton.setText( "CONTINUE" );
	continueButton.setBackground( red );
	continueButton.setForeground( black );
	continueButton.setBounds( 260, 414, 100, 25 );
	continueButton.setVisible( false );
	
	pauseButton  =  new Button( shell, SWT.PUSH );
	pauseButton.setText( "PAUSE" );
	pauseButton.setBackground( red );
	pauseButton.setForeground( black );
	pauseButton.setBounds( 260, 414, 100, 25 );
	
	Button toMenuButton  =  new Button( shell, SWT.PUSH );
	toMenuButton.setText( "MENU" );
	toMenuButton.setBackground( red );
	toMenuButton.setForeground( black );
	toMenuButton.setBounds( 380, 414, 100, 25 );
	shell.addListener(SWT.Close, new Listener() {
	  public void handleEvent(Event event) {
		if ( MySnake.getMode() != 3) {
		  if( MySnake.getLife() == true ) {
		   	MySnake.save(playerName);
		  } else
		      recordsRefresh( nameFile, MySnake.getPoints( ), playerName );
		  MySnake.repFileClose();
	    }
	  }
	});
	Listener  pauseListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    pauseButton.setVisible( false );
	    continueButton.setVisible( true );
	    pauseFlag = true;
      }
    };
    
	Listener  continueListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	 	pauseButton.setVisible( true );
	    continueButton.setVisible( false );
	    pauseFlag = false;
      }
	};
	Listener  exitListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
		if ( MySnake.getMode() != 3) {
		  if( MySnake.getLife() == true ) {
		  	MySnake.save(playerName);
		  } else
		      recordsRefresh( nameFile, MySnake.getPoints( ), playerName );
		  MySnake.repFileClose();
		}
		System.exit( 0 );
	  }
	};
	Listener  menuListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	 	newGameFlag = true;
	    mainShell.setVisible( true );
	    if ( MySnake.getMode() != 3) {
	      if( MySnake.getLife() == true ) {
	    	MySnake.save(playerName);
	      } else
		      recordsRefresh( nameFile, MySnake.getPoints( ), playerName );
	      MySnake.repFileClose();
	    }
	 	shell.setVisible( false );
		return;
      }
	};
	newGameButton  =  new Button( shell, SWT.PUSH );
	newGameButton.setText( "RESTART" );
	newGameButton.setBackground( red );
	newGameButton.setForeground( black );
	newGameButton.setBounds( 260, 414, 100, 25 );
	newGameButton.setVisible( false );
	Listener  newGameListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    recordsRefresh( nameFile, MySnake.getPoints( ), playerName );
		open(mainShell,display,playerName,level,mode);
		mainShell.setVisible( false );
	  }
	};
	newGameButton.addListener( SWT.Selection, newGameListener );
	pauseButton.addListener( SWT.Selection, pauseListener );
	exitButton.addListener( SWT.Selection, exitListener );
	toMenuButton.addListener( SWT.Selection, menuListener );
	continueButton.addListener( SWT.Selection, continueListener );
	
	scoreLabel  =  new Label( shell, SWT.None );
	scoreLabel.setText( "Score :  "+ MySnake.getPoints( ) );
	scoreLabel.setBounds( 10, 420, 70, 20 );
	canvas  =  new Canvas( shell, SWT.NO_BACKGROUND );
	canvas.setBounds( 7, 10, 600, 400 );
	canvas.setBackground( white );
    final  Image image  =  new Image( shell.getDisplay( ), "/home/green/final vers1.png" );
    final  Image image2  =  new Image( shell.getDisplay( ), "/home/green/d.gif" );
    final  Image image3  =  new Image( shell.getDisplay( ), "/home/green/index.jpeg" ); 
    canvas.addPaintListener( new PaintListener( ) {    // отрисовка игрового поля
	  public void paintControl( PaintEvent event ) {
	    int tx = 0;    //координаты  требуемой части  изображения для отображения элемента змейки
	    int ty = 0;
	    int segx;   //текущее значение координат элемента змейки
        int segy;
        Coords  nseg, pseg;   //координаты след и пред эл-та змейки
        for ( int i = 0; i<600; i+= 20 ) {                             //отрисовка границ поля
	      event.gc.drawImage( image2, 0, 0, 256, 256, i, 0, 20, 20 );
	      event.gc.drawImage( image2, 0, 0, 256, 256, i, 380, 20, 20 );
	    }
	    for ( int i = 0; i<400; i+= 20 ) {                             //отрисовка границ поля
		  event.gc.drawImage( image2, 0, 0, 256, 256, 0, i, 20, 20 );
		  event.gc.drawImage( image2, 0, 0, 256, 256, 580, i, 20, 20 );
	    }
	    for ( int i = 20; i<580; i+= 20 )                    // отрисовка поля змейки
	    for ( int j = 20; j<380; j+= 20 )
	      event.gc.drawImage( image3, 0, 48, 48, 47, i, j, 20, 20 );
        event.gc.drawImage( image, 128, 192, 64, 64, MySnake.getFoodCoords( ).x, MySnake.getFoodCoords( ).y,20,20 );
        for ( int i = 0; i<MySnake.getWallSize(); i++ ) {
        	  event.gc.drawImage( image, 4, 193, 61, 61, MySnake.getWallCoords(i).x, MySnake.getWallCoords(i).y, 20, 20 );
        	  }        
	    for ( int i = 0; i<MySnake.getSize( ); i++ ) {             //отрисовка змейки
	      segx = MySnake.getSnakeCoords( i ).x;   //текущее значение координат элемента змейки
	      segy = MySnake.getSnakeCoords( i ).y;
	      event.gc.setBackground( event.display.getSystemColor( SWT.COLOR_RED ) );
	      if ( i  ==  0 ) {      // установка tx и ty для головы змейки
	        if ( MySnake.getDirection( ) == 1  && MySnake.getDirectionValue( ).x>0 )	{
	          tx = 4; ty = 0;
	        }
	        if ( MySnake.getDirection( ) == 1  && MySnake.getDirectionValue( ).x<0 ) {
	          tx = 3; ty = 1;
	        }
	        if ( MySnake.getDirection( ) == 2  && MySnake.getDirectionValue( ).y>0 ) {
	          tx = 4; ty = 1;
	        }
	        if ( MySnake.getDirection( ) == 2  && MySnake.getDirectionValue( ).y<0 ) {
	          tx = 3; ty = 0;
	        }
	    } else if ( i  ==  MySnake.getSize( )-1 ) {   // установка tx и ty для хвоста змейки
	          pseg  =  MySnake.getSnakeCoords( i-1 );
			  if ( pseg.y < segy ) {
	            tx  =  3;  ty  =  2;
	          } else if ( pseg.x > segx ) {
	              tx  =  4;  ty  =  2;
	            } else if ( pseg.y > segy ) {
	                tx  =  4;  ty  =  3;
	              } else if ( pseg.x < segx ) {
	                  tx  =  3;  ty  =  3;
	                }
	      } else {       // установка tx и ty для средней части змейки
	          nseg  =  MySnake.getSnakeCoords( i+1 );
	          pseg  =  MySnake.getSnakeCoords( i-1 );
              if ( pseg.x < segx && nseg.x > segx || nseg.x < segx && pseg.x > segx ) {
                tx  =  1;  ty  =  0;
              } else if ( pseg.x < segx && nseg.y > segy || nseg.x < segx && pseg.y > segy ) {
                  tx  =  2;  ty  =  0;
                } else if ( pseg.y < segy && nseg.y > segy || nseg.y < segy && pseg.y > segy ) {
                    tx  =  2;  ty  =  1;
                  } else if ( pseg.y < segy && nseg.x < segx || nseg.y < segy && pseg.x < segx ) {
                      tx  =  2;  ty  =  2;
                    } else if ( pseg.x > segx && nseg.y < segy || nseg.x > segx && pseg.y < segy ) {
                        tx  =  0;  ty  =  1;
                      } else if ( pseg.y > segy && nseg.x > segx || nseg.y > segy && pseg.x > segx ) {
                          tx  =  0;  ty  =  0;
                        }
              }	      
	    event.gc.drawImage( image, tx*64, ty*64, 64, 64, MySnake.getSnakeCoords( i ).x, 
		        			MySnake.getSnakeCoords( i ).y, IMAGE_WIDTH, IMAGE_WIDTH );  // отрисовка i-го эл-та змейки
		}
	  }
    }); 
	Listener listenerKeyboard  =  new Listener( ) { // действие на нажатие кнопок
	  public void handleEvent( Event e ) {
		if ( e.keyCode == keyPause ) {
		  pauseFlag = !pauseFlag;
		}
		if ( !pauseFlag )
	      MySnake.keyPressed( e.keyCode );    //e, keyCode - код нажатой клавиши
	  }
	};
	Display.getCurrent( ).addFilter( SWT.KeyDown, listenerKeyboard );
	shell.open( );
	display.timerExec( 1000, runnable );	//запуск движения змейки через секунду
 }
  /** this method update records table*/
  public  void recordsRefresh( String filename, int points, String playerName ) {  //метод обновления рекордов
    String name[] = new String[10], record[] = new String[10];
	try{
	  BufferedReader myfile  =  new BufferedReader ( new FileReader( filename ) );
	  try {
		for ( int i = 0; i<10; i++ ) {  //считывание инф-и из фалйа в массивы имен и рекордов
		  name[i] = myfile.readLine( );
		  record[i] = myfile.readLine( );
		}
      } finally {
		  myfile.close( ); // закрытие файла
		}
	} catch ( IOException e ) {
	    throw new RuntimeException( e );
	  }
	if ( points>= Integer.parseInt( record[9].trim( ) ) ) { //сравнение и сортировка набранных очков с эл-ми массива рекордов
	  for ( int i = 9; i>= 0; i-- ) {
	    if ( points>= Integer.parseInt( record[i].trim( ) ) &&
	  	   ( i == 0 || points<Integer.parseInt( record[i-1].trim( ) ) ) ) {
	      for ( int j = 9; j>i; j-- ) {
	        name[j] = name[j-1];
	    	record[j] = record[j-1];
	      }
	      name[i] = playerName;
	      record[i] = String.valueOf( points );
	      break;
	    }
	  }
	File file  =  new File( filename );
	try {
	  PrintWriter out  =  new PrintWriter( file.getAbsoluteFile( ) );  //PrintWriter обеспечит возможности записи в файл
	  try {
	    for ( int i = 0; i<10; i++ ) {   //запись текста в файл
	      out.println( name[i] );
	      out.println( record[i] );
	    }
	  } finally {
	      out.close( );    //закрытие файла
	    }
	} catch ( IOException e ) {
	    throw new RuntimeException( e );
	  }
	}
  }
  /** displays game over field*/
  public  void gameOver( final Shell shell, final Display display, String points, final Shell mainShell ) { //конец игры  
   	canvas.dispose( );
	scoreLabel.dispose( );
	pauseButton.setVisible( false );
	shell.setText( "Game over" );
	Label image_label  =  new Label( shell, SWT.NONE );               // загрузка кортинки
	image_label.setImage( new Image( display, "/home/green/ov.png" ) );
	image_label.setBounds( 70, 50, 380, 280 );
	Label gameOverLabel  =  new Label( shell, SWT.WRAP | SWT.CENTER );
	gameOverLabel.setText( " Your score : "+points );
	gameOverLabel.setBounds( 10, 420, 100, 30 );
	newGameButton.setVisible( true );
  }
}