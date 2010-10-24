/* this is one of the basic weapons */

import java.util.*;
import java.awt.*;
import javax.swing.*;


public class Bullet extends Weapon{

	protected double theta; // bullet angle
	protected double v; // magnitude of the velocity
	protected double vx; // velocity x-component
	protected double vy; // velocity y-component
	protected double maxT; // time of flight
	protected int cellX; // the landing cell
	protected int cellY;
	protected double t; // current time;
	protected double a; // the "a" constant for the parabolic path the bullet follows


	Cell [][] grid;

	public Bullet( LinkedList<Effect> effects, Cell[][] grid, double startX, double startY, double endX, double endY, double theta, double v, double size, double ap ){
		super(effects,startX,startY,endX,endY,size,ap);
		this.grid = grid ;
		this.cellX = (int)(endX/Global.MAX_SIZE) ; // once the bullet lands, it'll check in this cell for units
		this.cellY = (int)(endY/Global.MAX_SIZE) ;
		if(cellX >= Global.TILE_AMOUNT || cellY >= Global.TILE_AMOUNT){
			alive = false;
		}
		// the travel path is pre-calculated for a couple reasons
		// a) it makes it easier to program the tower to aim in the distance
		// b) we can control the exact flight animation of a bullet to differentiate between small projectiles and large ones.

		this.theta = theta ;
		this.v = v ;
		this.vx = v*Math.cos(theta) ;
		this.vy = v*Math.sin(theta) ;
		this.maxT = Math.hypot(endX-startX,endY-startY)/this.v ; // travel time = distance/velocity

		this.a = this.maxT/( (maxT/2.0)* (maxT/2.0 - maxT) ) ;
	}

	public double height(double time){
		return this.a*(time)*(time-maxT);
	}

	public void work(){

		X += vx;
		Y += vy;
		t++;
		Z = height(t);


		damage();
	}
	protected double scaledX;
	protected double scaledY;
	protected double scaledZ;
	protected double scaledSize;
	public void scale( double size, double normal, double [] ref){
		scaledX = X*size/normal + ref[0];
		scaledY =Y*size/normal + ref[1];
		scaledZ = Z*size/normal;
		scaledSize = this.size*size/normal;
	}

	public void damage(){
		if( Z < 0){ // damage the units in the cell after the bullet has landed

			grid[cellX][cellY].damageAll(ap);
			kill(); // kill self

			effects.add( new Explosion(endX,endY,15,10) );
		}
	}
	public void kill(){
		alive = false;
	}
	public boolean getAlive(){
		return alive;
	}

	public void draw(Graphics g, JFrame c){


		D3.circle3D( scaledX,scaledY,scaledZ,scaledSize,new Color( 255,(int)(Math.random()*256),0),g,c );

	}



}