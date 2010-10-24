import java.util.*;
import java.awt.*;
import javax.swing.*;

/* holds a list of menus which can be manipulated */

public class GUI{

	private HashMap<String,MyMenu> menus = new HashMap<String,MyMenu>();
	private LinkedList<MyMenu> menuSet = new LinkedList<MyMenu>();
	private Graphics g;
	private JFrame c;
	private int MB = 0;
	private int TB = 0;

	public GUI(Graphics g, JFrame c){
		this.g = g;
		this.c = c;
	}

	public void autoDeclick(String menuName,String buttonName, boolean auto ){
		menus.get ( menuName ) .autoDeclick ( buttonName,auto ) ;
	}
	public void addMenu( String name,int[] pos, int c, int r, int w, int h, String pressed ){
		menus.put(name, new MyMenu(name, pos, c, r, w, h, pressed) );
		menuSet.add( menus.get(name) );
	}

	public void addMenu( String name,int[] pos, int c, int r){
		menus.put(name, new MyMenu(name, pos, c, r, 116, 16, "graphics/buttons/pressed.png") );
		menuSet.add( menus.get(name) );
	}
	public void add(String theMenu, String name, String filename, String statement,boolean onImage){
		menus.get(theMenu).add(name,filename,statement,onImage);
	}
	public void add(String theMenu, String name, String statement){
		menus.get(theMenu).add(name,"graphics/buttons/standard.png",statement,true);
	}
	public boolean getVisible( String menuName ){
		return menus.get(menuName).getVisible();
	}

	public void setVisible( String menuName, boolean n ){
		menus.get( menuName ).setVisible( n );
	}

	public void check(int MX, int MY, int MB){

			for ( MyMenu i: menuSet ){

				if(i.getVisible()){
					i.check(MX,MY,MB);

				}
			}

	}

	public  myButton getActiveButton(String menuName ){
		return menus.get(menuName).getActiveButton();
	}

	public void deactivate( String menuName ){
		menus.get(menuName).depressActiveButton();
	}

	public String getActiveButtonName( String menuName ){
		myButton act = menus.get(menuName).getActiveButton();
		String emp = null;
		if (act == null){
			return emp;
		}
		else{
			return act.getName();
		}
	}

	public void reset(){
		for ( MyMenu i: menuSet ){
			i.depressActiveButton();
		}
	}

	public void draw(Graphics g,JFrame canvas){
		for(MyMenu i: menuSet){
			if(i.getVisible())
				i.draw(g,canvas);
		}
	}

}