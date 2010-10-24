import java.util.*;
import javax.swing.*;
import java.awt.*;
public class Laser extends Weapon{

	Unit enemy;

	public Laser( LinkedList<Effect> effects, Unit enemy, double startX, double startY, double endX, double endY, double size, double ap ){
		super(effects,startX,startY,endX,endY,size,ap);
		this.enemy = enemy;

	}

	double scaledStartX;
	double scaledStartY;
	double scaledEndX;
	double scaledEndY;
	double scaledSize;
	int G = 0;

	public void scale( double size, double normal, double [] ref){
		scaledStartX = startX*size/normal + ref[0] ;
		scaledStartY = startY*size/normal + ref[1] ;
		scaledEndX = enemy.getX()*size/normal + ref[0];
		scaledEndY = enemy.getY()*size/normal + ref[1];
		scaledSize = this.size*size/normal;
	}

	public boolean getAlive(){ return alive; }

	public void damage(){
		enemy.damage(ap);
		effects.add( new Explosion(enemy.getX(),enemy.getY(),15,10) );

	}
	public void work(){
		damage();
	}

	public void draw(Graphics g, JFrame c){


		for(int i=0; i < size/2; i++){
			int increaseX = Global.randint(scaledSize*-1,scaledSize);
			int increaseY = Global.randint(scaledSize*-1,scaledSize);
			//g.drawLine();
			int newG = (int)(Math.random()*256) + G;
			if (newG < 255 && newG >= 0 ){
				try{

				D3.line3D(scaledStartX+increaseX,scaledStartY+increaseY,0.0,scaledEndX+increaseX,scaledEndY+increaseY,0.0,new Color(newG,0,0),g,c);
				}
				catch(Exception e){
					System.out.println("the failed red colour is "+newG );
				}
			}
			if (G < -255){
				alive = false;
			}
			G -= 10;
		}

	}
}