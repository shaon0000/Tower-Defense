import java.util.*;
import javax.swing.*;
import java.awt.*;

public class TitleShine extends Effect{
	private int life, counter, X, Y, C;
	private String title;
	private int shifter = 10;
	private Font titleFont = new Font("Calibri",Font.BOLD,70);
	private Font regular = new Font("Arial",Font.BOLD,12);	
	public TitleShine( int life, String title ){
		super(0,0,1,1);
		this.life = life;
		this.title = title;
	}
	public void scale(){}
	public void work(){
		counter ++;
		C += shifter;
		if( C > 255 || C < 0){
			shifter *= -1;
			C += shifter;
		}
		if (counter == life){
			kill();
		}
	}
	public void draw(Graphics g, JFrame c){
		g.setColor(new Color(C,0,0));
		for(int i = 0; i < 20; i++){
			Y = Global.randint(0,200);
			g.drawLine( 0,Y,Global.WIDTH,Y );
		}
		g.setFont( titleFont );
		g.setColor(new Color(C,C,C));
		g.drawString(title,Global.WIDTH/2 - 50, 170);
		g.setFont( regular );
	}
}
