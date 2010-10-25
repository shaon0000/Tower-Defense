import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Lightning extends Weapon{
	protected double X[];
	protected double Y[];
	protected double scaled_X[];
	protected double scaled_Y[];
	protected double theta, range;
	Cell[][] grid;
	int G = 255;
	public Lightning(LinkedList<Effect> effects, Cell[][] grid, double startX, double startY, double theta, double size,double range, double ap){
		super(effects,startX,startY,0,0,size, ap);
		this.range = range;
		this.theta = theta;
		this.grid = grid;
		X = new double[(int)(range/size)];
		X[0] = startX;
		Y = new double[(int)(range/size)];
		Y[0] = startY;
		scaled_X = new double[(int)(range/size)];
		scaled_Y = new double[(int)(range/size)];
	}
	public void work(){
		for(int i = 1; i < X.length; i++){
			double newAng = theta + Global.randint(-45,45)*Math.PI /180;
			X[i] = X[i-1] + size*Math.cos(newAng);
			Y[i] = Y[i-1] + size*Math.sin(newAng);
		}
		for(int i=0; i < 2; i++){
			effects.add( new Explosion(X[X.length-1],Y[X.length-1],10,75) );
		}
		damage();
	}
	public void damage(){
		for(int i = 0; i < X.length; i++){
			int cellX = (int)(X[i]/Global.MAX_SIZE);
			int cellY = (int)(Y[i]/Global.MAX_SIZE);
			if(cellX >= 0 && cellX < Global.TILE_AMOUNT && cellY >= 0 && cellY < Global.TILE_AMOUNT)
				grid[cellX][cellY].damageBySize(size,X[i],Y[i],this.ap);

		}
	}
	public void scale( double current, double normal, double []ref ){
		for(int i = 0; i < scaled_X.length; i++){
			scaled_X[i] = X[i]*current/normal + ref[0];
			scaled_Y[i] = Y[i]*current/normal + ref[1];
		}
	}
	public void draw( Graphics g, JFrame c ){
		if(G < 255 && G > 0){
			if(Math.random() > .5)
				g.setColor(new Color( G, G, 0));
			else{
				g.setColor(new Color(G,G,G));
			}
			for(int i = 1; i < scaled_X.length; i++){
				g.drawLine((int)scaled_X[i],(int)scaled_Y[i],(int)scaled_X[i-1],(int)scaled_Y[i-1]);
			}
		}
		G -= 20;
		if (G <= 0){
			kill();
		}


	}
	public void kill(){ alive = false;}
}
