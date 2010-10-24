import java.awt.*;
import javax.swing.*;
import java.util.*;
class Menu{
	int[] pos;
	int c; // the number of columns in this menu
	int w; // width of each cell in menu
	int h; // height of each cell in menu
	int r; // number of rows in this menu
	Image pressImg; // the image that goes on top of buttons if they are pressed
	int n; // number of buttons currently in the menu;
	int nextX; // the next X-coordinate where the button will go
	int nextY; // the next Y-coordinate where the button will go
	int currentCellX; // the X-index of the current button in the grid
	int currentCellY; // the Y-index of the current button in the grid
	myButton currentButton;  // the current button selected by the user
	HashMap<String,myButton> buttonSet;
	myButton [][] grid; // grid of strings of button names
	
	boolean visible = true;
	public boolean getVisible(){
		return visible;
	}
	public void setVisible(boolean vis){
		visible = vis;
	}
	
	public Menu(int[] pos, int columns,int rows,int width, int height, String pressed){
		buttonSet = new HashMap<String,myButton>();
		grid = new myButton[columns][rows];
		pos = pos;
		nextX = pos[0];
		nextY = pos[1];
		this.c = columns;
		this.w = width;
		this.h = height;
		this.pressImg = Toolkit.getDefaultToolkit ().createImage (pressed);
	}
	public void add(String name, String fileName, String statement, boolean onImage){
		if ( n == c){ // if there are four button on this row
			n = 0; // reset to the first position
			nextY += h; // move to the next row
			currentCellY++; // move to the next row in the grid
			nextX = pos[0]; 
			currentCellX = 0; 
		}
		else{
			nextX += w; // move to the next column
			currentCellX++; 
			n++;
		}
		int [] newPos = {nextX, nextY};
		if (onImage == true){
			grid[currentCellX][currentCellY] = new myButton(fileName,newPos,pressImg,statement,name,w,h);
		}
		else{
			grid[currentCellX][currentCellY] = new myButton(fileName,newPos,pressImg,statement,"",w,h);
		}
		
		buttonSet.put(name,grid[currentCellX][currentCellY]);
	}
	public void check(int MX, int MY, int MB){
		MX = ((MX-pos[0])/w);
		MY = ((MY-pos[0])/h);		

		if (MX < c && MX >= 0 && MY < r && MB == 1){
			currentButton.unpress();
			currentButton = grid[MX][MY];
			currentButton.press();
		}
	}
	public void draw(Graphics g, JFrame canvas){
		for( int i = 0; i < grid.length; i++ ){
			for (int j = 0; j < grid[i].length; j++){
				grid[i][j].draw(g,canvas);
			}
		}
	}
}