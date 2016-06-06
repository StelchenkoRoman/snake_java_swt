import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.eclipse.swt.widgets.Text;
/**
 * this class describes Play window
 * @author green
 * @version 0.1
 */
public class PlayWindow {         //игровое окно
  private final int IMAGE_WIDTH  =  20;
  private  final int botMode = 1,replayMode = 3;
  private int gameMode,keyPause = 262144, TIMER_INTERVAL = 100;
  private Label scoreLabel;
  private Canvas canvas;
  private Shell shell, mainShell;
  private Display display;
  private String playerName, nameFile;
  private Button pauseButton, newGameButton;
  private boolean pauseFlag, newGameFlag,menuFlag;
  private Text annotationText;
  private static Color buttonColor,backGroundColor,textColor;
  private Snake MySnake = new Snake( );    //объект класса Snake
  /**  Thread-server class*/
  class SnakeAnimate extends Thread
  {
  	/** here is the calculation of the positions of the snake segments and the food*/
  	public void run() {
  		MySnake.animate( scoreLabel );
  		if ( gameMode == replayMode )
  		annotationText.setText(annotationText.getText() + MySnake.getAnnotationString());
  	}
  }
  Device device  =  Display.getCurrent ( );
  PlayWindow ( ) {}
  PlayWindow ( final Shell mainShell1, final Display display1 ) {   //настройка игрового окна
    display = display1;
    mainShell = mainShell1;
   
    textColor = new Color ( device, 0, 0, 0);
	buttonColor  =  new Color ( device, 255, 218, 185 );
	backGroundColor = new Color ( device, 224, 238, 224  );
	
    shell  =  new Shell( mainShell1, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM );
    shell.setText( "Snake" );
    shell.setSize( 620, 470 );
    shell.setMinimumSize( 620, 470 );
    pauseFlag = false;
    menuFlag = false;
   shell.setBackground( backGroundColor );
  }
  /** Client-thread, here is updated graphical field, and checks the end of the game*/
  final Runnable runnable  =  new Runnable( ) {
	public void run( ) {
      if ( !shell.isDisposed( ) )
	    if ( !pauseFlag && !menuFlag) {
		  SnakeAnimate snAnim = new SnakeAnimate();
		  snAnim.run();
		  try {
			snAnim.join();
		  } catch (InterruptedException e) {
			e.printStackTrace();
			};
	      if ( !MySnake.getLife( ) ) {                     //если конец игры
		    gameOver( shell, display, String.valueOf( MySnake.getPoints( ) ), mainShell );
		    return;
	      }
	      if ( MySnake.getMode() == 3 && !MySnake.getFileFlag() ) {
	    	pauseButton.setVisible(false);
	    	return;
	      }
	      canvas.redraw();
	      if(MySnake.keyFlag==false){
			  MySnake.keyFlag=true;
		  }
	    }
	  display.timerExec( TIMER_INTERVAL, this );
	}
  };
  /** displays in the window play field */
  public  void open( final Shell mainShell, final Display display, final String name, final int level,final int mode,String fileNameRep) {
	for ( Control kid : shell.getChildren( ) ) {
	  kid.dispose( );
	}
	shell.setText( "Snake" );
	newGameFlag = false;
	menuFlag = false;
	gameMode = mode;
	MySnake.startValue( level,mode,name ,fileNameRep);
	if ( gameMode == replayMode ) {
	  shell.setSize(900,470);
	  annotationText = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
	  annotationText.setBounds( 630, 10, 250, 400 );
	  annotationText.setBackground(buttonColor);
	  annotationText.setText(MySnake.getAnnotationString());
	}
	else shell.setSize(620,470);
	playerName = name;
	Button exitButton  =  new Button( shell, SWT.PUSH );
	exitButton.setText( "EXIT" );
	exitButton.setBackground( buttonColor );
	exitButton.setForeground( textColor );
	exitButton.setBounds( 500, 414, 100, 25 );
	
	final Button continueButton  =  new Button( shell, SWT.PUSH );
	continueButton.setText( "CONTINUE" );
	continueButton.setBackground( buttonColor );
	continueButton.setForeground( textColor );
	continueButton.setBounds( 260, 414, 100, 25 );
	continueButton.setVisible( false );
	
	pauseButton  =  new Button( shell, SWT.PUSH );
	pauseButton.setText( "PAUSE" );
	pauseButton.setBackground( buttonColor );
	pauseButton.setForeground( textColor );
	pauseButton.setBounds( 260, 414, 100, 25 );
	
	Button toMenuButton  =  new Button( shell, SWT.PUSH );
	toMenuButton.setText( "MENU" );
	toMenuButton.setBackground( buttonColor );
	toMenuButton.setForeground( textColor );
	toMenuButton.setBounds( 380, 414, 100, 25 );
	shell.addListener(SWT.Close, new Listener() {
	  public void handleEvent(Event event) {
		if ( MySnake.getMode() != 3) {
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
		    MySnake.repFileClose();
		}
		System.exit( 0 );
	  }
	};
	Listener  menuListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	 	newGameFlag = true;
	    mainShell.setVisible( true );
	 	shell.setVisible( false );
	 	menuFlag = true;
	 	if ( MySnake.getMode() != 3) {
		  MySnake.repFileClose();
		}
		return;
      }
	};
	newGameButton  =  new Button( shell, SWT.PUSH );
	newGameButton.setText( "RESTART" );
	newGameButton.setBackground( buttonColor );
	newGameButton.setForeground( textColor );
	newGameButton.setBounds( 260, 414, 100, 25 );
	newGameButton.setVisible( false );
	Listener  newGameListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    open(mainShell,display,playerName,level,mode,"");
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
	final  Image image  =  new Image( shell.getDisplay( ), "images/final vers1.png" );
    final  Image image2  =  new Image( shell.getDisplay( ), "images/d.gif" );
    final  Image image3  =  new Image( shell.getDisplay( ), "images/index.jpeg" ); 
   
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
		if ( !pauseFlag && MySnake.keyFlag == true )
		  MySnake.keyFlag=false;
		  MySnake.keyPressed( e.keyCode );    //e, keyCode - код нажатой клавиши
	      
	  }
	};
	Display.getCurrent( ).addFilter( SWT.KeyDown, listenerKeyboard );
	shell.open( );
	 	 
	display.timerExec( 1000, runnable );	//запуск движения змейки через секунду
  }
  /** displays game over field*/
  public  void gameOver( final Shell shell, final Display display, String points, final Shell mainShell ) { //конец игры  
   	canvas.dispose( );
	scoreLabel.dispose( );
	pauseButton.setVisible( false );
	shell.setText( "Game over" );
	Label image_label  =  new Label( shell, SWT.NONE );               // загрузка кортинки
	image_label.setImage( new Image( display, "images/ov.png" ) );
	image_label.setBounds( 70, 50, 380, 280 );
	Label gameOverLabel  =  new Label( shell, SWT.WRAP | SWT.CENTER );
	gameOverLabel.setText( " Your score : "+points );
	gameOverLabel.setBounds( 10, 420, 100, 30 );
	newGameButton.setVisible( true );
  }
  public void statisticsOpen(final Shell mainShell, final Display display, final String name, final int level,final int mode,String fileNameRep ) {
	for ( Control kid : shell.getChildren( ) ) {
	  kid.dispose( );
	}
	shell.setText( "Statistics" );
	Button exitButton  =  new Button( shell, SWT.PUSH );
	exitButton.setText( "EXIT" );
	exitButton.setBackground( buttonColor );
	exitButton.setForeground( textColor );
	exitButton.setBounds( 500, 414, 100, 25 );
		
	Button toMenuButton  =  new Button( shell, SWT.PUSH );
	toMenuButton.setText( "MENU" );
	toMenuButton.setBackground( buttonColor );
	toMenuButton.setForeground( textColor );
	toMenuButton.setBounds( 380, 414, 100, 25 );
	
	Listener  exitListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
		System.exit( 0 );
	  }
	};
	Listener  menuListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
		  
		if ( MySnake.getMode() != 3) {
			    MySnake.repFileClose();
		}
	    mainShell.setVisible( true );
	 	shell.setVisible( false );
		return;
      }
	};
	exitButton.addListener( SWT.Selection, exitListener );
	toMenuButton.addListener( SWT.Selection, menuListener );
	scoreLabel  =  new Label( shell, SWT.None );
	scoreLabel.setBounds( 5, 350, 300, 20 );
	canvas  =  new Canvas( shell, SWT.NO_BACKGROUND );
	canvas.setBounds( 5, 5, 300, 200 );
    final  Image image2  =  new Image( shell.getDisplay( ), "images/d.gif" );
    final  Image []statImages=new Image[5];
    statImages[0]  =  new Image( shell.getDisplay( ), "images/stat1.png" );
    statImages[1]  =  new Image( shell.getDisplay( ), "images/stat2.png" );
    statImages[2]  =  new Image( shell.getDisplay( ), "images/stat3.png" );
    statImages[3]  =  new Image( shell.getDisplay( ), "images/stat4.png" );
    statImages[4]  =  new Image( shell.getDisplay( ), "images/stat5.png" );
    
    ArrayList<Coords> snakeStatCoords = new ArrayList<Coords>( );
  	ArrayList<Coords> foodStatCoords = new ArrayList<Coords>( );
  	final int [][]statCount; 
  	final int [][]statFoodCount; 
    Coords maxHeadCoords=new Coords(0,0);
    Coords maxFoodCoords=new Coords(0,0);   
    Statistics stat= new Statistics();
    int max,maxFood;
  	try {	    
      SeekableByteChannel fHelpChannel = Files.newByteChannel(Paths.get(fileNameRep));
  	  int fileSize = (int) fHelpChannel.size();
  	  ByteBuffer bufferRead = ByteBuffer.allocate(fileSize);	
  	  fHelpChannel.read(bufferRead);
  	  bufferRead.flip();
  	  char temp;
  	  String fileName="";
  	  int totalPoints=bufferRead.getInt();
  	  scoreLabel.setText(String.valueOf(totalPoints)+ " -   total points" );
  	  while ( true ) {
  		temp = bufferRead.getChar();
  	    if ( temp == '+' ) {
  	      break;
  	    }
  		fileName += String.valueOf(temp);
      } 
  	  Coords foodStCoordss = new Coords();
  	  foodStCoordss.x=bufferRead.getInt();
  	  foodStCoordss.y=bufferRead.getInt();
  	  foodStatCoords.add(foodStCoordss);  
  	  while ( bufferRead.hasRemaining() ) {
        Coords readSnCoords = new Coords();
	    readSnCoords.x=bufferRead.getInt();
	    readSnCoords.y=bufferRead.getInt();
	    if ( readSnCoords.x==foodStatCoords.get(foodStatCoords.size()-1).x 
	    		&& readSnCoords.y==foodStatCoords.get(foodStatCoords.size()-1).y) {
	      if ( !bufferRead.hasRemaining() )
	  	    break;
	      Coords foodStCoords = new Coords();
		  foodStCoords.x=bufferRead.getInt();
		  foodStCoords.y=bufferRead.getInt();
		  foodStatCoords.add(foodStCoords);  
	    }
	    snakeStatCoords.add(readSnCoords);   
      }
    } catch (IOException e) {
     	e.printStackTrace();
      }
 
    statCount=stat.get(snakeStatCoords);
    statFoodCount= stat.get(foodStatCoords);	 
    max=stat.maxCount(statCount, maxHeadCoords);
    maxFood=stat.maxCount(statFoodCount, maxFoodCoords); 
    final int maxHead=max;
	canvas.setBackground(backGroundColor);
    canvas.addPaintListener( new PaintListener( ) {    // отрисовка игрового поля
	  public void paintControl( PaintEvent event ) {
	    for ( int i = 0; i<300; i+= 10 ) {                             //отрисовка границ поля
	      event.gc.drawImage( image2, 0, 0, 256, 256, i, 0, 10, 10 );
	      event.gc.drawImage( image2, 0, 0, 256, 256, i, 190, 10, 10 );
	    }
	    for ( int i = 0; i<200; i+= 10 ) {                             //отрисовка границ поля
		  event.gc.drawImage( image2, 0, 0, 256, 256, 0, i, 10, 10 );
		  event.gc.drawImage( image2, 0, 0, 256, 256, 290, i, 10, 10 );
	    }    
		Image statImage; 
		for ( int i = 0; i<28; i++ )                    // отрисовка поля змейки
		  for ( int j = 0; j<18; j++ ) {
		   	if ( maxHead<5 )
			  statImage = statImages[statCount[i][j]];
		  	else {
	  	      if(statCount[i][j]>4*maxHead/5 && maxHead>3)
				statImage = statImages[4];     
		  	  else if(statCount[i][j]>3*maxHead/5 && maxHead>2)
			    statImage = statImages[3];     
		  	  else if(statCount[i][j]>2*maxHead/5 && maxHead>1)
				statImage = statImages[2];     
		  	  else if(statCount[i][j]>maxHead/5 && maxHead>0)
    			statImage = statImages[1];     
      	 	  else  statImage = statImages[0];     
			}
		    event.gc.drawImage( statImage, i*10+10, j*10+10 );    
   	      }
	    }
      });
	  Label statImage1Label  =  new Label( shell, SWT.None );
	  statImage1Label.setBounds( 5, 215, 10, 10 );
	  statImage1Label.setImage(statImages[0]);
	  Label statText1Label  =  new Label( shell, SWT.None );
	  statText1Label.setBounds(20, 210, 100, 20 );
	    
	  Label statImage2Label  =  new Label( shell, SWT.None );
	  statImage2Label.setBounds( 5, 235, 10, 10 );
	  statImage2Label.setImage(statImages[1]);
	  Label statText2Label  =  new Label( shell, SWT.None );
	  statText2Label.setBounds(20, 230, 100, 20 );
	  
	  Label statImage3Label  =  new Label( shell, SWT.None );
	  statImage3Label.setBounds( 5, 255, 10, 10 );
	  statImage3Label.setImage(statImages[2]);
	  Label statText3Label  =  new Label( shell, SWT.None );
	  statText3Label.setBounds(20, 250, 100, 20 );
	  
	  Label statImage4Label  =  new Label( shell, SWT.None );
	  statImage4Label.setBounds( 5, 275, 10, 10 );
	  statImage4Label.setImage(statImages[3]);
	  Label statText4Label  =  new Label( shell, SWT.None );
	  statText4Label.setBounds(20, 270, 100, 20 );
	  
	  Label statImage5Label  =  new Label( shell, SWT.None );
	  statImage5Label.setBounds( 5, 295, 10, 10 );
	  statImage5Label.setImage(statImages[4]);
	  Label statText5Label  =  new Label( shell, SWT.None );
	  statText5Label.setBounds(20, 290, 100, 20 );
	  
	  if ( max<5 ) {
	   	statText1Label.setText(" -   "+0);
	  	statText2Label.setText(" -   "+1);
	   	statText3Label.setText(" -   "+2);
	   	statText4Label.setText(" -   "+3);
	   	statText5Label.setText(" -   "+4);
	  }
	  else {
	    statText1Label.setText(" -   "+0+"-"+max/5);
	    statText2Label.setText(" -   "+(max/5+1)+"-"+2*max/5);
	    statText3Label.setText(" -   "+(2*max/5+1)+"-"+3*max/5);
	    statText4Label.setText(" -   "+(3*max/5+1)+"-"+4*max/5);
	    statText5Label.setText(" -   "+(4*max/5+1)+"-"+max);
	  }
	  
	  Label hauntedLabel  =  new Label( shell, SWT.None );
	  hauntedLabel.setBounds(5, 310, 300, 20 );
	  hauntedLabel.setText(max+"   -   times,haunted field { "+maxHeadCoords.x*20+ " ; "+maxHeadCoords.y*20+" }");
      Label hauntedFoodLabel  =  new Label( shell, SWT.None );
	  hauntedFoodLabel.setBounds(5, 330, 300, 20 );
	  hauntedFoodLabel.setText(maxFood+"   -   times,haunted food field { "+maxFoodCoords.x*20+ " ; "+maxFoodCoords.y*20+" }");
    
      Table table  =  new Table( shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION );
	  table.setLinesVisible( true );
	  table.setHeaderVisible( true );
	  final  String[] titles  =  { "{   x;y    }","snake","food" };
	  for ( int i  =  0;  i < titles.length;  i++ ) {
	  TableColumn column  =  new TableColumn( table, SWT.CENTER);
	  column.setText( titles[i] );
	}
	for ( int i = 0; i<28; i++ )                    // отрисовка поля змейки
	  for ( int j = 0; j<18; j++ ) {
	    TableItem item  =  new TableItem( table, SWT.NONE );
	    item.setText( 0,String.valueOf(i*20)+" ; "+String.valueOf(j*20));
	    item.setText( 1, String.valueOf(statCount[i][j]));
	    item.setText( 2,String.valueOf(statFoodCount[i][j]));   
	  }
	  for ( int i = 0; i<titles.length; i++ ) {	  
	    table.getColumn ( i ).pack ( );
	    table.getColumn(i).setWidth(83);
	  }
	  table.setBounds( 340, 5, 250, 400 );
  shell.open( );	
}

public  void generate( final Shell mainShell, final int level,final int mode, int numbers) {
	Random random=new Random();
	String botName;
	int Num=numbers;
	for(int i=0;i<Num;i++) {
		numbers=i;
	newGameFlag = false;
	botName="bot"+String.valueOf((1+random.nextInt( 20 )));			
	MySnake.startValue( level,mode,botName ,"");
	if ( !MySnake.getLife( ) ) {                     //если конец игры
	    gameOver( shell, display, String.valueOf( MySnake.getPoints( ) ), mainShell );
	    return;
    }
	while(true){
	if( MySnake.getLife() == true ) {
		MySnake.animate(null);  
		}
	else{
		MySnake.repFileClose();
	break;}
	}
}
}
}