import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * this class describes menu window
 * @author green
 * @version 0.1
 */
public class Menu {
  private static Table table;
  private static int level;
  private static PlayWindow play;
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
	Color red  =  display.getSystemColor( SWT.COLOR_RED );
	Color white  =  display.getSystemColor( SWT.COLOR_WHITE );
	Color green  =  display.getSystemColor( SWT.COLOR_GREEN );
	Color yellow  =  display.getSystemColor( SWT.COLOR_YELLOW );
	Color cyan  =  display.getSystemColor( SWT.COLOR_CYAN );
	Color black  =  display.getSystemColor( SWT.COLOR_BLACK );
	shell.setBackground( white );	      
	Label image_label  =  new Label( shell, SWT.NONE );               // загрузка кортинки
	image_label.setImage( new Image( display, "/home/green/im.png" ) );
	image_label.setBounds( 25, 20, 234, 148 );
	Button start_button  =  new Button( shell, SWT.PUSH );            //кнопка старт
	start_button.setText( "&START GAME" );                        //текст кнопки
	start_button.setBounds( 40, 170, 200, 50 );                   //размер и положение кнопки
	start_button.setBackground( green );                         //цвет фона
	start_button.setForeground( black );                         //цвет текста
	Listener startListener  =  new Listener( ) {                // действие на нажатие кнопки
	  public void handleEvent( Event event ) {
        openStartSettings( shell, display );
      }
	};
	start_button.addListener( SWT.Selection, startListener );   // привязка кнопки к действию

	Button replay_button  =  new Button( shell, SWT.None );    //сохраненные игры
	replay_button.setText( "&REPLAY GAME" );
	replay_button.setBackground( cyan );
	replay_button.setForeground( black );
	replay_button.setBounds( 40, 225, 200, 50 );
	Listener replayListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
		  File file  =  new File( "replay.txt" );
		  try {
		    Scanner sc = new Scanner(file);
		    level = sc.nextInt();
		    sc.close();
		    }
		    catch (IOException  e) {
		        e.printStackTrace();
		    }
		 play.open( shell, display, "", level,replayMode);
		 shell.setVisible( false );
		     }
	};
	replay_button.addListener( SWT.Selection, replayListener );

	Button rec_button  =  new Button( shell, SWT.None );    //кнопка рекорды
	rec_button.setText( "&RECORDS" );
	rec_button.setBackground( cyan );
	rec_button.setForeground( black );
	rec_button.setBounds( 40, 280, 200, 50 );
	Listener recListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    openRecords( shell, display );
	  }
	};
	rec_button.addListener( SWT.Selection, recListener );

	Button infoButton  =  new Button( shell, SWT.None );   //кнопка информация
    infoButton.setText( "&INFO" );
	infoButton.setBackground( yellow );
	infoButton.setForeground( black );
	infoButton.setBounds( 40, 335, 200, 50 );
	Listener infoListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
	    openInfo( shell, display );
	  }
	};
	infoButton.addListener( SWT.Selection, infoListener );

	Button exitButton  =  new Button( shell, SWT.PUSH );   //кнопка выход
	exitButton.setBackground( red );
	exitButton.setForeground( black );
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
    Color cyan  =  display.getSystemColor( SWT.COLOR_CYAN );
    Color red  =  display.getSystemColor( SWT.COLOR_RED );
    Color black  =  display.getSystemColor( SWT.COLOR_BLACK );
    Color white  =  display.getSystemColor( SWT.COLOR_WHITE );
    shell.setForeground( cyan );
    shell.setBackground( cyan );
    shell.setText( "RECORDS" );
    final  String filename[] = {"EASY.txt", "NORMAL.txt", "HARD.txt"};   // сохранение имен файлов в массив  
    Label combo_label  =  new Label( shell, SWT.None );
    combo_label.setText( "Select lvl  " );
    combo_label.setBounds( 55, 10, 70, 30 );
    final  Combo levelCombo  =  new Combo( shell, SWT.DROP_DOWN | SWT.READ_ONLY );
    levelCombo.setBounds( 130, 0, 100, 30 );
    levelCombo.add( "EASY" );
    levelCombo.add( "NORMAL" );
    levelCombo.add( "HARD" );
    levelCombo.setText( "EASY" );
    Button exitButton  =  new Button( shell, SWT.PUSH );
    exitButton.setText( "EXIT" );
    exitButton.setBackground( red );
    exitButton.setForeground( black );
    exitButton.setBounds( 155, 350, 100, 50 );
    Button backButton  =  new Button( shell, SWT.PUSH );
    backButton.setText( "BACK" );
    backButton.setBackground( white );
    backButton.setForeground( black );
    backButton.setBounds( 35, 350, 100, 50 );
    Listener backListener  =  new Listener( ) {
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
    table  =  new Table( shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION );
    table.setLinesVisible( true );
    table.setHeaderVisible( true );
    final  String[] titles  =  { "Name ", "Record" };
    for ( int i  =  0;  i < titles.length;  i++ ) {
	  TableColumn column  =  new TableColumn( table, SWT.NONE );
	  column.setText( titles[i] );
	}
	fillTable( table, filename[0], titles.length );
	levelCombo.addSelectionListener( new SelectionAdapter( ) {
	  public void widgetSelected( SelectionEvent e ) {
	    if ( levelCombo.getText( ).equals( "EASY" ) ) {
	   	  table.clearAll( );
	   	  table.setItemCount( 0 );
	  	  fillTable( table, filename[0], titles.length );       	 
	    } else if ( levelCombo.getText( ).equals( "NORMAL" ) ) {
	        table.clearAll( );
	        table.setItemCount( 0 );
	        fillTable( table, filename[1], titles.length );
	      } else {
	          table.clearAll( );
	          table.setItemCount( 0 );
	          fillTable( table, filename[2], titles.length );
	        }
      }
     });
  }
  /**  displays in the window information about the game*/
  public static void openInfo( final Shell shell, final Display display ) {
	for ( Control kid : shell.getChildren( ) ) {
      kid.dispose( );
    }
    Color yellow  =  display.getSystemColor( SWT.COLOR_YELLOW );
    Color red  =  display.getSystemColor( SWT.COLOR_RED );
    Color black  =  display.getSystemColor( SWT.COLOR_BLACK );
    Color white  =  display.getSystemColor( SWT.COLOR_WHITE );
    shell.setForeground( yellow );
    shell.setBackground( yellow );
    shell.setText( "Information" );
    
    Button exitButton  =  new Button( shell, SWT.PUSH );
    exitButton.setText( "EXIT" );
    exitButton.setBackground( red );
    exitButton.setForeground( black );
    exitButton.setBounds( 155, 350, 100, 50 );
 
    Button backButton  =  new Button( shell, SWT.PUSH );
    backButton.setText( "BACK" );
    backButton.setBackground( white );
    backButton.setForeground( black );
    backButton.setBounds( 35, 350, 100, 50 );
    
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
	Color green  =  display.getSystemColor( SWT.COLOR_GREEN );
	Color red  =  display.getSystemColor( SWT.COLOR_RED );
	Color white  =  display.getSystemColor( SWT.COLOR_WHITE );
	Color black  =  display.getSystemColor( SWT.COLOR_BLACK ); 
	shell.setForeground( green );
	shell.setBackground( green ); 
	shell.setText( "New game" );
	
	Label selectLevelLabel  =  new Label( shell, SWT.None );
	selectLevelLabel.setText( "Select level  " );
	selectLevelLabel.setBounds( 50, 105, 100, 30 );
	
	final Combo selectLevel  =  new Combo( shell, SWT.DROP_DOWN | SWT.READ_ONLY );
	selectLevel.setBounds( 130, 100, 100, 30 );
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
	
    Button startButton  =  new Button( shell, SWT.PUSH );
	startButton.setText( "&START" );
	startButton.setBackground( red );
	startButton.setForeground( black );
	startButton.setBounds( 155, 280, 100, 50 );
	
	Button loadButton  =  new Button( shell, SWT.PUSH );
	loadButton.setText( "&LOAD GAME" );
	loadButton.setBackground( red );
	loadButton.setForeground( black );
	loadButton.setBounds( 35, 280, 100, 50 );
	
	Button botButton  =  new Button( shell, SWT.PUSH );
	botButton.setText( "BOT MODE" );
	botButton.setBackground( white );
	botButton.setForeground( black );
	botButton.setBounds( 155, 350, 100, 50);
	Listener  botListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
		 play.open( shell, display, "bot", 2,botMode);
		 openMenu(shell,display);
		 shell.setVisible( false );
      }
    };
	Button backButton  =  new Button( shell, SWT.PUSH );
	backButton.setText( "BACK" );
	backButton.setBackground( white );
	backButton.setForeground( black ); 
	backButton.setBounds( 35, 350, 100, 50 );
	Listener backListener  =  new Listener( ) {
      public void handleEvent( Event event ) {
	   	openMenu( shell, display );
	  }
	};
	Listener loadListener  =  new Listener( ) {
	      public void handleEvent( Event event ) {
	    	  String name="";
	    	  File file  =  new File( "names.txt" );
			  try {
			    Scanner sc = new Scanner(file);
			    name = sc.nextLine();
			    level = sc.nextInt();
			    sc.close();
			    }
			    catch (IOException  e) {
			        e.printStackTrace();
			    }
			 play.open( shell, display, name, level,loadMode);
			 openMenu(shell,display);
			 shell.setVisible( false );
			     }      
		};	
	Listener startListener  =  new Listener( ) {
	  public void handleEvent( Event event ) {
        String name;
        if ( playerName.getCaretPosition( ) == 0 )
	      name = "( no name )";
	    else name = playerName.getText( );
	      play.open( shell, display, name, level,playerMode );
	    openMenu(shell,display);
	    shell.setVisible( false );
	  }
	};
	startButton.addListener( SWT.Selection, startListener );
	loadButton.addListener( SWT.Selection, loadListener );
	botButton.addListener( SWT.Selection, botListener );
	backButton.addListener( SWT.Selection, backListener );
  }
  /**  It reads the data from the file and fills the high score table*/
  public static void fillTable( Table table, String filename, int k ) {   //считывание из файла и запись в таблицу
	String name[] = new String[10], rec[] = new String[10];
    try{
	  BufferedReader myfile  =  new BufferedReader ( new FileReader( filename ) );
	  try {
		for( int i = 0; i<10; i++ ) {
		  name[i] = myfile.readLine( );
		  rec[i] = myfile.readLine( );
		}
      } finally {
		  myfile.close( );
		}
	} catch( IOException e ) {
	    throw new RuntimeException( e );
	  }
    for ( int i  =  0; i < 10; i++ ) {
      TableItem item  =  new TableItem( table, SWT.NONE );
      item.setText( 0, name[i] );
      item.setText( 1, rec[i] );
    }
    for ( int i = 0; i<k; i++ ) {
      table.getColumn ( i ).pack ( );
    }
    table.setBounds( 45, 40, 200, 280 );
  }
}