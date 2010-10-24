import java.util.*;
import javax.swing.*;
import java.awt.*;

public class LightningTower extends Tower{
	public LightningTower(double X,double Y){
		super(X,Y);
	}

	public LightningTower(){
		super();
	}
	public void grow( double factor){
		for( Poly i: body ){
			i.grow( factor );
		}
		for (Poly i : turret){
			i.grow( factor );
		}
		box[0] *= factor;
		box[1] *= factor;
		rand *= factor;
	}
	public void initBody(){
		double[][] Xcords = {{-12,12,25,25,12,-12,-25,-25} };
		double[][] Ycords = {{-25,-25,-12,12,25,25,12,-12} };
		int[][] colours = {{122,122,122}};
		for(int i = 0; i < Xcords.length; i++){
			addBody(colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);

		}
	}
	
	public void initTurret(){
 		double[][] Xcords = {
 		{0.0, -7.0, -19.0, -17.0, -14.0, -4.0, -1.0, 5.0, 15.0, 18.0, 23.0, 8.0 },
 		{0.0, -6.0, -1.0, 6.0},

 		};
		double[][] Ycords = {
 		{-25.0, -6.0, -15.0, 44.0, -1.0, 12.0, -1.0, 10.0, -1.0, 47.0, -17.0, -6.0 },
 		{19.0, 23.0, 73.0, 23.0 },

	};
		int[][] colours = {{255,255,0}, {0,0,0},{50,50,50},{0,0,0}};
		for(int i = 0; i < Xcords.length; i++){
			addTurret( colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);

		}
		rotateTurret(-Math.PI/2.0);
		  		
	}
	
	public void initStats(){
		 hp=1; // hit points
		 ap=1; // attack power
		 range=200; // range of the unit's attack
		 vx=0; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=0; // velocity
		 heat = 15;
		 bulletRad = 10;
		 rand = 25;
	}
	public void produce(double x,double y ){
		LightningTower tmp = new LightningTower(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tower.add( tmp );
		
	}
	public void attack(Unit enemy){
		
		if(heatCounter >= heat){
			for(int i = 0; i < this.level; i++){
				weapons.add( new Lightning(effects,grid,pos[0],pos[1],ang,bulletRad,range,ap) );
			}
		}
		else{

		}
	}
	
}