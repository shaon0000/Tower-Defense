import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/*
 * BattleMap class holds together the whole game
 */

public class UnitMaker extends JFrame implements MouseWheelListener,KeyListener, MouseMotionListener, MouseListener {

	private Graphics dbg;
	private Image dbImage;
	private int UP = (int)('s');
	private int DOWN = (int)('w');
	private int LEFT = (int)('d');
	private int RIGHT = (int)('a');
	private int speed = 5; // speed at which the map moves up/down/left/right
	private Font dflt = new Font ("Courier New",Font.BOLD,15);
	boolean [] keys = new boolean[2000];
	private int MB = 0;
	private int tb = 0;
	private int WIDTH = 1024;
	private int HEIGHT = 600;
	double ap;
	double hp;
	double v;
	double r;
	int [] mP = new int[2];
	int current = 0;
	ArrayList<ArrayList<Point>> set = new ArrayList<ArrayList<Point>>();
	ArrayList<Color> colorSet = new ArrayList<Color>();
	GUI gui;
	public UnitMaker(){
		super("Unit Maker");
		setSize( WIDTH+Global.topX,HEIGHT+Global.topY );
		addMouseWheelListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setVisible(true);
		colorSet.add( new Color(255,0,0) );
		set.add( new ArrayList<Point>() );
		gui = new GUI(dbg,this);
		int [][] pos = {{0,HEIGHT-(3*16)},
						{0,HEIGHT-(2*16)},
						{0,HEIGHT-(1*16)}};
		String [] polyMenu = {"colour","new polygon","delete"};
		String [] propertyMenu = {"save","load"};
		String [] unitMenu = {"attack","hitpoints","speed","range"};
		gui.addMenu( "property",pos[0],propertyMenu.length,1 );
		for(String i: propertyMenu){
			gui.add("property",i,"");
			gui.autoDeclick("property",i,true);
		}

		gui.addMenu( "poly",pos[1], polyMenu.length, 1);
		for(String i: polyMenu){
			gui.add("poly",i,"");
			gui.autoDeclick("poly",i,true);
		}

		gui.addMenu( "unit",pos[2], unitMenu.length, 1);
		for(String i: unitMenu){
			gui.add("unit",i,"");
			gui.autoDeclick("unit",i,true);
		}
		run();

	}

	public void run(){
		while(true){
			gui.check(mP[0],mP[1],MB);
			//guiCheck();
			repaint();
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

	public void keyPressed(KeyEvent e){

		keys[(int)(e.getKeyChar())] = true;

	}

	public void guiCheck(){

		String active = gui.getActiveButtonName("poly");
		if (active != null){

			if (active.equals("new polygon")){
				set.add( new ArrayList<Point>() );
			}
			if(active.equals("delete")){
				set.remove(current);
			}
			if(active.equals("colour")){
				Scanner in = new Scanner(System.in);
				System.out.println("enter the colours with red, green, blue");
				int R = in.nextInt();
				int G = in.nextInt();
				int B = in.nextInt();
				Color newCol = new Color(R,G,B);
				colorSet.remove(colorSet.size()-1);
				colorSet.add( newCol );
				dbg.setColor( newCol );
			}
			gui.deactivate("poly");
		}
		active = gui.getActiveButtonName("property");
		if (active != null){
			if(active.equals("save")){
				try{
					Scanner in = new Scanner(System.in);
					System.out.println("Please enter the file name with out extensions");
					PrintWriter out = new PrintWriter ( new File(in.next()+".txt") );
					out.println(set.size());

					for(ArrayList<Point> i: set){
						for(Point j: i){
							out.print(j.getX()+" ,");
						}
						out.println("");
						for(Point j: i){
							out.print(j.getY()+" ,");
						}
						out.println("");
					}
					in.close();
					out.close();
					System.out.println("file was properly saved");
				}
				catch(IOException f){
					System.out.println("file not found");
				}
			}
		gui.deactivate("property");
		}
	}

	public void keyReleased(KeyEvent e){
		keys[(int)(e.getKeyChar())] = false;

		if (e.getKeyChar() == 'w' ){
			/*ArrayList<Point> points = set.get(set.size()-1);
			for( Point i: points ){

			}
			*/
		}
		else if (e.getKeyChar() == 'n'){
			set.add( new ArrayList<Point>() );
		}
		else if(e.getKeyChar() == 's'){
				try{
					Scanner in = new Scanner(System.in);
					System.out.println("Please enter the file name with out extensions");
					PrintWriter out = new PrintWriter ( new File(in.next()+".txt") );
					out.println(set.size());

					for(ArrayList<Point> i: set){
						for(Point j: i){
							out.print((j.getX()-275)+", ");
						}
						out.println("");
						for(Point j: i){
							out.print((j.getY()-275)+", ");
						}
						out.println("");
					}
					in.close();
					out.close();
					System.out.println("file was properly saved");
				}
				catch(IOException f){
					System.out.println("file not found");
				}

		}
		else if (e.getKeyChar() == 'a'){ // working here
		}
		else if (e.getKeyChar() == 'd' ){
							set.remove(current);

		}
		else{
			current++;
		}
		if(current>= set.size()){
			current = 0;
		}
	}

	public void keyTyped( KeyEvent e){}

	public void mouseMoved( MouseEvent e){
		mP[0] = e.getX()-Global.topX;
		mP[1] = e.getY()-Global.topY;
	}

	public void mouseDragged(MouseEvent e){
		mP[0] = e.getX()-Global.topX;
		mP[1] = e.getY()-Global.topY;
	}

	public void mouseWheelMoved(MouseWheelEvent e){}
 	public void	mouseEntered(MouseEvent e){}
 	public void mouseExited(MouseEvent e){}
 	public void mousePressed(MouseEvent e){MB = 1;}

 	public void mouseReleased(MouseEvent e){
		mP[0] = e.getX()-Global.topX;
		mP[1] = e.getY()-Global.topY;
		if (mP[1] < 500 && mP[0] < 500)
 			this.set.get(set.size()-1).add(new Point(mP[0],mP[1])); MB=0; }

	public void mouseClicked( MouseEvent e){

	}
	public static void main(String[] args){
		UnitMaker program = new UnitMaker();
	}

	public void paint(Graphics g){
		if (dbImage == null){
			System.out.println("image created");
			dbImage = createImage(WIDTH,HEIGHT);
			dbg = dbImage.getGraphics();
			dbg.setFont(dflt);
		}
		dbg.setColor (Color.black);
		dbg.fillRect (0, 0, WIDTH,HEIGHT);
		dbg.setColor(Color.gray);
		for(int i =0; i < 10; i++){
			dbg.drawLine( i*50, 0 , i*50 ,500) ;
			dbg.drawLine( 0 , i*50 , 500 , i*50);
		}
		dbg.drawRect( 0,0,10*50,10*50);
		dbg.setColor(Color.red);
		for(ArrayList<Point> i : set){
			for(Point j : i){
				dbg.fillOval( (int)(j.getX()-2),(int)(j.getY()-2),4,4 );
			}
		}
		if(set.size()>0){

			dbg.setColor(colorSet.get(colorSet.size()-1));
			for(int i = 0; i < set.size(); i++){
					if(set.get(i).size()>1)
						for(int j = 0; j < set.get(i).size(); j++){

								//dbg.drawString(j+"",(int)set.get(i).get(0).getX(),(int)set.get(i).get(0).getY());

							dbg.drawLine( (int)set.get(i).get(0).getX(),(int)set.get(i).get(0).getY(),(int)set.get(i).get(1).getX(),(int)set.get(i).get(1).getY());
							set.get(i).add(set.get(i).remove(0));
						}
			}
			if(set.get(set.size()-1).size() > 1){
				dbg.setColor(Color.green);
				dbg.fillOval( (int)(set.get(set.size()-1).get(current).getX()-2),(int)(set.get(set.size()-1).get(current).getY()-2),4,4 );
			}
		}
		dbg.setColor(Color.white);
		if(set.size()>0)
			for(int i = 0; i < set.get( set.size()-1 ).size(); i++){
				dbg.drawString(i+"-> ("+(int)(set.get(set.size()-1).get(i).getX()-275)+","+(int)(set.get(set.size()-1).get(i).getY()-275)+")",510,i*20+Global.topY);
			}
		dbg.setColor(Color.red);
		dbg.drawLine(5*50, 0, 5*50, 500);
		dbg.drawLine(0,5*50, 500, 5*50);
		dbg.drawString("("+(mP[0]-275)+","+(mP[1]-275)+","+MB+")",mP[0],mP[1]);
		gui.draw(dbg,this);
		g.drawImage(dbImage,Global.topX,Global.topY,this);
	}

}