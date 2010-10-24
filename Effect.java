import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.awt.image.*;

public class Effect{

	protected double size;
	protected double life;
	protected double rate;
	protected double x;
	protected double y;
	protected double maxSize;
	protected boolean alive = true;

	public int randint(int a, int b){
		return (int)(Math.random()*(b-a+1)) + a;

	}

	public int randint(double a, double b){
		return randint((int)a,(int)b);
	}

	public Effect(double x, double y, double size, double life){

		this.x = x ;
		this.y = y ;
		this.size = size ;
		this.life = life ;
		rate = size/life ;
		this.maxSize = size ;

	}
	// methods here are generally called in this manner:
	// work(), scale(), draw().

	public void scale( double current, double normal, double[] ref){} // scale the object in terms of the current size of the map and location
	public void work(){} // performs any logic decisions or mathematical calculations for the object. Ussually this method calls kill(), when life = 0
	public void shadow(Graphics g, JFrame c){} // draw a shadow of a unit
	public boolean getAlive(){return alive;} // return whether the unit is alive
	public void kill(){alive = false;} // the Effect set is purged of all effect's whose getAlive() value return false
	public void draw(Graphics g, JFrame c){} // draw the effect

}