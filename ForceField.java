import java.util.*;
import javax.swing.*;
import java.awt.*;

public class ForceField extends Lightning{
	LinkedList<Weapon> weapons;
	boolean original = true;
	boolean once = false;
	public ForceField(LinkedList<Effect> effects, Cell[][] grid, double startX, double startY, double theta, double size,double range, double ap){
		super(effects,grid,startX,startY,theta,size,range,ap);
		this.weapons = new LinkedList<Weapon>();
	}
	public void work(){
		double newAng = theta;
		for(int i = 1; i < X.length; i++){
			newAng += Global.randint(-45,45)*Math.PI /180;
			X[i] = X[i-1] + size*Math.cos(newAng);
			Y[i] = Y[i-1] + size*Math.sin(newAng);
			if( Math.random() < 0.05 && original == true){
				ForceField temp = new ForceField(effects,grid,X[i],Y[i],newAng,size,(X.length-i)*size,ap);
				temp.G = G;
				temp.original = false;
				weapons.add(temp);
			}
		}
		//effects.add( new Explosion(X[X.length-1],Y[Y.length-1],15,10) );
		//weapons.add( new Bullet(effects,grid,this.X[0],this.Y[0],X[X.length-1],Y[Y.length-1],this.theta,1,3,50) );
		damage();
		Iterator<Weapon> i = weapons.iterator();
		while(i.hasNext()){
			Weapon m = i.next();
			if( m.getAlive() == false ){i.remove();}
			m.work();
		}
	}
	public void kill(){alive = false;}
	public void damage(){
		for(int i = 0; i < X.length; i++){
			int cellX = (int)(X[i]/Global.MAX_SIZE);
			int cellY = (int)(Y[i]/Global.MAX_SIZE);
			if(cellX >= 0 && cellX < Global.TILE_AMOUNT && cellY >= 0 && cellY < Global.TILE_AMOUNT)
				grid[cellX][cellY].damageBySize(size,X[i],Y[i],this.ap);
		}
	}
	private double scaledRange ;
	public void scale( double current, double normal, double []ref ){
		for(int i = 0; i < scaled_X.length; i++){
			scaled_X[i] = X[i]*current/normal + ref[0];
			scaled_Y[i] = Y[i]*current/normal + ref[1];
		}
		scaledRange = range*current/normal;
		for(Weapon i: weapons){
			i.scale(current, normal, ref);
		}
	}
	public void draw( Graphics g, JFrame c ){
		if(G < 255 && G >= 0){
			if(Math.random() < 0.5)
				g.setColor(new Color( G, G, G));
			else{
				g.setColor( new Color(0,0,G));
			}
			for(int i = 1; i < scaled_X.length; i++){
				g.drawLine((int)scaled_X[i],(int)scaled_Y[i],(int)scaled_X[i-1],(int)scaled_Y[i-1]);
			}
			//g.drawOval((int)(scaled_X[0]-scaledRange),(int)(scaled_Y[0]-scaledRange),(int)(2*scaledRange),(int)(2*scaledRange));
		}
		G -= 20;
		if ( G < 0 || original == false && weapons.size() != 0){
			kill();
		}
		for(Weapon i: weapons){
			i.draw(g,c);
		}
	}
}
