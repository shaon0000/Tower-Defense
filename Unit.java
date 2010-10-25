import java.util.*;
import java.awt.*;
import javax.swing.*;
public class Unit {

	/* This is the base class for all the other units and towers in the game
	 * It has methods to allow the unit to move around the map based on some final
	 * tile. Since it is polygon based, the body can grow or shrink.
	 *
	 * The object can use the test body which is a static object for the Unit class
	 * the variables are referred with "test" because they are for testing
	 * purpose only
	 */
	static double[]testX = {-5,0,5,1,5,0,-5,-1};
	static double[]testY = {-5,-1,-5,0,5,1,5,0};
	static Poly testBody = new Poly(testX,testY);
	static double[] turretX = {0,7,0};
	static double[] turretY = {-1,0,1};
	static Poly testTurret = new Poly( turretX,turretY,new Color(100,200,254) );
	// the box is the bounding box width and height
	protected int [] box = {(int)(10*1.5),(int)(10*1.5)};
	protected double [] boxX;
	protected double [] boxY;
	protected double [] pos;
	protected boolean alive = true;
	protected int rand;

	protected double hp=100; // hit points
	protected double maxHp = 100;
	protected double ap; // attack power
	protected double range; // range of the unit's attack
	protected double vx=2; // velocity in the x direction
	protected double vy=0; // velocity in the y direction
	protected double v=2;
	protected boolean frozen = false; // can the object move or not
	protected double bulletV;
	protected double bulletRad;
	protected double explosionLife;
	protected double ang; // the current direction of the turret
	protected int cellX;
	protected int cellY;
	protected int oldCellX;
	protected int oldCellY;
	public double getRange(){ return range;}
	public double getAttack(){ return ap;}
	public double getLevel(){ return level;}
	/***======= path finding variables ==========***/
	protected boolean searchingPath; // is the object searching for a path
	protected boolean move; // is the object moving
	protected boolean foundPath; // did the object find a path
	protected boolean creatingPath; // is the object currently creating a path
	protected boolean movingToNextCell = false;
	protected boolean atFinalCell = false;
	protected Path path;
	protected LinkedList<Point> pathSet = null;
	protected Point nextCell = null;
	protected int nextX = 0;
	protected int nextY = 0;
	protected double finalPointX = 0;
	protected double finalPointY = 0;
	protected int destinationCellX;
	protected int destinationCellY;
	protected Cell[][] grid;
	/***** COMMAND VARIABLES *****/
	protected boolean select; // is the object selected
	protected boolean attack; // is the object going to attack
	protected boolean searchingForEnemey; // is the object searching for an enemy;
	protected boolean foundEnemy; // did the unit fnd an enemy
	/* ~~~~~~~~~~~~~~~~~~~~~ scan/attack/hunt variables ~~~~~~~~~~ */
	protected Unit target; // the enemies that this unit is going to keep hunting
	protected LinkedList<Unit> allTargets = new LinkedList<Unit>();
	protected int heat;
	protected int heatCounter;
	protected LinkedList<Poly> body; // body frame
	protected LinkedList<Poly> turret; // turret frame
	protected Graphics g;
	protected JFrame c;
	protected int level = 1;
	public void nextLevel(){
		level++;
		if(level%5 == 0){
			grow(1.5);
		}
	}

	public void upgradeLevel( int n ){
		maxHp *= Math.pow(2,n);
		hp = maxHp;
		level = n;
	}
	public void multiplyRange(double val){range *= val;}
	public void multiplyAP(double val){ap *= val;}
	public void multiplyHP( double val ){maxHp *= val;}
	public void multiplyDamageRadius( double val ){bulletRad *= val;}
	public int randint(int a, int b){return (int)(Math.random()*(b-a+1)) + a;}
	public double getX(){return pos[0];}
	public double getY(){return pos[1];}
	public int getCellX(){return cellX;}
	public int getCellY(){return cellY;}
	public String toString(){return "hp: "+hp+" cell: ("+cellX+","+cellY+")";}
	public Unit(double X,double Y){
		this.pos = new double[2] ;
		pos[0] = X;
		pos[1] = Y;
		cellX = (int)(pos[0]/Global.MAX_SIZE);
		cellY = (int)(pos[1]/Global.MAX_SIZE);
		oldCellX = cellX;
		oldCellY = cellY;
		body = new LinkedList<Poly>();
		turret = new LinkedList<Poly>();
		maxHp = hp;
		heatCounter = heat;
		initStats();
		initBody();
		initTurret();
	}
	public Unit(){
		this.pos = new double[2] ;
		pos[0] = -50;
		pos[1] = -50;
		cellX = (int)(pos[0]/Global.MAX_SIZE);
		cellY = (int)(pos[1]/Global.MAX_SIZE);
		oldCellX = cellX;
		oldCellY = cellY;
		body = new LinkedList<Poly>();
		turret = new LinkedList<Poly>();
		maxHp = hp;
		initStats();
		initBody();
		initTurret();
	}
	public void initStats(){
		 hp=2; // hit points
		 ap=0; // attack power
		 range=0; // range of the unit's attack
		 vx=10; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=10; // velocity
		 heat = 3;
		 bulletV = 24;
		 bulletRad = 3;
		 rand = 25;
		testingMode();
	}

	public void initBody(){}
	public void initTurret(){}
	protected LinkedList<Unit> units;
	protected LinkedList<Tower> tower;
	protected LinkedList<Weapon> weapons;
	protected LinkedList<Effect> effects;
	public void init(LinkedList<Unit> units, LinkedList<Tower> tower, Cell[][] grid, LinkedList<Weapon> weapons, LinkedList<Effect> effects){
		this.units = units ;
		this.tower = tower ;
		this.grid = grid ;
		this.weapons = weapons ;
		this.effects = effects ;
		if(pos[0] != -50){
			this.path = new Path( this.grid );
			pathSet = this.path.getPath();
			if(v == 0){ // if the velocity is 0, than this is a tower
				grid[cellX][cellY].block();
			}
		}
	}

	public void testingMode(){
		body.add(Unit.testBody.produce());
		turret.add(Unit.testTurret.produce());
		grow(3.0);
	}
	public void addBody(int R, int G, int B, double[] X, double [] Y){body.add( new Poly(X,Y,new Color(R,G,B)));}
	public void addTurret(int R, int G, int B, double[] X, double [] Y){turret.add( new Poly(X,Y,new Color(R,G,B)));}

	public void grow( double factor){
		for( Poly i: body ){
			i.grow( factor );
		}
		for (Poly i : turret){
			i.grow( factor );
		}
		bulletRad *= factor;
		box[0] *= factor;
		box[1] *= factor;
		rand *= factor;
	}
	double freezeTimer = 0;
	public void freeze( double n ){
		freezeTimer = n ;
		frozen = true ;
	}
	public void rotateBody(double theta){
		for(Poly i: body){
			i.rotate(theta);
		}
	}
	public void damage( double amount){
		hp -= amount;
		if (hp <= 0){
			alive = false;
		}
	}
	public void damageAfterCheck(double size, double x, double y, double amount){
		if(Math.hypot(x-pos[0],y-pos[1]) < size+(box[0]/2)){
			damage(amount);
		}
	}
	public void kill(){
		grid[cellX][cellY].remove(this);
		effects.add( new Explosion(pos[0],pos[1],box[0],20) );
		alive = false;
	}

	public boolean getAlive(){return alive;}

	public void moveOrder(double endX, double endY){
		if(move == true){
			stopMoving();
		}
		move = true;
		searchingPath = true;
		findPath(endX,endY);
	}
	public void updateCellPosition(){
		cellX = (int)( pos[0]/Global.MAX_SIZE );
		cellY = (int)( pos[1]/Global.MAX_SIZE );
		
		if (cellX != oldCellX || cellY != oldCellY){
			grid[oldCellX][oldCellY].remove(this);
			grid[cellX][cellY].add(this);
		}
	}
	public void work(){
		if(hp <= 0){
			kill();
			return;
		}
		if (frozen == false){
			updateCellPosition();
			if (move == true){
				if(searchingPath){
					searchPath();
				}
				if(foundPath){
					startMoving();
					moveToCell();
				}
			}
			oldCellX = cellX;
			oldCellY = cellY;
		}
		else{
			freezeTimer -= 0.25;
			if (freezeTimer <= 0){
				frozen = false;
				freezeTimer = 0.0;
			}
		}
	}

	public void findPath(double endX, double endY){ // this is the beginning of the path finding
		if(path == null){
			path = new Path( this.grid );
		}
		this.path.start(cellX,cellY,(int)(endX/Global.MAX_SIZE),(int)(endY/Global.MAX_SIZE));
		destinationCellX = (int)(endX/Global.MAX_SIZE);
		destinationCellY = (int)(endY/Global.MAX_SIZE);
		finalPointX = endX;
		finalPointY = endY;
	}

	public void searchPath(){
		if (searchingPath){
			path.execute();
			if(path.done()){
				searchingPath = false;
				foundPath = true;
			}
		}
	}
	public void startMoving(){ // once the path is found, the object can start moving
		if(foundPath== true && movingToNextCell == false){
			/* everytime the object arrives at a new point on the path,
			 * pop a new destination from the pathSet. A path has to be already created
			 * and the object must be already at the short-hand destination
			 * The short destination is a node along the path.
			 */
			 if (pathSet.size() != 0){
				nextCell = pathSet.pop() ;
				nextX = (int)nextCell.getX() ;
				nextY = (int)nextCell.getY() ;
			}
		}

	}
	public void moveToCell(){
		/* there are three stages to the objects movement
		 * 1) object is moving to the next cell
		 * 2) object has made it to the next cell and now needs to find the angle to move the next new cell
		 * 3) object has made ito the destination cell and now needs to start moving to it's final point
		 */
		if(grid[nextX][nextY].getBlock()){
			moveOrder(finalPointX,finalPointY);
			return;
		}

		if( (cellX != nextX || cellY != nextY) && movingToNextCell == false && atFinalCell == false){ // stage 2
		// find the new angle that the object needs to head in for the next cell
			double directionX = nextX-cellX;
			double directionY = nextY-cellY;
			double newAngle = Math.atan2(directionY,directionX);
			rotateBody(newAngle-ang);
			rotateV( newAngle-ang);
			ang = newAngle;
			movingToNextCell = true;
		}
		else if (cellX == destinationCellX && cellY == destinationCellY && atFinalCell == false){ // stage 3
			double newAngle = Math.atan2(finalPointY-pos[1],finalPointX-pos[0]);
			atFinalCell = true;
			rotateBody(newAngle-ang);
			rotateV(newAngle-ang);
			ang = newAngle;
		}
		else if (cellX == nextX && cellY == nextY && atFinalCell == false){ // also part of stage 2
		// the object has made it to the next cell, so now a new cell has to aimed for
			movingToNextCell = false;
		}
		if(atFinalCell == true){
			if(finalPointX-pos[0] < vx || finalPointY-pos[1] < vy){
				stopMoving();
			}
		}
		pos[0] += vx;
		pos[1] += vy;
	}

	public void mapCollision(){
		/* although the path avoids obstacles, there is always the
		 * the chance that an obstacle will be placed over time
		 * in its path. In that case, the unit is stopped and a new
		 * path is created from the new location
		 */
		boolean obstacle = false;
		while(grid[cellX][cellY].getBlock() == true){
			obstacle = true;
			pos[0] -= vx;
			pos[1] -= vy;
			updateCellPosition();
		}
		if(obstacle == true){
			moveOrder(finalPointX,finalPointY);
		}
	}
	public void stopMoving(){
		path.reset();
		nextCell = null;
		nextX = 0;
		nextY = 0;
		movingToNextCell = false;
		finalPointX = 0;
		finalPointY = 0;
		destinationCellX = 0;
		destinationCellY = 0;
		searchingPath = false; // is the object searching for a path
		move = false; // is the object moving
		foundPath = false; // did the object find a path
		creatingPath = false; // is the object currently creating a path
		atFinalCell = false;
	}
	public void rotateV( double theta ){
		double newX;
		double newY;
		newX = Math.cos ( theta ) * vx - Math.sin ( theta ) * vy;
		newY = Math.sin ( theta ) * vx + Math.cos ( theta ) * vy;
		vx = newX;
		vy = newY;
	}

	public double distance( Unit enemy ){return Math.hypot(enemy.getX()-pos[0],enemy.getY()-pos[1]);}
	public double getScaledX() {return scaledX;}
	public double getScaledY() {return scaledY;}
	public void rotateTurret(double theta){ // roate the unit based on some angle
		for(Poly i: turret){
			i.rotate(theta);
		}
	}
	protected double scaledX;
	protected double scaledY;
	protected double scaledZ;
	protected double scaledSize;
	protected double scaleVal;
	protected double mapX;
	protected double mapY;
	public void scale( double size, double normal, double[] ref ){
		/*
		 * scale the unit based on the current size of the map,
		 * the normal size of the map,
		 * and position of the map
		 */
		scaleVal = size;
		scaledX = pos[0]*size/normal + ref[0];
		scaledY = pos[1]*size/normal + ref[1];
		scaledZ = 120*size/normal;
		scaledSize = 4*size/normal;
		mapX = ref[0];
		mapY = ref[1];
		for(Poly i: body){ i.scale(size,normal,pos[0], pos[1],ref);}
		for(Poly i: turret){ i.scale(size,normal,pos[0], pos[1],ref);}
	}
	public void highlight(){ // highlight the unit
		for(Poly i: body){ // go through the polygons and put them in "select" mode
			i.setSelect (true) ;
		}
		for(Poly i: turret){
			i.setSelect (true) ;
		}
		select = true;
	}
	public void dehighlight(){ // unhighlight the unit
		for(Poly i: body){ // go through the polygons and turn of their "select" mode
			i.setSelect (false) ;
		}
		for(Poly i: turret){
			i.setSelect (false) ;
		}
		select = false;
	}
	public void draw(Graphics g, JFrame c){
		for(Poly i: body){
			i.draw (g,c) ;
		}
		for(Poly i: turret){
			i.draw(g,c);
		}
		//D3.circle3D(scaledX,scaledY,scaledZ,scaledSize,Color.red,g,c);
		//D3.line3D(scaledX,scaledY,0,scaledX,scaledY,scaledZ,Color.red,g,c);
		if(target != null){
			g.drawLine((int)scaledX,(int)scaledY,(int)target.getScaledX(),(int)target.getScaledY());
		}
		if(v!=0){
			g.setColor(Color.red);
			g.fillRect((int)scaledX,(int)scaledY,(int)(50*scaleVal/Global.MAX_SIZE),(int)(5*scaleVal/Global.MAX_SIZE));

			g.setColor(Color.green);
			g.fillRect((int)scaledX,(int)scaledY,(int)(50*hp*scaleVal/Global.MAX_SIZE/maxHp),(int)(5*scaleVal/Global.MAX_SIZE));
		}
		heatBar(g,c);
		g.setColor(Color.white);
		if ( select == true){}
		else{
			g.drawString("("+level+")",(int)scaledX,(int)scaledY);
		}
	}
	public void heatBar(Graphics g, JFrame c){}
	public String arrayString( double [] a){
		String w = "";
		for (int i = 0; i < a.length; i++){
			w += a[i]+" ";
		}
		return w;
	}

    public boolean check_if_in_Box(double [] rect ){ // returns true if the object is in some box, they are all coordinates
        double [] mBox = {rect[0],rect[1],rect[2],rect[3]}; // make a copy of the box to use
        // the nBox needs to converted into the uclidean box
        double [] nBox = {pos[0]-box[0]/2,pos[1]-box[1]/2,pos[0]-box[0]/2+box[0],pos[1]-box[1]/2+box[1]}; // bouding box of unit
        if(nBox[0] < mBox[0]){ // flip the boxes if the checking box position is behind the big box
			double [] tmp = nBox;
            nBox = mBox;
            mBox = tmp;
        }

        if (nBox[0] > mBox[0] && nBox[0] < mBox[2]){ // check the box according to X
            if(nBox[1] < mBox[1]){ // flip now for "y" cords
            	double [] tmp = nBox;
                nBox = mBox;
                mBox = tmp;
            }
            if(nBox[1] >= mBox[1] && nBox[1] <= mBox[3]){ // check Y cords
                return true;
            }
        }
        return false;
    }
	public void produce( double x, double y, int level){
		Unit tmp = new Unit(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tmp.upgradeLevel(level);
		tmp.moveOrder((Global.TILE_AMOUNT/2)*Global.MAX_SIZE-Global.MAX_SIZE/2,(Global.TILE_AMOUNT/2)*Global.MAX_SIZE-Global.MAX_SIZE/2);
		units.add( tmp );
	}
}
