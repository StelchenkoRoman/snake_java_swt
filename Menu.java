import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

//import PlayWindow.SnakeAnimate;

/**
 * this class describes menu window
 * @author green
 * @version 0.1
 */
public class Menu {
  private static Table table;
  private static Label scalaLabel;
  private static Label javaLabel;
  private static int level,criterion;
  private static PlayWindow play;
  private static String []fileNames;
  private static Color textColor, buttonColor,backGroundColor;
  private static Device device  =  Display.getCurrent ( );
  private static int playerMode = 0 , botMode = 1, loadMode = 2, replayMode = 3;
  Menu( ) {												//конструктор класса Menu
	final  Display display  =  new Display( );
	final Shell shell  =  new Shell( display );
	play = new PlayWindow( shell, display );
	shell.setSize( 300, 500 );                       // установка размеров окна
	openMenu( shell, display );                       // вызов метода openMenu
	shell.open( );
	while ( !shell.isDisposed( ) ) {
	  if ( !display.readAndDispatch( ) ) {
	    display.sleep( );
	  }
    }
	display.dispose( );
  }
  /** It opens a window to the main menu */
  public static void openMenu( final Shell shell, final Display display ) {  // метод openMenu
	for ( Control kid : shell.getChildren( ) ) {                         // цикл очищает все ранее созданные обьекты в окне shell
	  kid.dispose( ); 
	}
	shell.setText( "Snake" );
	textColor = new Color ( device, 0, 0, 0);
	buttonColor  =  new Color ( device, 255, 218, 185 );
	backGroundColor = new Color ( device, 224, 238, 224  );
	
	shell.setBackground( backGroundColor );	      
	Label image_label  =  new Label( shell, SWT.NONE );               // загрузка кортинки
	image_label.setImage( new Image( display, "images/home.png" ) );
	image_label.setBounds( 25, 20, 234, 148 );
	Button start_button  =  new Button( shell, SWT.PUSH );            //кнопка старт
	start_button.setText( "&START GAME" );                        //текст кнопки
	start_button.setBounds( 40, 170, 200, 50 );                   //размер и положение кнопки
	start_button.setBackground( buttonColor );                         //цвет фона
	start_button.setForeground( textColor );                         //цвет текста
	Listener startListener  =  new Listener( ) {                // действие на нажатие кнопки
	  public void handleEvent( Event event ) {
        openStartSettings( shell, display );
      }
	};
	start_button.addListener( SWT.Selection, startListener );   // привязка кнопки к действию
	
	Button rec_button  =  new Button( shell, SWT.None );    //кнопка рекорды
	rec_button.setText( "&RECORDS" );
	rec_button.setBackground( buttonColor );
	rec_button.setForeground( textColor );
	rec_button.setBounds( 40, 280, 200, 50 );
	Listener recListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    openRecords( shell, display );
	  }
	};
	rec_button.addListener( SWT.Selection, recListener );

	Button infoButton  =  new Button( shell, SWT.None );   //кнопка информация
    infoButton.setText( "&INFO" );
	infoButton.setBackground( buttonColor );
	infoButton.setForeground( textColor );
	infoButton.setBounds( 40, 335, 200, 50 );
	Listener infoListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    openInfo( shell, display );
	  }
	};
	infoButton.addListener( SWT.Selection, infoListener );

	Button botButton  =  new Button( shell, SWT.PUSH );
	botButton.setText( "BOT MODE" );
	botButton.setBackground( buttonColor );
	botButton.setForeground( textColor );
	botButton.setBounds( 40, 225, 200, 50);
	Listener  botListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
		     Random random=new Random();
			 String botName="bot"+String.valueOf((1+random.nextInt( 20 )));
			 play.open( shell, display, botName, 2,botMode,"");
			 openMenu(shell,display);
			 shell.setVisible( false ); 
			 }
    };
    botButton.addListener( SWT.Selection, botListener );

	Button genButton  =  new Button( shell, SWT.PUSH );
	genButton.setText( "GEN" );
	genButton.setBackground( buttonColor );
	genButton.setForeground( textColor );
	genButton.setBounds( 250, 170, 40, 20);
	genButton.setVisible(false);
	Listener  genListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {	
		  openGenerate(shell,display);
	  }
    };
    genButton.addListener( SWT.Selection, genListener );    
	Button exitButton  =  new Button( shell, SWT.PUSH );   //кнопка выход
	exitButton.setBackground( buttonColor );
	exitButton.setForeground( textColor );
	exitButton.setBounds( 40, 390, 200, 50 );
	exitButton.setText( "&EXIT" );
	Listener exitListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    System.exit( 0 );
	  }
    }; 
    exitButton.addListener( SWT.Selection, exitListener );
  }
  /**  Displays the table records in the window */
  public static void openRecords( final Shell shell, final Display display )	{ // метод открытия рекордов
	for ( Control kid : shell.getChildren( ) ) {
	  kid.dispose( );
	}
    shell.setBackground( backGroundColor );
    shell.setText( "RECORDS" );
    final  String filename[] = {"EASY.txt", "NORMAL.txt", "HARD.txt"};   // сохранение имен файлов в массив  
    Label combo_label  =  new Label( shell, SWT.None );
    combo_label.setText( "Select lvl  " );
    combo_label.setBounds( 55, 10, 70, 30 );
    final  Combo levelCombo  =  new Combo( shell, SWT.DROP_DOWN | SWT.READ_ONLY );
    levelCombo.setBounds( 130, 0, 100, 30 );
    levelCombo.setBackground(buttonColor);
    levelCombo.add( "EASY" );
    levelCombo.add( "NORMAL" );
    levelCombo.add( "HARD" );
    levelCombo.setText( "EASY" );
    scalaLabel  =  new Label( shell, SWT.None );
    scalaLabel.setBounds( 35, 380, 300, 20 );
    javaLabel  =  new Label( shell, SWT.None );
    javaLabel.setBounds( 35, 405, 300, 20 );
    final Button replayButton  =  new Button( shell, SWT.PUSH );
    replayButton.setText( "REPLAY" );
    replayButton.setBackground( buttonColor );
    replayButton.setForeground( textColor );
    replayButton.setBounds( 103, 430, 90, 30 );
    replayButton.setEnabled(false);
    
    final Button statisticsButton  =  new Button( shell, SWT.PUSH );
    statisticsButton.setText( "STATISTICS" );
    statisticsButton.setBackground( buttonColor );
    statisticsButton.setForeground( textColor );
    statisticsButton.setBounds( 198, 430, 90, 30 );
    statisticsButton.setEnabled(false);
    Listener statisticsListener  =  new Listener( ) {
        public void handleEvent( Event event ) {
  	     if(table.getSelectionIndex()!=-1)
      	  play.statisticsOpen( shell, display, "", level,replayMode,fileNames[table.getSelectionIndex()]);
  	     openMenu(shell,display);
  	     shell.setVisible(false);
        }
      };
      statisticsButton.addListener( SWT.Selection, statisticsListener );
 
    Button backButton  =  new Button( shell, SWT.PUSH );
    backButton.setText( "BACK" );
    backButton.setBackground( buttonColor );
    backButton.setForeground( textColor );
    backButton.setBounds( 8, 430, 90, 30 );
    Listener backListener  =  new Listener( ) {
      public void handleEvent( Event event ) {
        openMenu( shell, display );
      }
    };
    Listener replayListener  =  new Listener( ) {
      public void handleEvent( Event event ) {
	     if(table.getSelectionIndex()!=-1)
    	  play.open( shell, display, "", level,replayMode,fileNames[table.getSelectionIndex()]);
	     openMenu(shell,display);
	     shell.setVisible(false);
      }
    };
    replayButton.addListener( SWT.Selection, replayListener );
    backButton.addListener( SWT.Selection, backListener );  
    Label sortLabel  =  new Label( shell, SWT.None );
    sortLabel.setText( "Criterion " );
    sortLabel.setBounds( 55, 45, 70, 30 );
    final  Combo sortingSel =  new Combo( shell, SWT.DROP_DOWN | SWT.READ_ONLY );
    sortingSel.setBounds( 130, 40, 100, 30 );
    sortingSel.setBackground(buttonColor);
    sortingSel.add( "SCORES" );
    sortingSel.add( "NAMES" );
    sortingSel.setText( "SCORES" );
    criterion=0;
    level=2;
    table  =  new Table( shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION );
    table.setLinesVisible( true );
    table.setHeaderVisible( true );
    final  String[] titles  =  { "Name ", "Record" };
    for ( int i  =  0;  i < titles.length;  i++ ) {
	  TableColumn column  =  new TableColumn( table, SWT.NONE );
	  column.setText( titles[i] );
	}
    fillTable( table,level, titles.length,criterion );
    table.addSelectionListener( new SelectionAdapter( ) {
  	  public void widgetSelected( SelectionEvent e ) {
  		 statisticsButton.setEnabled(true);
  		 replayButton.setEnabled(true);
  	  }   
  	});
    
    levelCombo.addSelectionListener( new SelectionAdapter( ) {
	  public void widgetSelected( SelectionEvent e ) {
		table.clearAll( );
	   	table.setItemCount( 0 );
		statisticsButton.setEnabled(false);
  		replayButton.setEnabled(false);	
		if ( levelCombo.getText( ).equals( "EASY" ) ) {
		  level=2;
		  fillTable( table, level, titles.length, criterion );       	 
	    } else if ( levelCombo.getText( ).equals( "NORMAL" ) ) {
	        level=1;
	    	fillTable( table,level, titles.length, criterion );
	      } else {
	    	  level=0;
	          fillTable( table,level, titles.length, criterion);
	        }
      }
     });
	 sortingSel.addSelectionListener( new SelectionAdapter( ) {
	   public void widgetSelected( SelectionEvent e ) {
	     table.clearAll( );
		 table.setItemCount( 0 );
		 statisticsButton.setEnabled(false);
  		 replayButton.setEnabled(false);
		 if ( sortingSel.getText( ).equals( "SCORES" ) ) {
			 criterion=0;
			 fillTable( table, level, titles.length, criterion );       	 
		   } else if ( sortingSel.getText( ).equals( "NAMES" ) ) {
		       criterion=1;
		       fillTable( table,level, titles.length, criterion );
		     }
	  }
	});
  }
  /**  displays in the window information about the game*/
  public static void openInfo( final Shell shell, final Display display ) {
	for ( Control kid : shell.getChildren( ) ) {
      kid.dispose( );
    }
    shell.setForeground( textColor );
    shell.setBackground( backGroundColor );
    shell.setText( "Information" );
    
    Button exitButton  =  new Button( shell, SWT.PUSH );
    exitButton.setText( "EXIT" );
    exitButton.setBackground( buttonColor );
    exitButton.setForeground( textColor );
    exitButton.setBounds( 155, 430, 100, 30 );
 
    Button backButton  =  new Button( shell, SWT.PUSH );
    backButton.setText( "BACK" );
    backButton.setBackground( buttonColor );
    backButton.setForeground( textColor );
    backButton.setBounds( 35, 430, 100, 30 );
    
    String info = "Игрок управляет длинным, тонким существом, напоминающим змею, " +
    		      "которое ползает по плоскости, собирая предметы, избегая столкновения" +
    		      " с собственным хвостом и краями игрового поля. Каждый раз, когда змея" +
    		      " съедает кусок пищи, она становится длиннее, что постепенно усложняет" +
    		      " игру. Игрок управляет направлением движения головы змеи . Игрок" +
 	           	  " не может остановить движение змеи.";
    Label infoLabel  =  new Label( shell, SWT.WRAP | SWT.CENTER );
    infoLabel.setText( info );
    infoLabel.setBounds( 45, 20, 200, 320 );
    
    Listener  backListener  =  new Listener( ) {
      public void handleEvent( Event event ) {
  	    openMenu( shell, display );
      }
    };
    Listener exitListener  =  new Listener( ) {
      public void handleEvent( Event event ) {
        System.exit( 0 );
      }
    };
    exitButton.addListener( SWT.Selection, exitListener );
    backButton.addListener( SWT.Selection, backListener );        
  }
  
  /**  displayed starting settings the game*/
  public static void openStartSettings( final Shell shell, final Display display ) {
	for ( Control kid : shell.getChildren( ) ) {
	  kid.dispose( );
	}
    level = 2;
	shell.setForeground( textColor );
	shell.setBackground( backGroundColor ); 
	shell.setText( "New game" );
	
	Label selectLevelLabel  =  new Label( shell, SWT.None );
	selectLevelLabel.setText( "Select level  " );
	selectLevelLabel.setBounds( 50, 105, 100, 30 );
	
	final Combo selectLevel  =  new Combo( shell, SWT.DROP_DOWN | SWT.READ_ONLY );
	selectLevel.setBounds( 130, 100, 100, 30 );
	selectLevel.setBackground(buttonColor);
	selectLevel.add( "EASY" );
	selectLevel.add( "NORMAL" );
	selectLevel.add( "HARD" );
	selectLevel.setText( "EASY" );
	selectLevel.addSelectionListener( new SelectionAdapter( ) {
	  public void widgetSelected( SelectionEvent e ) {
	    if ( selectLevel.getText( ).equals( "NORMAL" ) ) {
	   	  level = 1;
	    } else if( selectLevel.getText( ).equals( "HARD" ) ) {
	        level = 0;
	      } else level = 2;
	  }
	});
	Label playerNameLabel  =  new Label( shell, SWT.None );
	playerNameLabel.setText( "Your name :  " );
	playerNameLabel.setBounds( 50, 155, 70, 30 );
	final Text playerName  =  new Text ( shell, SWT.BORDER );
	playerName.setBounds( 130, 150, 100, 30 );
	playerName.setBackground(buttonColor);
    Button startButton  =  new Button( shell, SWT.PUSH );
	startButton.setText( "&START" );
	startButton.setBackground( buttonColor );
	startButton.setForeground( textColor );
	startButton.setBounds( 155, 430, 100, 30 );
	
	Button backButton  =  new Button( shell, SWT.PUSH );
	backButton.setText( "BACK" );
	backButton.setBackground( buttonColor );
	backButton.setForeground( textColor ); 
	backButton.setBounds( 35, 430, 100, 30);
	Listener backListener  =  new Listener( ) {
      public void handleEvent( Event event ) {
	   	openMenu( shell, display );
	  }
	};
	Listener startListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
        String name;
        if ( playerName.getCaretPosition( ) == 0 )
	      name = "( no name )";
	    else name = playerName.getText( );
	      play.open( shell, display, name, level,playerMode,"");
	    openMenu(shell,display);
	    shell.setVisible( false );
	  }
	};
	startButton.addListener( SWT.Selection, startListener );
	backButton.addListener( SWT.Selection, backListener );
  }
  /**  It reads the data from the file and fills the high score table*/
  public static void fillTable( Table table, int level,int titleLenght,int criterion) {   //считывание из файла и запись в таблицу
	File fName  =  new File( "files/names.txt" );
	int []levelNumbers=new int[3];
	try {
	  Scanner repSc = new Scanner( fName );
	  for( int i = 0 ; i<3 ; i++ )
	    levelNumbers[i]=repSc.nextInt();
	  repSc.close();
	}
	catch (IOException  e) { }
	int Size=levelNumbers[level]-1;
	if ( Size==0 ) return;
	fileNames=new String[Size];
	String []playerNames=new String[Size];
	int []scores=new int[Size];
	String []fileNamesJ=new String[Size];
	String []playerNamesJ=new String[Size];
	int []scoresJ=new int[Size];
	String name="";
	try{
	  SeekableByteChannel fHelpChannel = Files.newByteChannel(Paths.get("files/"+String.valueOf(level)+"_AllResults.txt"));
	  int fileSize = (int) fHelpChannel.size();
	  ByteBuffer bufferRead = ByteBuffer.allocate(fileSize);	
	  fHelpChannel.read(bufferRead);
	  bufferRead.flip();
	  char temp;		  
	  for (int i=0 ; i<Size ; i++) {
	    while ( true ) {
	      temp = bufferRead.getChar();
		  if ( temp == '+')
		  	break;
		  name += String.valueOf(temp);
		}
		fileNames[i]=fileNamesJ[i]=name;
		name="";
		while ( true ) {
		  temp = bufferRead.getChar();
		  if ( temp == '+')
		  	break;
		  name += String.valueOf(temp);
	    }
		playerNames[i]=playerNamesJ[i]=name;
		name="";
		scores[i]=scoresJ[i]=bufferRead.getInt();
		bufferRead.getChar();
      }
	  fHelpChannel.close();
	} catch (IOException e) {
	    e.printStackTrace();
	  }
	long startTimeSc=0,endTimeSc=0,startTimeJ=0,endTimeJ=0;
	if ( Size > 0) {
	  if(criterion==0) {
		Sorting sortReplays = new Sorting();    
		startTimeSc = System.nanoTime();
	    sortReplays.SCsort(fileNames, playerNames, scores);
	    endTimeSc = System.nanoTime()-startTimeSc;
	    SortingJ sortJava=new SortingJ();
	    startTimeJ = System.nanoTime();
	    sortJava.SCsort(scoresJ, fileNamesJ, playerNamesJ, 0, Size-1);  
	 	endTimeJ = System.nanoTime()-startTimeJ;
	  } else  {
		  Sorting sortReplays = new Sorting();
		  startTimeSc = System.nanoTime();
	 	  sortReplays.AZsort(fileNames, playerNames, scores);
	 	  endTimeSc = System.nanoTime()-startTimeSc;
	 	  SortingJ sortJava=new SortingJ();
	 	  startTimeJ = System.nanoTime();
	 	  sortJava.AZSort(scoresJ, fileNamesJ, playerNamesJ, 0, Size-1);  
	 	  endTimeJ = System.nanoTime()-startTimeJ;
	    }		
	  scalaLabel.setText( "Scala time (ns): " + String.valueOf(endTimeSc));
	  javaLabel.setText( "Javas time (ns): " + String.valueOf(endTimeJ));
      for ( int i  =  0; i < fileNames.length; i++ ) {
        TableItem item  =  new TableItem( table, SWT.NONE );
        item.setText( 0, playerNames[i] );
        item.setText( 1, String.valueOf(scores[i]));
      }
      for ( int i = 0; i<titleLenght; i++ ) {
        table.getColumn ( i ).pack ( );
      }
    table.setBounds( 45, 80, 200, 280 );
  }
}  
  public static void openGenerate(final Shell shell, final Display display ) {
    final Snake MySnake=new Snake();
	final Random random=new Random();    	 	  	
  	for ( Control kid : shell.getChildren( ) ) {
	  kid.dispose( );
	}
    level = 2;
	shell.setForeground( textColor );
	shell.setBackground( backGroundColor ); 
	shell.setText( "Generate" );
	final Label numberLabel  =  new Label( shell, SWT.None );
	numberLabel.setText( "Input numbers :  " );
	numberLabel.setBounds( 50, 155, 70, 30 );
	final Text numberText  =  new Text ( shell, SWT.BORDER );
	numberText.setBounds( 130, 150, 100, 30 );
	numberText.setBackground(buttonColor);
    Button startButton  =  new Button( shell, SWT.PUSH );
	startButton.setText( "&START" );
	startButton.setBackground( buttonColor );
	startButton.setForeground( textColor );
	startButton.setBounds( 155, 430, 100, 30 );
	final ProgressBar bar = new ProgressBar(shell, SWT.SMOOTH);
	bar.setBounds(10, 10, 200, 32);
	bar.setSelection(0);
		
	Button backButton  =  new Button( shell, SWT.PUSH );
	backButton.setText( "BACK" );
	backButton.setBackground( buttonColor );
	backButton.setForeground( textColor ); 
	backButton.setBounds( 35, 430, 100, 30);
	final Runnable runnable  =  new Runnable( ) {
	public void run( ) {
	  String botName;               
	  bar.setMaximum(Integer.valueOf(numberText.getText()));
	  for ( int i=0; i<Integer.valueOf(numberText.getText()); i++ ) {
		botName="bot"+String.valueOf((1+random.nextInt( 20 )));			
		MySnake.startValue( 2,botMode,botName ,"");
	    if ( !MySnake.getLife( ) ) {                    
	      return;
	  }
	  while ( true ) {
		if ( MySnake.getLife() == true ) {
		  MySnake.animate(null);  
		}
		else {
		  MySnake.repFileClose();
		  break;
		}
	  }	
	  bar.setSelection(i);              
    }}
	};
	Listener backListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    openMenu( shell, display );
 	  }
	};
    Listener startListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {	  
        display.asyncExec(runnable);
      }
	};
	startButton.addListener( SWT.Selection, startListener );
	backButton.addListener( SWT.Selection, backListener );			
  }
}