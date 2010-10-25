import java.util.*;
import javax.swing.*;
import java.awt.*;

/* makes a huge tower of 3D circles which shrinks and grows parallel to the surface over time */

public class CircleTowerEffect extends Effect{
	protected int sizeV = 3;
	protected double vz = 20;
	double z,scaledX, scaledY, scaledZ, scaledSize, lifeCounter;
	public CircleTowerEffect( double x, double y, double size, double life ){
		super(x,y,size,life);
	}
	public void scale( double current, double normal, double[] ref){
		scaledX = x*current/normal + ref[0];
		scaledY = y*current/normal + ref[1];
		scaledZ = z*current/normal;
		scaledSize = this.size*current/normal;
	}
	public void work(){

		size += sizeV;
		if(size < 0 || size >= maxSize){
			sizeV *= -1 ;
			size += sizeV ;
		}
		lifeCounter++;
		z += vz;
		if (lifeCounter > life){kill();}
	}
	public void draw(Graphics g, JFrame c){
		D3.drawCircle3D(scaledX,scaledY,scaledZ,scaledSize,new Color(0,0,255),g,c);
	}

}
