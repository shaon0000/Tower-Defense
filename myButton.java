import java.awt.*;
import javax.swing.*;
public class myButton{
	
	private int[] pos;
	private Image pressImg;
	private Image button;
	private String statement;
	private boolean pressed;
	private int width;
	private int height;
	private String name;
	private boolean rollOver = false;
	private boolean highlight;
	private boolean onImage;
	public myButton(String filename, int[] pos, Image pressImg, String statement, String name, int width, int height, boolean onImage){
		this.pos = pos;
		button = Toolkit.getDefaultToolkit ().createImage (filename);

		this.width = width;
		this.height = height;
		this.pressImg = pressImg;
		this.onImage = onImage;
		this.statement = statement;
		this.name = name;
	}
	public boolean auto = false;
	
	public String getName(){
		return name;
	}
	
	public void draw(Graphics g, JFrame canvas){

		g.drawImage(button,pos[0],pos[1],canvas);
		if (highlight == true){

			g.drawImage(pressImg,pos[0],pos[1],canvas);
		}
		if(onImage)
			g.drawString(name,pos[0],pos[1]+height-3);
	}
	
	public boolean isPressed(){
		return pressed;
	}
	public void unpress(){
		pressed = false;
	}
	
	public void deHighlight() {
		highlight = false;
	}
	
	public void deselect() {
		pressed = false;
		highlight = false;
	}
	
	public void select() {
		pressed = true;
		highlight = true;
	}
	
}