import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class my {


  public static void main(String[] args) {
	 
	  Menu menu=new Menu();

}
}
class Menu
{ 	private static Table table;
	private  static int kl;
    private static PlayWindow play;
	Menu()
	{
		
		  final  Display display = new Display();
	      final Shell shell = new Shell(display);      
	      play=new PlayWindow(shell,display);
	      shell.setSize(300, 450);
	      openMenu(shell,display);
	      shell.open();
	      while (!shell.isDisposed()) {
		        if (!display.readAndDispatch()) {
		                display.sleep();
		            }
		       // if(play.newGameFlag)
		        //	openMenu(shell,display);
		        } 
		        display.dispose();
	
	}
public static void openMenu(final Shell shell,final Display display)
{
	  for (Control kid : shell.getChildren())
		{
		kid.dispose();
		}
		
		   // final SnakeStartSettings startSettings=new SnakeStartSettings();
		    //final SnakeRecords records =new SnakeRecords();
		    //final SnakeInfo info=new SnakeInfo();
		    shell.setText("Snake");
		    Color red = display.getSystemColor(SWT.COLOR_RED);
	        Color white = display.getSystemColor(SWT.COLOR_WHITE);
	        Color green = display.getSystemColor(SWT.COLOR_GREEN);
	        Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
	        Color cyan = display.getSystemColor(SWT.COLOR_CYAN);
	        Color black = display.getSystemColor(SWT.COLOR_BLACK);       
	        shell.setBackground(white);
	        
	        
	        Label image_label = new Label(shell, SWT.NONE);            
	        image_label.setImage(new Image(display,"/home/green/im.png"));
	        image_label.setBounds(25, 20, 234, 148);
	      
	        Button start_button = new Button(shell, SWT.PUSH);
	        start_button.setText("&START GAME");  
	        start_button.setBounds(40,170 ,200 , 50);
	        start_button.setBackground(green);
	        start_button.setForeground(black);
	        Listener startListener = new Listener() {
	            public void handleEvent(Event event) {
	              openStartSettings(shell,display);
	              
	            }
	          };    
	        start_button.addListener(SWT.Selection, startListener);

	        Button rec_button = new Button(shell, SWT.None);
	        rec_button.setText("&RECORDS");
	        rec_button.setBackground(cyan);
	        rec_button.setForeground(black);
	        rec_button.setBounds(40,225 ,200 ,50 );
	        Listener recListener = new Listener() {
	            public void handleEvent(Event event) {
	            openRecords(shell,display);
	            }
	          };    
	          rec_button.addListener(SWT.Selection, recListener);

	          Button infoButton = new Button(shell, SWT.None);
		        infoButton.setText("&INFO");
		        infoButton.setBackground(yellow);
		        infoButton.setForeground(black);
		        infoButton.setBounds(40,280 ,200 , 50);
		        Listener infoListener = new Listener() {
		            public void handleEvent(Event event) {
		              openInfo(shell,display);
		        
		            }
		          };    
		        infoButton.addListener(SWT.Selection, infoListener);

		        Button exitButton = new Button(shell, SWT.PUSH);  
		        exitButton.setBackground(red);
		        exitButton.setForeground(black); 
		        exitButton.setBounds(40,335 ,200 , 50);
		        exitButton.setText("&EXIT");
		        Listener exitListener = new Listener() {
		            public void handleEvent(Event event) {
		             System.exit(0);
		            }
		          };  		          
		      exitButton.addListener(SWT.Selection, exitListener);
	}
public static void openRecords(final Shell shell,final Display display)
	{
	  for (Control kid : shell.getChildren())
		{
		kid.dispose();
		}
			
    Color cyan = display.getSystemColor(SWT.COLOR_CYAN);
    Color red = display.getSystemColor(SWT.COLOR_RED);
    Color black = display.getSystemColor(SWT.COLOR_BLACK);
    Color white = display.getSystemColor(SWT.COLOR_WHITE);
    shell.setForeground(cyan);
    shell.setBackground(cyan);
    shell.setText("RECORDS");
    
    final  String filename[]={"EASY.txt","NORMAL.txt","HARD.txt"};
    
    Label combo_label = new Label(shell, SWT.None);           
    combo_label.setText("Select lvl  ");
    combo_label.setBounds(55, 10, 70, 30);
    
    final  Combo levelCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
    levelCombo.setBounds(130, 0, 100, 30);
    levelCombo.add("EASY");
    levelCombo.add("NORMAL");
    levelCombo.add("HARD");
    levelCombo.setText("EASY");
    
    Button exitButton = new Button(shell, SWT.PUSH);
    exitButton.setText("EXIT");
    exitButton.setBackground(red);
    exitButton.setForeground(black);
    exitButton.setBounds(155,350 ,100 , 50);
    
    Button backButton = new Button(shell, SWT.PUSH);
    backButton.setText("BACK");
    backButton.setBackground(white);
    backButton.setForeground(black);
    backButton.setBounds(35,350 ,100 , 50);   
    Listener backListener = new Listener() {
        public void handleEvent(Event event) {
        openMenu(shell,display);
        }
      }; 
      
     Listener exitListener = new Listener() {
	            public void handleEvent(Event event) {
	             System.exit(0);
	            }
	          };  		          
	      exitButton.addListener(SWT.Selection, exitListener);
      backButton.addListener(SWT.Selection, backListener);
      
        table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	        table.setLinesVisible(true);
	        table.setHeaderVisible(true);
	        final  String[] titles = { "Name ", "Record" };
	        for (int i = 0; i < titles.length; i++) 
	          {
	            TableColumn column = new TableColumn(table, SWT.NONE);
	            column.setText(titles[i]);
	          }
	        fillTable(table,filename[0],titles.length);
	         
	        levelCombo.addSelectionListener(new SelectionAdapter() {
	            public void widgetSelected(SelectionEvent e) {
	              if (levelCombo.getText().equals("EASY")) {
	            	  table.clearAll();
	            	  table.setItemCount(0);
	            	  fillTable(table,filename[0],titles.length);
	            	 
	              } else if (levelCombo.getText().equals("NORMAL")) {
	            	  table.clearAll();
	            	  table.setItemCount(0);
	            	  fillTable(table,filename[1],titles.length);
	              } else {
	            	  table.clearAll();
	            	  table.setItemCount(0);
	            	  fillTable(table,filename[2],titles.length);
	              }
	            }
	          });	        			          
}	
public static void openInfo(final Shell shell,final Display display)
	{
	  for (Control kid : shell.getChildren())
	   {
     	kid.dispose();
    }
	
 Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
 Color red = display.getSystemColor(SWT.COLOR_RED);
 Color black = display.getSystemColor(SWT.COLOR_BLACK);
 Color white = display.getSystemColor(SWT.COLOR_WHITE);
 shell.setForeground(yellow);
 shell.setBackground(yellow);
 shell.setText("Information");
 
 Button exitButton = new Button(shell, SWT.PUSH);
 exitButton.setText("EXIT");
 exitButton.setBackground(red);
 exitButton.setForeground(black);
 exitButton.setBounds(155,350 ,100 , 50);
 
 Button backButton = new Button(shell, SWT.PUSH);
 backButton.setText("BACK");
 backButton.setBackground(white);
 backButton.setForeground(black);
 backButton.setBounds(35,350 ,100 , 50);    
 
 String info="Игрок управляет длинным, тонким существом, напоминающим змею, которое ползает по плоскости" +
 		", собирая предметы, избегая столкновения с собственным" +
 		" хвостом и краями игрового поля. Каждый раз, когда змея съедает кусок пищи, она становится " +
 		"длиннее, что постепенно усложняет игру. Игрок управляет " +
 		"направлением движения головы змеи . Игрок" +
 		" не может остановить движение змеи.";
 Label infoLabel = new Label(shell, SWT.WRAP | SWT.CENTER);           
 infoLabel.setText(info);
 infoLabel.setBounds(45, 20, 200, 320);
 
 
 Listener  backListener = new Listener() {
     public void handleEvent(Event event) {
  	   openMenu(shell,display);
     }
   };    
 Listener exitListener = new Listener() {
         public void handleEvent(Event event) {
          System.exit(0);
         }
       };  		          
   exitButton.addListener(SWT.Selection, exitListener);
   backButton.addListener(SWT.Selection, backListener);       
}
public static void openStartSettings(final Shell shell,final Display display)
	{
	  for (Control kid : shell.getChildren())
			{
			kid.dispose();
			}
		kl=2;	
	    Color green = display.getSystemColor(SWT.COLOR_GREEN);
	    Color red = display.getSystemColor(SWT.COLOR_RED);
	    Color white = display.getSystemColor(SWT.COLOR_WHITE);
	    Color black = display.getSystemColor(SWT.COLOR_BLACK);
	    shell.setForeground(green);
	    shell.setBackground(green);
		shell.setText("New game");
	    
		Label selectLevelLabel = new Label(shell, SWT.None);           
	    selectLevelLabel.setText("Select lvl  ");
	    selectLevelLabel.setBounds(55, 105, 70, 30);
	    
	    final Combo selectLevel = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    selectLevel.setBounds(130, 100, 100, 30);
	    selectLevel.add("EASY");
	    selectLevel.add("NORMAL");
	    selectLevel.add("HARD");
	    selectLevel.setText("EASY");
	    selectLevel.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {    
	         if (selectLevel.getText().equals("NORMAL")) {
	        	  kl=1;
	          } else if(selectLevel.getText().equals("HARD")){
	        	  kl=0;
	          } else kl=2;
	        }
	      });
	    
	    
	    Label playerNameLabel = new Label(shell, SWT.None);           
	    playerNameLabel.setText("Your name :  ");
	    playerNameLabel.setBounds(55, 155, 70, 30);
	    final Text playerName = new Text (shell, SWT.BORDER);
	    playerName.setBounds(130, 150, 100,30);
	     
	    
	    Button startButton = new Button(shell, SWT.PUSH);
	    startButton.setText("&START");
	    startButton.setBackground(red);
	    startButton.setForeground(black);
	    startButton.setBounds(155,350 ,100 , 50);
	    
	    Button backButton = new Button(shell, SWT.PUSH);
	    backButton.setText("BACK");
	    backButton.setBackground(white);
	    backButton.setForeground(black);
	    backButton.setBounds(35,350 ,100 , 50);   
	    Listener backListener = new Listener() {
	        public void handleEvent(Event event) {  	
	        	openMenu(shell,display);
	        }
	      };     
	  
	      
	        
	     Listener startListener = new Listener() {
	            public void handleEvent(Event event) {
	            		
                   String name;	            	
	             	 if(playerName.getCaretPosition()==0)
	                	name="(no name)";
	             	 else name=playerName.getText();
	             	 play.open(shell, display,name,kl);
	            	shell.setVisible(false);
	            }
	          };  		          
	      startButton.addListener(SWT.Selection, startListener);
	      backButton.addListener(SWT.Selection, backListener);
	}
public static void fillTable(Table table,String filename,int k){
	  String name[]=new String[10],rec[]=new String[10];
     try{
			 BufferedReader myfile = new BufferedReader (new FileReader(filename));	
			   try {
				 for(int i=0;i<10;i++) 
		       {
					 name[i]=myfile.readLine();
				     rec[i]=myfile.readLine();
			   }
			   } finally {
		           
		            myfile.close();
		        }
			 
		 } catch(IOException e) {
		        throw new RuntimeException(e);
		    }
      for (int i = 0; i < 10; i++) {
          TableItem item = new TableItem(table, SWT.NONE);
          item.setText(0, name[i]);
          item.setText(1, rec[i]);
        }
      for (int i=0; i<k; i++) {
          table.getColumn (i).pack ();
        }   
        table.setBounds(45,40, 200, 280);		  
}		          
	        	
}


class Coords
{
	public  int x;
	public  int y;
	public void value(int xValue,int yValue)
	   {
		 x=xValue;
		 y=yValue;
	   }	
}


class PlayWindow 
{private  final int IMAGE_WIDTH = 20;
private  int TIMER_INTERVAL ;
private  Label scoreLabel;
private Canvas canvas;
private  Shell shell;
private  Shell mainShell;
private  Display display;
private String playerName;
private int level;
private String nameFile;
private Button pauseButton; 
private Button newGameButton;
private boolean pauseFlag;
public boolean newGameFlag;
private Snake MySnake=new Snake();
Device device = Display.getCurrent ();
	PlayWindow(){}
	PlayWindow( final Shell mainShell1,final Display display1 )
	{
		display=display1;
		mainShell=mainShell1;
	  Color green = display1.getSystemColor(SWT.COLOR_GREEN);      
	  shell = new Shell(mainShell1,SWT.APPLICATION_MODAL | SWT.SHELL_TRIM);
	  shell.setText("Snake");
	  shell.setSize(620,470);
	  shell.setMinimumSize(620, 470);
	  pauseFlag=false;
	  
	  Color yellow = new Color (device,255, 250, 205 );
	 
	  shell.setBackground(yellow);
	}
	
	
	final Runnable runnable = new Runnable() {
	    public void run() {
	  	  if(!MySnake.getLife())
	  	  {   
	  		  gameOver(shell,display,String.valueOf(MySnake.getPoints()),mainShell);
	  		  return;
	  	  }
			        if(!shell.isDisposed())
			        	if(!pauseFlag)
	      animate();
	  	display.timerExec(TIMER_INTERVAL, this);  
	  	if(!MySnake.getLife())
	      return;
	    }
	    
	  };
	  
	
public void open(final Shell mainShell,final Display display,final String name , int lvl )
{
		 for (Control kid : shell.getChildren())
			{
			kid.dispose();
			}
		newGameFlag=false;
		MySnake.startValue(); 
		level=lvl;
		playerName=name;
		
	    if(level==0)
		  { 
			  TIMER_INTERVAL =50;
			  nameFile="HARD.txt";
		  }		
	    if(level==1)
		  {
			  TIMER_INTERVAL =75;
			  nameFile="NORMAL.txt";
		  }
	    if(level==2)
			  {
				  nameFile="EASY.txt";
				  TIMER_INTERVAL =100;
			  }
	  
	    Color red = new Color (device, 250, 128, 114 );
	    Color white = new Color (device, 255, 255, 255 );
	    Color black = new Color (device,0, 0, 0 );
		  
	     
	        Button exitButton = new Button(shell, SWT.PUSH);
	        exitButton.setText("EXIT");
	        exitButton.setBackground(red);
	        exitButton.setForeground(black);
	        exitButton.setBounds(500,414 ,100 , 25);    
	       
	        final Button continueButton = new Button(shell, SWT.PUSH);
	        continueButton.setText("CONTINUE");
	        continueButton.setBackground(red);
	        continueButton.setForeground(black);
	        continueButton.setBounds(260,414 ,100 , 25);    
	        continueButton.setVisible(false);
	        
	      
	        pauseButton = new Button(shell, SWT.PUSH); 
	        pauseButton.setText("PAUSE");
	        pauseButton.setBackground(red);
	        pauseButton.setForeground(black);
	        pauseButton.setBounds(260,414 ,100 , 25);    
	 
	        Button toMenuButton = new Button(shell, SWT.PUSH);
	        toMenuButton.setText("MENU");
	        toMenuButton.setBackground(red);
	        toMenuButton.setForeground(black);
	        toMenuButton.setBounds(380,414 ,100 , 25);    
	           
	        Listener  pauseListener = new Listener() {
	              public void handleEvent(Event event) {
	            	 pauseButton.setVisible(false);
	            	 continueButton.setVisible(true); 	
	            	 pauseFlag=true;
	              }
	            };   
	           
	            Listener  continueListener = new Listener() {
		              public void handleEvent(Event event) {
		            	 pauseButton.setVisible(true);
		            	 continueButton.setVisible(false); 
		            	 pauseFlag=false;
		              }
		            }; 
	        Listener  exitListener = new Listener() {
		              public void handleEvent(Event event) {
		            	System.exit(0);	        
		              }
		            }; 
		     Listener  menuListener = new Listener() {
			              public void handleEvent(Event event) {
			            	 newGameFlag=true;
			            	 mainShell.setVisible(true);
			            	 recordsRefresh(nameFile,MySnake.getPoints(),playerName);
			            	 shell.setVisible(false);
			            	 return;
			              }
			            };          
			            newGameButton = new Button(shell, SWT.PUSH);
			            newGameButton.setText("NEW GAME");
			            newGameButton.setBackground(red);
			            newGameButton.setForeground(black);
			            newGameButton.setBounds(230,200 ,100 , 25);    
			          	newGameButton.setVisible(false);       
			    	    Listener  newGameListener = new Listener() {
			    	              public void handleEvent(Event event) {
			    	            	  mainShell.setVisible(true);
			    	            	  recordsRefresh(nameFile,MySnake.getPoints(),playerName);
			    	              	shell.setVisible(false);
			    	              }
			    	            };    
			    	      newGameButton.addListener(SWT.Selection,newGameListener);
			    		     
	             
	      pauseButton.addListener(SWT.Selection,pauseListener);
	      exitButton.addListener(SWT.Selection,exitListener);
	      toMenuButton.addListener(SWT.Selection,menuListener);
	      continueButton.addListener(SWT.Selection,continueListener);
			  
	      
		  scoreLabel = new Label(shell, SWT.None);           
		  scoreLabel.setText("Score :  "+ MySnake.getPoints());
		  scoreLabel.setBounds(10, 420, 70, 20);  
		  canvas = new Canvas(shell, SWT.NO_BACKGROUND);
		  canvas.setBounds(7, 10, 600, 400);
		  canvas.setBackground(white);

		  canvas.addPaintListener(new PaintListener() {
		  public void paintControl(PaintEvent event) {
		          event.gc.setBackground(event.display.getSystemColor(SWT.COLOR_GREEN));
		          event.gc.fillRectangle(MySnake.getFoodCoords().x, MySnake.getFoodCoords().y, 20, 20);
		      for(int i=0;i<MySnake.getSize();i++)
		        {      
		      	event.gc.setBackground(event.display.getSystemColor(SWT.COLOR_RED));
		          event.gc.fillRectangle(MySnake.getSnakeCoords(i).x, MySnake.getSnakeCoords(i).y,IMAGE_WIDTH, IMAGE_WIDTH);
		        }
		    }
		  });  
		  
			  Listener listenerKeyboard = new Listener(){
			  public void handleEvent(Event e) {
				  if(!pauseFlag)
			    	  MySnake.keyPressed(e.keyCode);
			  }
			  };			 
			 Display.getCurrent().addFilter(SWT.KeyDown, listenerKeyboard);
			  	    
			    shell.open();
			    display.timerExec(TIMER_INTERVAL, runnable);  
			   
	}
	
	  public  void recordsRefresh(String filename,int points,String playerName){
    	   String name[]=new String[10],record[]=new String[10];
	       try{
				 BufferedReader myfile = new BufferedReader (new FileReader(filename));	
				   try {
					 for(int i=0;i<10;i++) 
			       {
						 name[i]=myfile.readLine();
					     record[i]=myfile.readLine();
				   }
				   } finally {    
			            myfile.close();
			        }
			 } catch(IOException e) {
			        throw new RuntimeException(e);
			    }
	       if(points>=Integer.parseInt(record[9].trim()))
	       {    for(int i=9;i>=0;i--)
	       {
	    	   
	    	   if(points>=Integer.parseInt(record[i].trim()) &&
	    			   (i==0 || points<Integer.parseInt(record[i-1].trim()) ))
	    	   {
	    		   for(int j=9;j>i;j--)
	    		      {	   name[j]=name[j-1];
	    		           record[j]=record[j-1];
	    	          }
	    		   name[i]=playerName;
	    		   record[i]=String.valueOf(points);
	    	          break;
	    	   }
	    	   }
	    		    File file = new File(filename); 
	    		    try {    		 
	    		        //PrintWriter обеспечит возможности записи в файл
	    		        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
	    		        try {
	    		            //Записываем текст у файл
	    		        	for(int i=0;i<10;i++)
	    		        	{   out.println(name[i]);
	    		                out.println(record[i]);
	    		            }
	    		        } finally {
	    		            //После чего мы должны закрыть файл
	    		            //Иначе файл не запишется
	    		            out.close();
	    		        }
	    		    } catch(IOException e) {
	    		        throw new RuntimeException(e);
	    		    }
	       }}	    	
	  
	  public  void animate()
	  {	  
		MySnake.animate(scoreLabel);
	    canvas.redraw();
	  }

	  public  void gameOver(final Shell shell,final Display display,String points, final Shell mainShell)
	  {   

		    Color red = new Color (device, 250, 128, 114 );
		    Color black = new Color (device,0, 0, 0 );
			
		 	  
		canvas.dispose();
		scoreLabel.dispose();
		pauseButton.setVisible(false);
		shell.setText("Game over");
		Label gameOverLabel = new Label(shell, SWT.WRAP | SWT.CENTER);           
	    gameOverLabel.setText("Game over\n Your score : "+points );
	    gameOverLabel.setBounds(230, 150, 100, 100);
	    newGameButton.setVisible(true);   
	    
	  }
}

class Snake
{
      private     ArrayList<Coords> snakeCoords;
	  private  Coords foodCords;
	  private  Coords directionValue;
	  private  int points;
	  private  int direction;
	  private  int size;
	  private  boolean life;
	  private Random random;
	  Snake()
	  {
	      snakeCoords=new ArrayList<Coords>();
	      foodCords=new Coords();
	      directionValue=new Coords();
	      random= new Random();	 
	   }
	  public void newGame()
	  {
		  
	  }
  public boolean getLife()
  {
	return life;
  }
  public Coords getFoodCoords()
	  {
	  return foodCords;
	  }
  public Coords getDirectionValue()
  {
	  return directionValue;
  }
  public int getDirection()
  {
	  return direction;
  }
  public int getPoints()
  {
	  return points;
  }
  public int getSize()
  {
	return size;
  }
  public Coords getSnakeCoords(int index)
  {
	  return snakeCoords.get(index);
  }
  public void setDirection(int x)
  {
	  direction=x;
  }
  public void setDirectionValueX(int x)
  {
	  directionValue.x=x;
  }
  public void setDirectionValueY(int y)
  {
	  directionValue.y=y;
  }
  public void setPoints()
  {
	points++;  
  }
  public void setLife()
  {
	 life=false; 
  }
  public void setFoodCoords(Coords c)
  {
	  foodCords=c;
  }
  public void setSnakeCoords()
  {  
  }
  public void startValue()
  {	  
	  points=0; 
      size=1;
      life=true;
      if(random.nextInt(2)==1)
    	 direction=1;
    	  else direction=2;      
      if(random.nextInt(2)==1)
    	  directionValue.x=20;
    	  else directionValue.x=-20;
      if(random.nextInt(2)==1)
	    	  directionValue.y=20;
	    	  else directionValue.y=-20;
      snakeCoords.clear();

	  Coords c=new Coords();  
	  c.value(random.nextInt(30)*20,random.nextInt(20)*20);
	  snakeCoords.add(c); 
	  while(true)
	  {
	  foodCords.x=random.nextInt(30)*20;
      foodCords.y=random.nextInt(20)*20;
	  if(foodCords.x!=c.x && foodCords.y!=c.y)
	  break;
	  }
	  
	  
}
  public void animate(Label score)
  {
	   
	  if(snakeCoords.get(0).x==foodCords.x && snakeCoords.get(0).y==foodCords.y)
	     {	 		  
		  
		  Coords c=new Coords();
		  c.value(snakeCoords.get(0).x, snakeCoords.get(0).y);
		  snakeCoords.add(c);
		  size++;
		  points++;
		  score.setText("Score :  "+ points);
          final Random random = new Random();
          while(true)
            {
        	  boolean r=false;
        	  foodCords.x=random.nextInt(30)*20;
              foodCords.y=random.nextInt(20)*20;
              for(int i=0;i<size;i++)
                if(foodCords.x!=snakeCoords.get(i).x && foodCords.y!=snakeCoords.get(i).x)
                 {  
        	       r=true;
                   break;
                 }
              if(r)
        	  break;
            }
	  }
	 
	  for(int i=size-1;i>0;i--)
		  snakeCoords.get(i).value(snakeCoords.get(i-1).x, snakeCoords.get(i-1).y); 
	  if(direction == 1)
		  snakeCoords.get(0).x += directionValue.x;   
	  if(direction == 2)		
		  snakeCoords.get(0).y += directionValue.y; 
	  for(int i=1;i<size;i++)
		 {
		      if(snakeCoords.get(0).x==snakeCoords.get(i).x && snakeCoords.get(0).y==snakeCoords.get(i).y)
		     	  {	          
			           life=false;   			                	
		    	  }
		  }
	  
    if (snakeCoords.get(0).x < 0)
    	snakeCoords.get(0).x = 580; 
    if (snakeCoords.get(0).x > 580)
    	snakeCoords.get(0).x = 0; 
    if (snakeCoords.get(0).y < 0) 
    	snakeCoords.get(0).y = 380;
    if (snakeCoords.get(0).y > 380) 
    	snakeCoords.get(0).y = 0;    
   
  }
  public void keyPressed(int Code)
  {
	  if(Code==16777220 && direction==2)
	    { 
		  directionValue.x=20;
		  direction=1; 
		//"Right"
	    }
	  if(Code==16777219 && direction==2)
	    {
    //"Left"
		  directionValue.x=-20;
		  direction=1; 
		}
	  if(Code==16777218 && direction==1)
	    {
		  //"Down"
		  directionValue.y=20;
		  direction=2;  
  		}
	  if(Code==16777217 && direction==1)
	    {
		  //"Up";
		  directionValue.y=-20;
		  direction=2; 
		 }
  }
  
}