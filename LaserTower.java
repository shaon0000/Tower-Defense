import java.util.*;
import javax.swing.*;
import java.awt.*;

public class LaserTower extends Tower{
	public LaserTower(double X,double Y){
		super(X,Y);
	}
	public LaserTower(){
		super();
	}
	public void initBody(){
		double[][] Xcords = {
		{-26.0, -12.0, -1.0, 9.0, 25.0, 10.0, 24.0, 5.0, -1.0, -11.0, -25.0, -15.0},
		{35.0, 24.0, 33.0, 41.0},
		{23.0, 20.0, 31.0, 35.0},
		{-8.0, -2.0, 5.0, -3.0},
		{-36.0, -25.0, -25.0, -39.0},
		{-26.0, -40.0, -48.0, -38.0},
		{-24.0, -37.0, -35.0, -23.0},
		{-5.0, -13.0, -6.0, 0.0},
		{18.0, 12.0, 25.0, 29.0 }
		};
		double[][] Ycords = {
		{-12.0, -26.0, -16.0, -28.0, -12.0, -4.0, 10.0, 23.0, 8.0, 23.0, 9.0, -2.0},
		{-10.0, -1.0, 7.0, -3.0},
		{-38.0, -24.0, -26.0, -39.0},
		{-37.0, -29.0, -38.0, -50.0},
		{-26.0, -24.0, -38.0, -39.0},
		{-3.0, -11.0, 0.0, 5.0},
		{19.0, 19.0, 29.0, 28.0},
		{22.0, 33.0, 36.0, 28.0 },
		{20.0, 30.0, 32.0, 20.0}
		};
		int[][] colours = {
			{255,0,0}, 
			{255,255,255},
			{150,150,150},
			{255,255,255},
			{150,150,150},
			{255,255,255},
			{150,150,150},
			{255,255,255},
			{150,150,150}
		};
		for(int i = 0; i < Xcords.length; i++){
			addBody(colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);

		}
		grow(0.5);
	}
	
	public void initTurret(){
 		double[][] Xcords = {
 		{-1.0, -8.0, -1.0, 5.0 },
 		{-8.0, -25.0, -9.0, -16.0},
 		{4.0, 10.0, 6.0, 24.0},
 		{-22.0, -3.0, 16.0, -1.0}
 		};
		double[][] Ycords = {
		{-20.0, -14.0, 174.0, -13.0},
		{157.0, -15.0, -27.0, -13.0 },
		{-29.0, -16.0, 156.0, -20.0},
		{-29.0, -33.0, -32.0, -45.0}
		};
		int[][] colours = {{0,0,0},{155,155,155},{155,155,155},{255,0,0}};
		for(int i = 0; i < Xcords.length; i++){
			addTurret( colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);

		}
		rotateTurret(-Math.PI/2.0);
		for(Poly i: turret){
			i.grow(0.5);
		}
		  		
	}
	
	public void initStats(){
		 hp=500; // hit points
		 ap=10; // attack power
		 range=300; // range of the unit's attack
		 vx=0; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=0; // velocity
		 heat = 20;
		 bulletRad = 10;
		 rand = 25;
	}
	public void produce(double x,double y){
		LaserTower tmp = new LaserTower(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tower.add( tmp );
	}
	public void attack(Unit enemy){
		
		if(heatCounter >= heat){
			
			fireX = enemy.getX () ;
			fireY = enemy.getY () ;
			int i = 1 ;
			Iterator<Unit> m = allTargets.iterator() ;
			weapons.add ( new Laser(effects,enemy,pos[0]+Global.randint(box[0]*-1,box[0]),pos[1]+Global.randint(box[0]*-1,box[0]),enemy.getX(),enemy.getY(),bulletRad,ap));
			double nextX = enemy.getX() ;
			double nextY = enemy.getY() ;
			while( m.hasNext() && i < level ){
				Unit n = m.next() ;
				if(n != target ){
					weapons.add( new Laser(effects,n,nextX+Global.randint(box[0]*-1,box[0]),nextY+Global.randint(box[0]*-1,box[0]),n.getX(),n.getY(),bulletRad,ap));
				} 
				i++ ;
			}	
		}
		else{

		}
		
	}
	
	
	
}