import java.util.*;
import java.awt.*;
/*
 * This class is used to generate a path
 * The PNode class represents a node on the grid
 * where the final cost is used to decide sorting
 *
 * The Path class has an execute method which must be
 * called in order to get one step closer to finding
 * the path. Normally, the execute method is called once
 * per game loop so that the path is generated over time.
 * This allows room to do other calculations.
 * The path is not the shortest path but it is close to it.
 */


class PNode implements Comparable{
	int X;
	int Y;
	int f; // final cost
	int g; // distance from the original starting node
	int h; // the estimated cost left to the final node

	/* In the PNode class, the child refers to the parent rather
	 * than the parent refering to the child. The path is backward
	 * so that when the nodes are pushed into a LinkedList, they can
	 * be poped in the order of initial node to final node.
	 */
	PNode parent;

	/*      accessor methds         */
	public int getX(){return this.X;}
	public int getY(){return this.Y;}
	public int getCost(){return this.f;}
	public int getBaseCost(){return this.g;}
	public int guessCost(){return this.h;}
	public int compareTo( Object other ){ // used in Collections which put items in a sorted order
		/* method compares the final cost of other node to this node */
		PNode temp = (PNode)(other);
		if (this.getCost() > temp.getCost())return 1;
		if (this.getCost() < temp.getCost())return -1;
		else{return 0;}
	}

	public PNode(int X, int Y, int g, int h){ // the first node does not have a parent
		this.X = X;
		this.Y = Y;
		this.g = g;
		this.h = h;
		this.f = g+h;
		this.parent = null;
	}

	public PNode(int X, int Y, int g, int h, PNode parent){ // every other node however can be traced back to the first node
		this.X = X;
		this.Y = Y;
		this.g = g;
		this.h = h;
		this.f = g+h;
		this.parent = parent;
	}

	public void extraction( LinkedList<Point> set ){ // extract the path from the node

	/* If this is the final node, then a path can be extracted from it.
	 * the method traverses up the tree by going through
	 * each node's parent and adding it to the LinkedList
	 */
		set.push(new Point(this.X,this.Y));
		if (this.parent != null){
			parent.extraction(set);
		}
	}
}

public class Path{
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private LinkedList<Point> pathSet;
	private PriorityQueue<PNode> open;
	private boolean [][] blocked;
	private int [][] check = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,-1},{-1,1},{1,-1}};
	private boolean pathMade = false;
	private Cell[][] mapGrid;
	public LinkedList<Point> getPath(){return pathSet;}
	public String toString(){return "the path exists";}
	public void reset(){
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		pathSet.clear();
		open.clear();
		blocked = new boolean[ mapGrid.length ][ mapGrid.length ];
		pathMade = false;
	}
	public void start(int startX, int startY, int endX, int endY){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		PNode temp = null;
		open.add(new PNode(startX, startY,0,0));
		blocked[0][0] = true;
	}
	public Path(Cell [][] mapGrid){
		this.mapGrid = mapGrid;
		pathSet = new LinkedList<Point>();
		open = new PriorityQueue<PNode>();
		System.out.println(mapGrid);
		blocked = new boolean[ mapGrid.length ][ mapGrid.length ];

	}
	public boolean done(){return pathMade;}
	public void execute(){

		if (pathMade == false && open.size() != 0){
			PNode extract = open.poll();
			int X = extract.getX();
			int Y = extract.getY();

			if (X == endX && Y == endY){
				extract.extraction( pathSet );
				this.pathMade = true;
			}

			int newX;
			int newY;
			for (int i = 0; i < check.length; i++){
				newX = check[i][0]+X;
				newY = check[i][1]+Y;
				if (newX >= 0 && newX < Global.TILE_AMOUNT && newY >= 0 && newY < Global.TILE_AMOUNT ){
					if (blocked[newX][newY] == false && mapGrid[newX][newY].getBlock() == false){
						blocked[newX][newY] = true;
						open.offer( new PNode(newX,newY,extract.getBaseCost()+1,Math.abs(endX-newX)+Math.abs(endY-newY),extract ) );
					}
				}
			}
		}
		else if(open.size() == 0){
			pathMade = true;
		}
	}
}
