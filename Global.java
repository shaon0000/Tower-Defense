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
	/*
	public static void main(String[]args) throws IOException{
		PrintWriter out = new PrintWriter( new File("out.txt") );
		Path path = new Path();
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				System.out.println("( "+x+", "+y+" ): "+(int)(180*Math.atan2(y,x)/3.14));
			}
		}
		while(path.done() == false){
			//System.out.println("Still attaining path");
			path.execute();

		}
		LinkedList <Point> set = path.getPath();
		System.out.println(set.size());
		int count = 1;
		while (set.size() != 0){
			Point temp = set.pop();
			map[(int)(temp.getX())][(int)(temp.getY())] = count;
			count++;
		}
		for(int i = 0; i < TILE_AMOUNT; i++){
			for(int j = 0; j < TILE_AMOUNT; j++){
				if(map[i][j] != 0){
					out.print(map[i][j]);
				}
				else{
					out.print(".");
				}
			}
			out.println("");
		}
		out.close();
	}
*/
}