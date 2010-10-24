public class StarTroop extends Unit{
	
	public StarTroop(){ super(); }
	public StarTroop(double X, double Y){ super(X,Y); }
	public void initBody(){
 		double[][] Xcords = {
 		{-8.0, -27.0, 0.0, 3.0, 6.0, 27.0, 11.0, 25.0, 12.0, 2.0, -12.0, -25.0, -7.0}
 		,{-1.0, -7.0, -1.0, 7.0, 7.0}};
		double[][] Ycords = {
		{-2.0, 9.0, -3.0, 14.0, -4.0, 8.0, -4.0, -17.0, -25.0, -10.0, -26.0, -21.0, -8.0 }
		,{-11.0, -28.0, -40.0, -43.0, -31.0}
		
		};
		int[][] colours = {{255,255,0}, {255,150,150},{255,0,0},{255,0,0}};
		for(int i = 0; i < Xcords.length; i++){
			addBody( colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);

		}
		rotateBody(-Math.PI/2.0);
	}
	
	public void initTurret(){

		  		
	}
	
	public void initStats(){
		 hp=10; // hit points
		 ap=0; // attack power
		 range=0; // range of the unit's attack
		 vx=3; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=3; // velocity
		 heat = 3;
		 bulletV = 24;
		 bulletRad = 3;
		 rand = 25;
	}
	public void produce( double x, double y, int level){
		StarTroop tmp = new StarTroop(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tmp.upgradeLevel(level);
		tmp.moveOrder((Global.TILE_AMOUNT/2)*Global.MAX_SIZE-Global.MAX_SIZE/2,(Global.TILE_AMOUNT/2)*Global.MAX_SIZE-Global.MAX_SIZE/2);
		units.add( tmp );
	}
}