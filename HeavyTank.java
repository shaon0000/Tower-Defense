public class HeavyTank extends Unit{	
	public HeavyTank(){
		super();
	}
	public HeavyTank(double X, double Y){
		super(X,Y);
	}
	public void initBody(){
 		double[][] Xcords = {{-1.0, -9.0, -25.0, -35.0, -19.0, -22.0, -14.0, -24.0, -28.0, -4.0, -31.0, -30.0, -15.0, -30.0, -26.0, -10.0, -24.0, -17.0, -14.0, -17.0, -12.0, -8.0, -13.0, -13.0, 0.0, -7.0, -5.0, -1.0, 3.0, 7.0, -4.0, 14.0, 11.0 }
 		,{-2.0, 13.0, 25.0, 37.0, 31.0, 29.0, 42.0 ,23.0 ,16.0, 14.0, 9.0}};
		double[][] Ycords = {{-24.0, -7.0, 1.0, 42.0, 118.0, 93.0, 94.0, 86.0, 65.0, 76.0, 57.0, 44.0, 55.0, 35.0, 21.0, 52.0, 13.0, 4.0, 14.0, 21.0, 17.0, 17.0, 8.0, 0.0, 8.0, 24.0, 30.0, 21.0,10.0, 21.0, 39.0, 22.0, 4.0 }
		,{-5.0, -3.0, -10.0, -43.0, -12.0, -5.0 ,10.0 ,-1.0, -1.0, 12.0, 2.0}};
		int[][] colours = {{255,255,0}, {255,150,150},{255,0,0},{255,0,0}};
		for(int i = 0; i < Xcords.length; i++){
			addBody( colours[i][0],colours[i][1],colours[i][2], Xcords[i],Ycords[i]);
		}
		rotateBody(-Math.PI/2.0);
	}
	public void initTurret(){}
	public void initStats(){
		 hp=10; // hit points
		 ap=0; // attack power
		 range=0; // range of the unit's attack
		 vx=1; // velocity in the x direction
		 vy=0; // velocity in the y direction
		 v=1; // velocity
		 heat = 3;
		 bulletV = 24;
		 bulletRad = 3;
		 rand = 25;
	}
	public void produce( double x, double y, int level){
		HeavyTank tmp = new HeavyTank(x,y);
		tmp.init(units,tower,grid,weapons,effects);
		tmp.upgradeLevel(level);
		tmp.moveOrder((Global.TILE_AMOUNT/2)*Global.MAX_SIZE-Global.MAX_SIZE/2,(Global.TILE_AMOUNT/2)*Global.MAX_SIZE-Global.MAX_SIZE/2);
		units.add( tmp );
	}
}
