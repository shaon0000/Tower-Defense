import java.util.*;
import java.awt.*;
import javax.swing.*;

/* The Cell class is used to reduce the amount of checking
 * required for collision detection. Each cell holds a list
 * of units located on the cell. Bullets and weapons can check
 * the cell and damage the units in it.
 */

public class Cell{

	private int size; // number of units in the cell
	private LinkedList<Unit> set; // units in the cell
	private int [] pos; // position of cell
	private boolean block; // if there is a tower in the cell


	public Cell(int[] pos){
		this.pos = pos;
		this.set = new LinkedList<Unit>();
	}
	public void remove( Unit other ){
		this.set.remove( other );
	}

	public void add( Unit other ){
		this.set.add( other );
	}

	public void damageAll(double ap){
		Iterator<Unit> i = set.iterator();

		while(i.hasNext()){
			i.next().damage(ap);
		}
	}

	public void damageBySize( double r, double x, double y, double ap){
		// check if the unit is in range before attacking it
		Iterator<Unit> i = set.iterator();
		while(i.hasNext()){

			i.next().damageAfterCheck(r,x,y,ap);
		}

	}
	public int size(){
		return this.set.size();
	}

	public boolean getBlock(){
		return block;
	}

	public void block(){
		block = true;
	}

	public LinkedList<Unit> getUnitList(){
		return set;
	}
	public Unit getFirstUnit(){
		return set.peek();
	}
	public int getSize(){
		return set.size();
	}

	public int getX(){
		return pos[0];
	}
	public int getY(){
		return pos[1];
	}
}