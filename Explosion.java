import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Explosion extends Effect{ // creates an explosion on the field
	private double z = 0;
	public Explosion(double x, double y, double size, double life){

		super( x,y,size,life );
		this.x = x + randint(size*-1,size);
		this.y = y + randint(size*-1,size);
	}
	double scaledX;
	double scaledY;
	double scaledZ;
	double scaledSize;
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
	public void shadow(Graphics g, JFrame c){
		D3.circle3D (scaledX, scaledY, 0, scaledSize, new Color(250,250,250), g, c);
		D3.circle3D( scaledX+randint(-1*scaledSize,scaledSize), scaledY+randint(-1*scaledSize,scaledSize), 0, scaledSize, new Color(250, 250, 250), g, c);
		D3.line3D(
		scaledX,
		scaledY,
		scaledZ,
		randint((int)scaledSize*-1,(int)scaledSize)*5+scaledX,
		randint((int)scaledSize*-1,(int)scaledSize)*5+scaledY,
		0,
		new Color(50, 50, 50),g,c);
	}
	public void draw(Graphics g, JFrame c, int R, int G, int B, double X, double Y, double Z){
		D3.circle3D (X, Y, 0, scaledSize, new Color(R,G,B), g, c);
		D3.drawCircle3D( X+randint(-1*scaledSize,scaledSize), Y+randint(-1*scaledSize,scaledSize), Z, scaledSize, Color.gray, g, c);
		D3.line3D(
		X,
		Y,
		Z,
		randint((int)scaledSize*-1,(int)scaledSize)*5+X,
		randint((int)scaledSize*-1,(int)scaledSize)*5+Y,
		randint((int)scaledSize*-1,(int)scaledSize)*5+Z,
		Color.gray,g,c);
	}
}