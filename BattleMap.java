/* --------------------------- Turret Defence -----------------------------
 *
 * The objective of this game is to survive for as long as possible. Points
 * are given based on the number of kills. The higher level the monster killed
 * the more points earned. You loose 10 monetary points everytime a monster makes it to the
 * center. If you have below zero monetary points, you loose the game. No, it is not
 * possible to go below zero by simply purchasing upgrades. The only way to go below
 * zero is to have enough units flood the center.
 *
 * There are several types of towers:
 * Lightning Tower - Shoots a lightning bolt directly ahead
 * Force Field - Fires lightning bolts in all directions
 * Bullet Tower - The bullet tower shoots a barrage of bullets at the enemy.
 * Laser Tower - Shoots a laser that has a guranteed hit rate, but low damage.
 */

import java.util.* ;
import java.util.concurrent.*;
import java.awt.* ;
import java.awt.event.* ;
import java.io.* ;
import javax.swing.* ;
import java.awt.image.* ;
import javax.imageio.* ;

/*
 * BattleMap class holds together the whole game. The run() method is the main game loop.
 *
 */

public class BattleMap extends JFrame implements MouseWheelListener,KeyListener, MouseMotionListener,MouseListener {

	private Graphics2D dbg;
	private BufferedImage dbImage;

	private int backColour = 250; // the colour of the background (monochrome)

	private double [] ref = new double[3]; // position of the whole map
	private double MAX_SIZE = 50; // normal size of a tile
	private double cur_size = 50; // the current size of the tiles
	private int TILE_AMOUNT = Global.TILE_AMOUNT; // number of times is TILE_AMOUNT x TILE_AMOUNT
	private int scroller = 0; // how much the mouse is scrolling
	private int UP = (int)('s');
	private int DOWN = (int)('w');
	private int LEFT = (int)('d');
	private int RIGHT = (int)('a');
	private boolean keyboardTouched = false; // if any keyboard button is pressed
	private int speed = 5; // speed at which the map moves up/down/left/right
	private int score = 0;
	boolean [] keys = new boolean[2000] ;
	private int [] mP = new int[2] ; // mouse coordinates
	private int [] tP = new int[2] ; // previous mouse coordinates
	private double ratio = 1 ; // the ratio between the old size of tile and new size of tile
	static ArrayList<String> words = new ArrayList<String>() ; // useless
	private int MB = 0 ; // which button is currently pressed


	private GUI gui = new GUI(dbg, this) ;
	private Player human = new Player(gui) ;

	/** LEVEL VARIABLES **/
	private int kills = 0 ;
	private int[] levelKill = {100,200,50} ;
	private int unitsBuilt = 0;
	private boolean once = false;
	private int modulo = 50;
	private int oldLevel = 0;


	private HashMap<String,Tower> buildSet = new HashMap<String,Tower>(); // what the player can build
	private LinkedList<Unit> enemyBuildSet = new LinkedList<Unit>(); // what the enemy can build
	private LinkedList<Unit> unitSet = new LinkedList<Unit>(); // all active units on the field
	private LinkedList<Tower> towerSet = new LinkedList<Tower>( ); // all active towers on the field
	private LinkedList<Weapon> weaponSet = new LinkedList<Weapon>(); // all active warheads on the field
	private LinkedList<Effect> effectSet = new LinkedList<Effect>(); // all active effects on the field
	private Cell[][] grid = new Cell[ Global.TILE_AMOUNT ][ Global.TILE_AMOUNT ];
	private Image mainBack;
	private boolean scrolling = false; // flag for mouse scroll

	private int lag = Global.TILE_AMOUNT*2 - 2 ; // the time difference between successive unit generation
	private Font regular = new Font("Arial",Font.BOLD,12) ;

	/* GAME STATUS VARIABLES */
	private int TITLE = 1;
	private int MAIN = 2;
	private int FINISH = 3;
	private int INSTRUCTION = 4;
	private int GAME_STATUS = TITLE;

	/** FPS variables **/

	int oldTime ;
	int time ;
	int fps = 25 ;
	int maxDelayTime = (int)( (1.0/fps)*1000 );

	public int[] map_translate2D( double[] p){ // takes a mouse point and translates it to map coordinates
		int[] newP = new int[2];
		newP[0] = (int)(( p[0]-ref[0] )*MAX_SIZE/cur_size);
		newP[1] = (int)(( p[1]-ref[1] )*MAX_SIZE/cur_size);
		return newP;
	}

	public int[] map_translate2D( int[] p ){
		int[] newP = new int[2];
		newP[0] = (int)(( p[0]-ref[0] )*MAX_SIZE/cur_size);
		newP[1] = (int)(( p[1]-ref[1] )*MAX_SIZE/cur_size);
		return newP;
	}

	public double[] map_detranslate2D( int[] p ){ // takes a map coordinate and translates it to screen coordinates
		double[] newP = new double[2];
		newP[0] = (p[0]*cur_size)/MAX_SIZE + ref[0];
		newP[1] = (p[1]*cur_size)/MAX_SIZE + ref[1];
		return newP;
	}

	public BattleMap() throws IOException{

		/* start off by loading up the JFrame */

		super("Final Project") ;

		// create the cell grid

		for(int i = 0; i < Global.TILE_AMOUNT;i ++){
			for(int j = 0; j < Global.TILE_AMOUNT; j++){
				int [] tempPos ={i,j};
				grid[i][j] = new Cell(tempPos);
			}
		}
		mainBack = Toolkit.getDefaultToolkit().createImage ("TowerDefence.JPG");

		setSize( Global.WIDTH,Global.HEIGHT) ;
		addMouseWheelListener(this) ;
		addKeyListener(this) ;
		addMouseMotionListener(this) ;
		addMouseListener(this) ;
		setDefaultCloseOperation (EXIT_ON_CLOSE) ;

		// initialize the gui
		int[] pos = {0,0} ;
		gui.addMenu("main",pos,1,8) ;
		gui.addMenu("upgrades",pos,1,8) ;
		int[] xPos = {320,400};
		gui.addMenu("intro",xPos,1,5) ;
		gui.setVisible("intro",true) ;
		gui.setVisible("upgrades",false) ;
		gui.setVisible("main",false) ;

		fillGUI() ;
		fillEnemyList();
		setVisible(true) ;

		while(true){

			gui.setVisible("intro",true) ;
			gui.setVisible("upgrades",false) ;
			gui.setVisible("main",false) ;
			titlePage();

			if(GAME_STATUS == INSTRUCTION){

				gui.setVisible( "intro",false ) ;
				gui.setVisible( "upgrades",false ) ;
				gui.setVisible( "main",false ) ;
				instructions() ;
			}

			if(GAME_STATUS == MAIN){

				gui.setVisible("intro",false) ;
				gui.setVisible("upgrades",false) ;
				gui.setVisible("main",true) ;
				run() ;
			}
			if(GAME_STATUS == FINISH){
				gui.setVisible("intro",false) ;
				gui.setVisible("upgrades",false) ;
				gui.setVisible("main",false) ;
				finish() ;
			}
		}
	}

	public void keyPressed(KeyEvent e){

		////System.out.println(e.getKeyChar()) ;
		keys[(int)(e.getKeyChar())] = true ;
		keyboardTouched = true;

	}

	public void keyReleased(KeyEvent e){
		keys[(int)(e.getKeyChar())] = false ;
		keyboardTouched = false;
	}

	public void keyTyped( KeyEvent e){}

	public void mouseMoved( MouseEvent e){

		mP[0] = e.getX()-Global.topX ;
		mP[1] = e.getY()-Global.topY ;
 		if( e.getButton() == MouseEvent.BUTTON1 )MB = 1 ;
 		if( e.getButton() == MouseEvent.BUTTON3 )MB = 2 ;
	}

	public void mouseDragged( MouseEvent e ){

		mP[0] = e.getX()-Global.topX ;
		mP[1] = e.getY()-Global.topY ;
 		if( e.getButton() == MouseEvent.BUTTON1 ) MB = 1 ;
 		if( e.getButton()== MouseEvent.BUTTON3 )MB =2 ;

	}

 	public void	mouseEntered(MouseEvent e){}
 	public void mouseExited(MouseEvent e){}
 	public void mousePressed(MouseEvent e){

 		if(e.getButton() == MouseEvent.BUTTON1) MB = 1;
 		if( e.getButton()== MouseEvent.BUTTON3 )MB =2;

 	}

 	public void mouseReleased(MouseEvent e){ MB = 0; }
	public void mouseClicked( MouseEvent e){}

	public void mouseWheelMoved(MouseWheelEvent e){

		scroller = e.getWheelRotation()*2;
		scrolling = true;
	}
	public void scaleWorld (){ // zooming in and out

	/* aside from resizing the map, it also fixes the position of the map
		so that the zooming looks right */

		if ( scrolling = true){
			scrolling = false;
			double old_size = cur_size;
			double centerX = 0;
			double centerY = 0;

			if ( scroller < 0 && cur_size > 13 ){
				centerX = Global.WIDTH/2 - ref[0];
				centerY = Global.HEIGHT/2 - ref[1];

				cur_size += scroller*cur_size/15;
				ratio = cur_size/old_size;

				// adjust the center position of the map
				centerX *= ratio;
				centerY *= ratio;

				// adjust the map position based on the new center
				ref[0] = (centerX-Global.WIDTH/2)*-1;
				ref[1] = (centerY-Global.HEIGHT/2)*-1;

				if (cur_size < 15){
	            			ref[0] -= ((ref[0]-(Global.WIDTH/2/3.1))/2);
	            			ref[1] -= (ref[1]/1.0001)-50;

				}
			}

			else if( scroller >= 0 && cur_size < 200 ){
				centerX = mP[0]-ref[0];
				centerY = mP[1]-ref[1];
				cur_size += scroller*cur_size/15;
				ratio = cur_size/old_size;
				centerX*= ratio;
				centerY*= ratio;
				ref[0] = (centerX-mP[0])*-1;
				ref[1] = (centerY-mP[1])*-1;
			}
			scroller = 0;
		}
	}
	public void moveMap () { // move the map based on keyboard inputs

		if (keys[UP] == true){
			ref[1] -= speed;

		}
		else if(keys[DOWN] == true){
			ref[1] += speed;

		}
		if (keys[LEFT] == true){
			ref[0] -= speed;

		}
		else if (keys[RIGHT] == true){
			ref[0] += speed;

		}


	}

	private ArrayList<Point> startSet = new ArrayList<Point>(); // all the locations a unit can be generated from

	public void generateStartPositions(){ // originall for testing purposes, this is the standard setup

		for(int i = 1; i < TILE_AMOUNT; i++ ){ startSet.add(new Point(0,i)); }
		for(int i = 1; i < TILE_AMOUNT; i++ ){ startSet.add(new Point(i,0)); }
		for(int i = 1; i < TILE_AMOUNT; i++ ){ startSet.add(new Point(TILE_AMOUNT-1,i)); }
		for(int i = 1; i < TILE_AMOUNT; i++ ){ startSet.add(new Point(i,TILE_AMOUNT-1)); }

		Collections.shuffle( startSet );

		Tower tmp = new LaserTower( (TILE_AMOUNT/2 - 1)*MAX_SIZE-MAX_SIZE/2,(TILE_AMOUNT/2 - 1)*MAX_SIZE-MAX_SIZE/2 ) ;
		tmp.init( unitSet,towerSet,grid,weaponSet,effectSet ) ;
		towerSet.add( tmp ) ;
		tmp = new LaserTower( (TILE_AMOUNT/2 + 1)*MAX_SIZE-MAX_SIZE/2,(TILE_AMOUNT/2 + 1)* MAX_SIZE-MAX_SIZE/2 ) ;
		tmp.init( unitSet,towerSet,grid,weaponSet,effectSet ) ;
		towerSet.add( tmp ) ;
		tmp = new LaserTower( (TILE_AMOUNT/2 - 1)*MAX_SIZE-MAX_SIZE/2,(TILE_AMOUNT/2 + 1)*MAX_SIZE-MAX_SIZE/2 ) ;
		tmp.init( unitSet,towerSet,grid,weaponSet,effectSet ) ;
		towerSet.add( tmp ) ;
		tmp = new LaserTower( (TILE_AMOUNT/2 + 1)*MAX_SIZE-MAX_SIZE/2,(TILE_AMOUNT/2 - 1)*MAX_SIZE-MAX_SIZE/2 ) ;
		tmp.init( unitSet,towerSet,grid,weaponSet,effectSet ) ;
		towerSet.add( tmp ) ;
	}


	public void generateUnit() { // create a unit on the map

		lag++;
		if( lag%modulo == 0 ){
			startSet.add( startSet.remove(0) ); // cycle the starting position list

			if( unitsBuilt < levelKill[oldLevel%enemyBuildSet.size()]*2){

				enemyBuildSet.get( oldLevel%enemyBuildSet.size() ).produce(startSet.get(0).getX()*MAX_SIZE + 25,startSet.get(0).getY()*MAX_SIZE + 25,oldLevel );
				unitsBuilt++;
			}


			if( kills >= levelKill[oldLevel%enemyBuildSet.size()] ){
				oldLevel++;
				unitsBuilt = 0;
				effectSet.add(new TitleShine(512/5,"Level "+(oldLevel)));
				kills = 0;
			}
			modulo--;

			if (modulo == 0){ modulo = 50;}
		}
	}

	public void titlePage(){
		while(GAME_STATUS == TITLE){

			time = (int)( System.currentTimeMillis() );
			gui.check(mP[0],mP[1],MB); // check the gui inputs
			String name = gui.getActiveButtonName("intro");
			if(name != null){
				if(name.equals("I N S T R U C T I O N S")){
					GAME_STATUS = INSTRUCTION;
					return;
				}
				if(gui.getActiveButtonName("intro").equals("P L A Y")){
					GAME_STATUS = MAIN;
					return;
				}
			}
			repaint();
			delay( (int)(maxDelayTime - (System.currentTimeMillis()-time)) );
		}
	}
	private String [] instruct = {
		"OBJECTIVE: prevent units from moving into the center of the map.",
		"If enough units move into the center, you will loose",
		"The game is over when your total money goes below zero.",
		"You loose 10 points everytime a unit goes to the center",
		"",
		"CONTROLS:",
		"W: up  S: down  A: left   D: right",
		"Mouse wheel: zoom in and out",
		"",
		"left click to build a unit and right click to clear everything.",
		"left click and drag after clearing to highlight units.",
		"highlighted units can be upgraded.",
		"",
		"FUN FACTOR LEVEL: lol"
	};
	public void instructions(){
		while(GAME_STATUS == INSTRUCTION){
			double time = System.currentTimeMillis();
			if(keyboardTouched == true){
				GAME_STATUS = TITLE;
			}
			repaint();
			delay( (int)(maxDelayTime - (System.currentTimeMillis()-time)) );
		}
	}
	public void instructionsPaint(){

		dbg.setColor(Color.black);
		dbg.fillRect(0,0,Global.WIDTH,Global.HEIGHT);
		dbg.setColor(Color.white);
		for(int i = 0; i < instruct.length; i++){
			dbg.drawString(instruct[i],(int)(Global.WIDTH*0.25),(int)(Global.HEIGHT*0.125+i*12+12));
		}
	}
	public void finish(){
		while(GAME_STATUS == FINISH){

			repaint();
		}
	}

	public void run(){ // this is the main game

		generateStartPositions ();
		while(done == false){}
		time = (int)(System.currentTimeMillis());
		while(true){
			time = (int)( System.currentTimeMillis() );
			if(human.getAlive() == false){
				GAME_STATUS = FINISH ;
				return;
			}

			moveMap(); // move the map
			scaleWorld();
			gui.check(mP[0],mP[1],MB); // check the gui inputs
			human.updateMouse(mP,MB,map_translate2D(mP)); // update the mouse for the player
			human.upgradeSelection();

			if ( mP[0] > 128 || mP[1] > 16*4 ){
				human.highlight( towerSet ); // see if the user highlighted anything
			}


			checkGUI();


			/* units, towers, and effects are iterated over.
                         * Each object is scaled and it's "work" method is called
                         * Work method updates the unit based on the battlefield
                         * if the object dies after updating, it's alive status becomes false
                         */

			Iterator<Unit> i = unitSet.iterator();
			while( i.hasNext() ){ // scale all polygons
				Unit j = i.next();
				j.scale( cur_size, MAX_SIZE,ref );
				j.work();

				// if the unit is in the center of the screen, remove it
				if ( j.getCellX() == (int)(TILE_AMOUNT/2)-1 && j.getCellY() == (int)(TILE_AMOUNT/2)-1 ){
					j.kill() ;
					backColour += 50;
					effectSet.add(new CircleTowerEffect(TILE_AMOUNT*MAX_SIZE/2 - 25 ,TILE_AMOUNT*MAX_SIZE/2 -25,MAX_SIZE,1000));
					kills--;
					human.addMoney(-11); // lose some money, get closer to death
					score--;


				}

				if ( j.getAlive() == false ){
					i.remove();
					human.addMoney(1);
					kills++;
					score++;
				}
			}

			Collections.sort(towerSet);

			Iterator<Tower> k = towerSet.iterator();

			while(k.hasNext()){ // scale all polygons
				Tower j = k.next();
				j.scale(cur_size, MAX_SIZE,ref);
				j.work();
				if ( j.getAlive()== false ){
					j.kill();
					k.remove();
				}
			}

			Iterator< Weapon > l = weaponSet.iterator();

			while( l.hasNext() ){ // scale all polygons
				Weapon j = l.next();
				j.scale( cur_size, MAX_SIZE,ref );
				j.work();
				if ( j.getAlive()== false ){
					l.remove();
				}
			}

			Iterator<Effect> m = effectSet.iterator();
			while( m.hasNext() ){ // scale all polygons
				Effect j = m.next();

				j.scale(cur_size, MAX_SIZE,ref);
				j.work();

				if (j.getAlive()== false){
					m.remove();

				}
			}
			generateUnit();

			delay( (int)(maxDelayTime - (System.currentTimeMillis()-time)) );
			repaint();
		}
	}
	public int randint(int a, int b){
		return (int)(Math.random()*(b-a+1)) + a;
	}

	HashMap<String,Unit>choosingMenu = new HashMap<String,Unit>();
	public void fillEnemyList(){
		enemyBuildSet.add( new StarTroop() );
		enemyBuildSet.add(new Unit() );
		enemyBuildSet.add( new HeavyTank() );
		for( Unit i : enemyBuildSet ){
			i.init(unitSet,towerSet,grid,weaponSet,effectSet);
		}
	}
	public void fillGUI(){ // populate the GUI with buttons

		buildSet.put("laser gun",new LaserTower() );
		buildSet.get("laser gun").init(unitSet,towerSet,grid,weaponSet,effectSet);
		gui.add("main","laser gun","");
		buildSet.put("lightning gun", new LightningTower() );
		buildSet.get("lightning gun").init(unitSet,towerSet,grid,weaponSet,effectSet);
		gui.add("main","lightning gun","");
		buildSet.put("bullet gun", new Tower() );
		buildSet.get("bullet gun").init(unitSet,towerSet,grid,weaponSet,effectSet);
		gui.add("main","bullet gun","");
		buildSet.put("Force Field", new ForceTower() );
		buildSet.get("Force Field").init(unitSet,towerSet,grid,weaponSet,effectSet);
		gui.add("main","Force Field","");
		buildSet.put("Freeze Tower", new Wall() );
		buildSet.get("Freeze Tower").init(unitSet,towerSet,grid,weaponSet,effectSet);
		gui.add("main","Freeze Tower","");

		gui.add("upgrades","+10% range","");
		gui.autoDeclick("upgrades","+10% range",true);
		gui.add("upgrades","+10% attack","");
		gui.autoDeclick("upgrades","+10% attack",true);
		gui.add("upgrades","+1 weapon","");
		gui.autoDeclick("upgrades","+1 weapon",true);
		gui.add("upgrades","+10% damage radius","");
		gui.autoDeclick("upgrades","+10% damage radius",true);

		gui.add("intro","I N S T R U C T I O N S","");
		gui.autoDeclick("intro","I N S T R U C T I O N S",true);
		gui.add("intro","P L A Y","");
		gui.autoDeclick("intro","P L A Y",true);


	}

	public void checkGUI(){ // check if the user pressed a button, and build the proper tower
		if(gui.getVisible("main")){
			human.build( grid,buildSet,gui.getActiveButtonName("main"),towerSet );
		}


	}

	boolean done = false;
	boolean drawing = false;
	private int centerColor = 0;
	private int centerShifter = 10;
	public static ArrayList<String> statList = new ArrayList<String>();

	public void titlePaint(){
		if(GAME_STATUS == TITLE){
			dbg.setColor( Color.red );
			dbg.setFont( regular );
			dbg.drawImage(mainBack,0,0,this);
			gui.draw(dbg,this);

		}

	}
	public void finishPaint(){
		dbg.setColor(Color.black);
		dbg.fillRect(0,0,Global.WIDTH,Global.HEIGHT);
		dbg.setColor(Color.white);
		dbg.drawString("final score: "+score,(int)(Global.WIDTH*0.25),(int)(Global.HEIGHT*0.25));
	}
	public void mainGamePaint(){

		// draw the background colour

		if(backColour > 255){
			backColour = 255;
		}

		dbg.setColor (new Color(backColour,backColour,backColour));

		if(backColour > 0){
			backColour -= 10;
			if(backColour < 0){
				backColour = 0;
			}
		}

		dbg.fillRect (0, 0, Global.WIDTH,Global.HEIGHT);

		int newPX = (int)((TILE_AMOUNT/2 - 1) * cur_size + ref[0]);
		int newPY = (int)((TILE_AMOUNT/2 - 1) * cur_size + ref[1]);

		dbg.setColor( new Color(centerColor,centerColor,centerColor) );

		centerColor += centerShifter;
		if( centerColor < 0 || centerColor > 255 ){
			centerShifter *= -1;
			centerColor += centerShifter;
		}

		dbg.fillRect(newPX,newPY,(int)(cur_size),(int)(cur_size));


		dbg.setColor( Color.gray);
		// draw the grid lines
		for(int i = 0; i <= TILE_AMOUNT; i++){
			int X = (int)(ref[0]);
			int Y = (int)(ref[1]);
			dbg.drawLine( (int)(X+i*cur_size), Y , (int)(X+i*cur_size) ,(int)(Y+cur_size*TILE_AMOUNT) );
			dbg.drawLine( X , (int)(Y+i*cur_size) , (int)(X+cur_size*TILE_AMOUNT) , (int)(Y+i*cur_size) );
		}

		if( gui.getActiveButton("main") != null ){

			int[] newP = map_translate2D(mP);
			int cellX = (int)(newP[0]/MAX_SIZE);
			int cellY = (int)(newP[1]/MAX_SIZE);
			newP[0] = (int)((int)(newP[0]/MAX_SIZE)*cur_size+ref[0]);
			newP[1] = (int)((int)(newP[1]/MAX_SIZE)*cur_size+ref[1]);
			if(cellX >= 0 && cellX < TILE_AMOUNT && cellY >= 0 && cellY < TILE_AMOUNT ){
				if( human.checkAroundCell(grid,cellX,cellY) == false ){

					dbg.setColor(Color.red);
					dbg.fillRect(newP[0],newP[1],(int)(cur_size),(int)(cur_size));
				}
				else{
					dbg.setColor(Color.green);
					dbg.fillRect(newP[0],newP[1],(int)(cur_size),(int)(cur_size));
				}

			}
		}

		drawUnits( dbg ) ;
		drawEffects( dbg ) ;
		drawWeapons( dbg ) ;


		dbg.setColor( Color.green );
		gui.draw(dbg,this);
		statBoard(dbg);
		human.draw(dbg,this);
	}

	public void paint(Graphics g) { // main method which gets called by the JFrame object to draw on screen

		drawing = true;
		if (dbImage == null){

			dbImage = new BufferedImage(Global.WIDTH, Global.HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
			dbg = dbImage.createGraphics();


			done = true;
		}
		if (GAME_STATUS == MAIN){

			mainGamePaint();
		}
		else if(GAME_STATUS == TITLE){

			titlePaint();
		}
		else if (GAME_STATUS == FINISH){

			finishPaint();
		}
		else if (GAME_STATUS == INSTRUCTION){

			instructionsPaint();
		}
		else{

		}
		g.drawImage(dbImage,Global.topX,Global.topY,this);
		drawing = false;

	}
			int top[] = {0,0};
			int bottom[] = {Global.WIDTH,Global.HEIGHT};
			Image scaledCut;

	int titleColor = 255;
	int shifter = -1;

	public void statBoard( Graphics g ){

		g.setColor(Color.gray);
		g.fillRect(Global.statX,Global.statY,Global.statWidth,Global.statHeight);
		g.setColor(Color.black);

		g.setFont( regular );
		titleColor += shifter;
		if( titleColor > 255 || titleColor < 0){
			shifter *= -1;
			titleColor += shifter;
		}
		for(int i = 0; i < BattleMap.statList.size(); i++){
			g.drawString(BattleMap.statList.get(i),0,Global.statY+i*12+12);
		}
		BattleMap.statList.clear();

		BattleMap.statList.add(" kills: "+kills);
		BattleMap.statList.add(" tower build cost: "+Global.towerCost);

	}

	public void drawUnits(Graphics g){ // draw all units on the field

		for(Unit i: unitSet){
			i.draw(g,this);

		}

		for(Tower i: towerSet ){ // draw all the towers on the field
			i.draw(g,this);
		}
	}

	public void drawWeapons( Graphics g ){ // draw all the weapons on the field
		for(Weapon i : weaponSet){
			i.draw(g,this);
		}
	}

	public void drawEffects( Graphics g){ // draw all the effects on the field
	try{
		Iterator<Effect> i = effectSet.iterator();
		while(i.hasNext()){
			Effect j = i.next();
			j.draw(g,this);
		}
	}
	catch(Exception e){

	}

	}

	public static void main(String[]args) throws IOException{
		BattleMap prg = new BattleMap();
	}

	public static void comment(String n){
		words.add(n);
		if (words.size() > 10 ){ // only add 10 comments to the comment box
			words.remove(0);
		}
	}

	public void delay (long n){
		// - if the value is <1, there is no point in delaying...
		if (n < 1)
			return;

		try{
		    Thread.sleep (n);
		}
		catch (InterruptedException e){}
    }
}