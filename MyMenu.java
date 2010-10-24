import java.awt.*;
import javax.swing.*;
import java.util.*;
class MyMenu{
	private int[] pos ;
	private int c ; // the number of columns in this menu
	private int w ; // width of each cell in menu
	private int h ; // height of each cell in menu
	private int r ; // number of rows in this menu
	private Image pressImg ; // the image that goes on top of buttons if they are pressed
	private int n ; // number of buttons currently in the menu;
	private int nextX=0 ; // the next X-coordinate where the button will go
	private int nextY=0 ; // the next Y-coordinate where the button will go
	private int currentCellX=0 ; // the X-index of the current button in the grid
	private int currentCellY=0 ; // the Y-index of the current button in the grid
	private int TB = 0 ;
	private myButton currentButton ;  // the current button selected by the user
	private HashMap<String,myButton> buttonSet ;
	public String name ;
	private myButton [][] grid ; // grid of strings of button names
	boolean visible = true ;
	
	public boolean getVisible(){
		return visible;
	}
	public void setVisible( boolean n ){
		visible=n;
	}
	
	public MyMenu(String name, int[] pos, int columns,int rows,int width, int height, String pressed){
		buttonSet = new HashMap< String,myButton >( );
		this.grid = new myButton[ columns ][ rows+1 ];
		this.pos = pos ;
		nextX = pos[0] ;
		nextY = pos[1] ;
		this.c = columns ;
		this.w = width ;
		this.h = height ;
		this.r = rows ;
		this.pressImg = Toolkit.getDefaultToolkit ().createImage (pressed) ;
		this.name = name ;
	}
	
	public void add(String name, String fileName, String statement, boolean onImage){
	
		int [] newPos = {nextX, nextY} ;

		grid[currentCellX][currentCellY] = new myButton(fileName,newPos,pressImg,statement,name,w,h,onImage) ;
		buttonSet.put(name,grid[currentCellX][currentCellY]) ;
		n++ ;
		if (n == c) {
			
			n = 0; // reset to the first position
			nextY += h; // move to the next row
			currentCellY++; // move to the next row in the grid	
			nextX = pos[0];
			currentCellX = 0; 
		
		}
		else{	
			nextX += w ; // move to the next column
			currentCellX++ ; 
		}
	}
	
	public void autoDeclick(String name,boolean auto){

		buttonSet.get(name).auto = auto;
	}
	
	public void check(int MX, int MY, int MB){
		if (currentButton != null && currentButton.auto == true){
			currentButton.unpress();
			if(MB==0){
				currentButton.deselect();
				currentButton = null;
			}
		}


			MX = ((MX-pos[0]));
			MY = ((MY-pos[1]));
			if ( MX < 0 || MY < 0 ){
				return;
			}
			MX /= w;
			MY /= h;
				
			if (MX < c && MX >= 0 && MY < r && MY >= 0 && MB == 1){
				
				if(TB == 0){
					if(currentButton != null)
						currentButton.deselect();
						currentButton = null;
					if(grid[MX][MY] != null){
						currentButton = grid[ MX ][ MY ];
						currentButton.select();
						
					}
				}
			}
	}

	public myButton getActiveButton() {
		return currentButton;
	}
	
	public void depressActiveButton() {
		if(currentButton != null)
			currentButton.deselect();
		currentButton = null;
	}
	
	public void draw(Graphics g, JFrame canvas) {
		for( int i = 0; i < grid.length; i++ ) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] != null)
					grid[i][j].draw(g,canvas);
			}
		}
	}
}