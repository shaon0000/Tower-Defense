import java.util.*;
import javax.swing.*;
import java.awt.*;

// super class of all the warheads in the game

public class Weapon{
	// starting position
	protected double startX, startY, endX, endY, X, Y, Z, size, ap;
	protected LinkedList<Effect> effects; // once the bullet is done, it adds an explosion to this list
	protected boolean alive = true;
	public Weapon(LinkedList<Effect> effects, double startX, double startY, double endX, double endY, double size, double ap){
		this.effects = effects; X = startX; Y = startY; this.startX = startX; this.startY = startY; this.endX = endX; this.endY = endY; this.size = size; this.ap = ap;
	}
	public void work(){}
	public void scale( double current, double normal, double []ref ){}
	public void draw( Graphics g, JFrame c ){}
	public void kill(){} // remove the warhead and any other reference
	public boolean getAlive(){return alive;}
}
