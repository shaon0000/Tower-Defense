import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Wall extends Tower{
	public Wall(double X,double Y){
		super(X,Y);
		//System.out.println("the wall is being made");
	}
	public Wall(){
		super();
		//System.out.println("the wall is being made");
	}

	public void initBody(){
		double[][] Xcords = {{-25,25,25,-25}};
		double[][] Ycords = {{25,25,-25,-25}};
		int[][] colours = {{155,155,155}};
		for(int i = 0; i < Xcords.length; i++){
			addBody(colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);

		}
	}

	public void initTurret(){
		double[][] Xcords = {{-25,25,25,-25}};
		double[][] Ycords = {{25,25,-25,-25}};
		int[][] colours = {{200,200,200}};
		for(int i = 0; i < Xcords.length; i++){
			addTurret( 0,150,0, Xcords[i],Ycords[i]);

		}
		rotateTurret(-Math.PI/4.0);

	}

	public void initStats(){
		 hp=1; // hit points
		 ap=0; // attack power
		 range=200; // range of the unit's attack
		 vx=0; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=0; // velocity
		 heat = 5;
		 bulletV = 0;
		 bulletRad = 0;
		 rand = 0;
	}

	public void produce(double x,double y){
		Wall tmp = new Wall(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tower.add( tmp );
	}
	public void attack( Unit enemy ){

		if(heatCounter >= heat){

			fireX = enemy.getX();
			fireY = enemy.getY();
			enemy.freeze(level*2.0);
			effects.add(new CircleTowerEffect(fireX ,fireY,Global.MAX_SIZE,100));
		}


	}
}