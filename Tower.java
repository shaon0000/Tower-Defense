import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Tower extends Unit implements Comparable{

/* This is the super class of all the Towers in the game.
 * In order to extend from this class, a few things should be replaced
 *
 *
 * 1) initBody() - shape of the towers body
 * 2) initBody() - shape of the turrets
 * 3) initStats() - the tower's stats
 * 4) attack( Unit enemy ) - how does it attack an enemy
 * 5) produce( X,Y ) - this is an important method. If its not over-ridden, then
 *                     a standard Tower will be created rather than the subclass
 *                     Tower. The produce method should give a brand a new tower
 *                     of the subclass.
 * 6) A constructor that takes in no parameters and also does nothing but call the super constructor
 */
	public int compareTo( Object other){
		Tower temp = (Tower)(other);
		if(temp.level > this.level){return -1;}
		else if (temp.level < this.level){return 1;}
		else{return 0;}
	}
	public Tower(double X,double Y){
		super(X,Y);
		grow(0.5);
	}
	public Tower(){
		super();
	}
	public void initBody(){
		double[][] Xcords = {{0,25,0,-25},{0,15,0,-15},{3-25,20,48-25},{20,3-25,2-25}};
		double[][] Ycords = {{-25,0,25,0},{-15,0,15,0},{3-25,20,42-25},{3-25,20,42-25}};
		int[][] colours = {{150,150,0}, {150,0,0},{255,0,0},{255,0,0}};
		for(int i = 0; i < Xcords.length; i++){
			addBody(colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);
		}
	}
	
	public void initTurret(){
 		double[][] Xcords = {
 			{-7.0, 1.0, 8.0, 0.0 },{-15.0, 0.0, 15.0, -1.0},{-19.0, -1.0, 20.0, -2.0},{-21.0, -26.0, -15.0},{-11.0, -17.0, -8.0},{-4.0, -6.0, -1.0, 0.0 },{7.0, 6.0, 13.0 },{15.0, 15.0, 22.0 }
 		};
		double[][] Ycords = {{-26.0, -13.0, -26.0, -23.0},{-28.0, -3.0, -28.0, 3.0 },{-27.0, 12.0, -27.0, 20.0 },{-9.0, 6.0, 90.0},{6.0, 15.0, 88.0 },{24.0, 29.0, 88.0, 26.0 },{9.0, 87.0, 17.0},{-9.0, 86.0, 6.0}
		};
		int[][] colours = { {50,50,50},{50,50,50},{150,150,150},{20,0,0},{225,5,5},{20,0,0},{225,5,5},{50,50,50},{50,50,50} };
		for(int i = 0; i < Xcords.length; i++){
			addTurret( colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);
		}
		rotateTurret(-Math.PI/2.0);
	}
	
	public void initStats(){
		 hp=1; // hit points
		 ap=50; // attack power
		 range=200; // range of the unit's attack
		 vx=0; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=0; // velocity
		 heat = 3;
		 bulletV = 24;
		 bulletRad = 3;
		 rand = 25;
	}
	
	public void scan(){
		/* if the unit has no target to attack,
		 * it scans through the list of units to attack
		 */
		Unit closest = null;
		double dist=Integer.MAX_VALUE;
		for ( Unit i : units ){
			double tempDis = distance(i);
			if( tempDis <= range){
				allTargets.push(i);
				if(tempDis < dist){
					closest = i;
					dist = tempDis;   
				}
			}
		}
		if(closest != null){
			target = closest;
			pointAtUnit(target);
		}
	}
	
	public void pointAtUnit(Unit enemy){ // rotate turrett to point at the enemy
		double enemyAng = Math.atan2( enemy.getY()-pos[1],enemy.getX()-pos[0] ); // atan2 gets the angle between 0 and 2*PI
		rotateTurret(enemyAng-this.ang); // rotate the turret by this much
		this.ang = enemyAng;
		attack = true; // after pointing at the enemy, attack it
	}
	
	protected double fireX;
	protected double fireY;
	public void attack( Unit enemy ){
		if(heatCounter >= heat){
			fireX = enemy.getX();
			fireY = enemy.getY();
			for(int i = 0; i < level; i++){
				weapons.add( warhead(pos[0],pos[1],enemy) );
			}
		}
	}
	public Bullet warhead(double x,double y,Unit enemy){
		return new Bullet(effects,grid,x+randint(rand*-1,rand),y+randint(rand*-1,rand),enemy.getX()+randint(rand*-1,rand),enemy.getY()+randint(rand*-1,rand),ang,bulletV,bulletRad,ap);
	}
	public void hunt(){
		if (target != null){
			if(checkUnit(target) && target.getAlive() == true){
				pointAtUnit(target);
				attack(target);
			}
			else{
				target = null;
				allTargets.clear();
			}
		}
	}
	
	public boolean checkUnit( Unit enemy ){ // check if the enemy unit is in range
		if ( Math.hypot(enemy.getX()-pos[0],enemy.getY()-pos[1]) <= range ){
			return true;
		}
		return false;
	}
	public double distance( Unit enemy ){
		return Math.hypot(enemy.getX()-pos[0],enemy.getY()-pos[1]);
	}
	
	public void produce( double x, double y){
		Tower tmp = new Tower(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tower.add( tmp );
	}	
	public void heatBar( Graphics g, JFrame c){
		g.setColor(Color.red);
		g.drawRect((int)scaledX,(int)scaledY+(int)(5*scaleVal/Global.MAX_SIZE),(int)(50*scaleVal/Global.MAX_SIZE),(int)(5*scaleVal/Global.MAX_SIZE));
		g.setColor(Color.yellow);
		g.fillRect((int)scaledX,(int)scaledY+(int)(5*scaleVal/Global.MAX_SIZE),(int)(50*heatCounter*scaleVal/Global.MAX_SIZE/heat),(int)(5*scaleVal/Global.MAX_SIZE));	
		
	}
	public void work(){
		heatCounter++;
		if(target == null){ scan(); }
		else{ hunt(); }
		if (heatCounter > heat-1){
			if(target == null){
				heatCounter = heat;
			}
			else{
				heatCounter = 0;
			}
		}
	}		
}
