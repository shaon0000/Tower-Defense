import java.util.*;
import java.awt.*;
import javax.swing.*;

/* This is a class with several static methods which can used to draw
 * simples shapes in 3D */

public class D3{
	
	private static double fl = 250.0;
	public static int centerX = Global.WIDTH/2;
	public static int centerY = Global.HEIGHT/2;
	public static double depth(double n){ // 3 dimensional coordinate visual point multiplier
	 // gives a back a certain value that scales size and position of an object to give a 3D illusion
	    return (fl+n)/fl;
	}
	
	public static double zX (double x,double z){ // returns a 3d point in 2d form
	    double scale_factor = depth(z);
	    x = (x-centerX)*(scale_factor)+centerX;
	    return x;
	}
	
	public static double zY ( double y, double z){ // returns a 3d point in 2d form
	    double scale_factor = depth(z);
	    y = (y-centerY)*(scale_factor)+centerY;
	    return y;
	}
	
	public static void circle3D (double X, double Y,double Z, double rad,Color colour, Graphics g,JFrame canvas){
        int rad3D = (int)(depth(Z)*rad);
        g.setColor(colour);
        g.fillOval((int)(zX(X,Z)-rad3D),(int)(zY(Y,Z)-rad3D),2*rad3D,2*rad3D);
    }
    
	public static void drawCircle3D (double X, double Y,double Z, double rad,Color colour, Graphics g,JFrame canvas){
        int rad3D = (int)(depth(Z)*rad);
        g.setColor(colour);
        g.drawOval((int)(zX(X,Z)-rad3D),(int)(zY(Y,Z)-rad3D),2*rad3D,2*rad3D);
    }
	public static void line3D(double X1, double Y1, double Z1, double X2, double Y2, double Z2, Color colour,Graphics g,JFrame canvas){
		
		g.setColor(colour);
		g.drawLine((int)zX(X1,Z1),(int)zY(Y1,Z1),(int)zX(X2,Z2),(int)zY(Y2,Z2));
		 
	}
	
}