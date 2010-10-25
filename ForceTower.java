import java.util.*;
import javax.swing.*;
import java.awt.*;

public class ForceTower extends LightningTower{
	public ForceTower(double X,double Y){
		super(X,Y);
	}
	public ForceTower(){
		super();
	}
	public void initBody(){
		double[][] Xcords = {
		{-11.0, -26.0, -26.0, -18.0, -8.0, -25.0, -25.0, -14.0, -3.0, 5.0, 23.0, 25.0, 6.0, 24.0, 24.0, 11.0, 1.0 },
		{-16.0, -45.0, -46.0, -35.0, -18.0, -46.0, -42.0, -18.0, -16.0, -4.0, -6.0, 5.0, 18.0, 9.0, 40.0, 36.0, 4.0, 34.0, 34.0, 8.0 },
		{-4.0, -36.0, -3.0, 30.0 }
		};
		double[][] Ycords = {{-26.0, -26.0, -13.0, -13.0, -3.0, 10.0, 24.0, 24.0, 4.0, 24.0, 24.0, 6.0, 3.0, -8.0, -24.0, -25.0, -9.0} ,
		{-7.0, -21.0, -7.0, -6.0, 0.0, 16.0, 27.0, 13.0, 39.0, 38.0, 15.0, 37.0, 38.0, 15.0, 17.0, 5.0, 3.0, -9.0, -19.0, -10.0 },
		{-24.0, -42.0, -9.0, -38.0 }
		};
		int[][] colours = {{2,12,122}, {0,150,150},{0,0,0},{0,0,0}};
		for(int i = 0; i < Xcords.length; i++){
			addBody(colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);
		}
	}
	
	public void initTurret(){
 		double[][] Xcords = {
 			{-59.0, -46.0, -58.0, -37.0},
 			{-23.0, -2.0, 21.0, -2.0},
 			{50.0, 28.0, 49.0, 39.0},
 			{-23.0, -2.0, 22.0, -1.0},
 			{-46.0, -67.0, -69.0, -58.0},
 			{33.0, 53.0, 64.0, 61.0},
 			{-47.0, -63.0, -59.0, -51.0},
 			{30.0, 56.0, 68.0, 65.0}
 		};
		double[][] Ycords = {
			{-23.0, -2.0, 21.0, -3.0},
			{50.0, 28.0, 49.0, 38.0},
			{-24.0, -2.0, 20.0, -2.0},
			{-51.0, -29.0, -53.0, -43.0},
			{-39.0, -61.0, -69.0, -66.0},
			{35.0, 66.0, 61.0, 48.0},
			{38.0, 59.0, 63.0, 59.0},
			{-34.0, -67.0, -70.0, -62.0}
		};
		Color[] colours = {new Color(255,255,255), new Color(150,150,150),new Color(255,0,0),new Color(255,0,0)};
		for(int i = 0; i < Xcords.length; i++){
			addTurret( 255,255,255, Xcords[i],Ycords[i]);
		}
		rotateTurret(-Math.PI/2.0);
	}
	
	public void initStats(){
		 hp=1; // hit points
		 ap=1; // attack power
		 range=400; // range of the unit's attack
		 vx=0; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=0; // velocity
		 heat = 30;
		 bulletRad = 10;
		 rand = 25;
	}
	public void produce(double x,double y ){
		ForceTower tmp = new ForceTower(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tower.add( tmp );
	}
	public void attack(Unit enemy){
		if(heatCounter >= heat){
			weapons.add( new ForceField(effects,grid,pos[0],pos[1],ang,bulletRad,range,ap) );
			fireX = enemy.getX();
			fireY = enemy.getY();
			int i = 1;
			Iterator<Unit> m = allTargets.iterator();
			while( m.hasNext() && i < level ){
				Unit n = m.next();
				weapons.add( new ForceField(effects,grid,n.getX(),n.getY(),ang,bulletRad,range,ap) );
				} 
				i+=5;
		}
	}	
}
