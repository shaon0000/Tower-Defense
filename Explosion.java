import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Explosion extends Effect{ // creates an explosion on the field
	double scaledX, scaledY, scaledZ, scaledSize, z;
	public Explosion(double x, double y, double size, double life){
		super( x,y,size,life );
		this.x = x + randint(size*-1,size);
		this.y = y + randint(size*-1,size);
	}
	
	public void scale( double current, double normal, double[] ref){
		x += randint(-1*size,size);
		y += randint(-1*size,size);
		scaledX = x*current/normal + ref[0];
		scaledY = y*current/normal + ref[1];
		scaledZ = z*current/normal;
		scaledSize = size*current/normal;
	}

	public void work(){

		this.size -= rate;
		if(this.size < 0){
			alive = false;
		}
		z+= 25;
	}
	public void draw(Graphics g, JFrame c){
		int R = (int)(255*size/maxSize);
		if(R > 255){
			R = 255;
		}
		int G = (int)(Math.random()*256*size/maxSize);
		if (G > 255){
			G = 255;
		}
		draw( g, c, R, G, 0, scaledX, scaledY, scaledZ);
	}
	public void draw(Graphics g, JFrame c, int R, int G, int B, double X, double Y, double Z){
		D3.circle3D (X, Y, 0, scaledSize, new Color(R,G,B), g, c);
		D3.drawCircle3D( X+randint(-1*scaledSize,scaledSize), Y+randint(-1*scaledSize,scaledSize), Z, scaledSize, Color.gray, g, c);
		D3.line3D(X,Y,Z,randint((int)scaledSize*-1,(int)scaledSize)*5+X,randint((int)scaledSize*-1,(int)scaledSize)*5+Y,randint((int)scaledSize*-1,(int)scaledSize)*5+Z,Color.gray,g,c);
	}
}
