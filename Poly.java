import java.awt.*;
import javax.swing.*;

class Poly{
	
	private double[] x; // original X-cordinates
	private double[] y; // original Y-cordinates
	private double [] finalX; // after scaling
	private double [] finalY; // after scaling
	private int [] convertX; // integer versions of finalX
	private int [] convertY; // integer versions of finalY
	private Color colour;
	private boolean select = false;
	
	public void setSelect(boolean select){
		this.select = select;
	}
	
	public Poly(double[] x, double [] y,Color colour){
		this.x = new double[x.length];
		this.y = new double[y.length];
		
		for(int i = 0; i < x.length; i++){
			this.x[i] = x[i];
			this.y[i] = y[i];
		}
		
		this.colour = colour;
		finalX = new double[x.length];
		finalY = new double[y.length];
		convertX = new int[x.length];
		convertY = new int[y.length];
	}

	public Poly(double[] x, double [] y){
		this.x = x;
		this.y = y;
		this.colour = Color.red;
		finalX = new double[x.length];
		finalY = new double[y.length];
		convertX = new int[x.length];
		convertY = new int[y.length];
	}
	
	public void scale( double size, double normal, double posX, double posY, double[] ref){

		for(int i =0; i < x.length; i++){
			finalX[i] = (x[i]+posX)*size/normal + ref[0];
			finalY[i] = (y[i]+posY)*size/normal + ref[1];
		}
	}
	
	public void rotate( double theta ){
		// rotate the polyogn according to this formula:
		double newX;
		double newY;
		for(int i = 0; i < x.length; i++){
			newX = Math.cos ( theta ) * x[i] - Math.sin ( theta ) * y[i];
			newY = Math.sin ( theta ) * x[i] + Math.cos ( theta ) * y[i];
			x[i] = newX;
			y[i] = newY;
		}
	}
	
	public void grow(double factor ){
		for(int i = 0; i < x.length; i++){
			x[i] *= factor;
			y[i] *= factor;
		}
	}
	public String toSring(){
		String word = "{";
		for(int i = 0; i < x.length; i++){
			word+="( "+x[i]+", "+y[i]+" ),";
		}
		word +="}";
		return word;
	}
	
	private int grayFactor = 255; // the gray colour of the outline
	private int decay = -10; // the rate at which the color increases or dicreases in intensity
	
	public void draw ( Graphics g, JFrame canvas){
		// draw the polygon
		for(int i = 0; i < x.length; i++){
			convertX[i] = (int)(finalX[i]);
			convertY[i] = (int)(finalY[i]);
		}

		g.setColor(colour);
		if (select)
			g.drawPolygon(convertX,convertY,finalX.length);
		else{
			g.fillPolygon(convertX,convertY,finalX.length);
			g.setColor(new Color ( grayFactor, grayFactor, grayFactor ) );
			g.drawPolygon(convertX,convertY,finalX.length);
			grayFactor += decay;
			if (grayFactor <= 0 || grayFactor >= 255 ){
				decay *= -1;
				grayFactor += decay;
			}
			
			
		}
		
	}
	public Poly produce(){
		double[]tempX = new double[x.length];
		double[]tempY = new double[y.length];
		for(int i = 0; i < tempX.length; i++){
			tempX[i] = x[i];
			tempY[i] = y[i];
		}
		Poly temp = new Poly(tempX,tempY,this.colour);
		return temp;
	}
	
}