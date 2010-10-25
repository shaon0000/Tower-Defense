import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
public class Global{
	public static int TILE_AMOUNT = 40;
	public static int [][] map = new int[TILE_AMOUNT][TILE_AMOUNT];
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	public static int MAX_SIZE = 50;
	public static int topX = 4;
	public static int topY = 30;
	public static int statX = 0;
	public static int statY = HEIGHT-200;
	public static int statHeight = 200;
	public static int statWidth = 128;
	public static int upgradeCost = 10;
	public static int towerCost = 30;
	public static int randint(int a, int b){
		return (int)(Math.random()*(b-a+1)) + a;
	}
	public static int randint(double a, double b){
		return randint((int)a,(int)b);
	}
}
