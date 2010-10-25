import java.util.*;
import java.awt.*;
import javax.swing.*;
/* creates and manages tower production and upgrades */
class Player{
	private int money = 500;
	private int MX,MY,MB, mapMX, mapMY, tx, ty, tb, mapTX, mapTY;
	private GUI gui;
	private boolean selecting; // is the player currently selecting anything
	private boolean alive = true; // is the player alive
	public Player( GUI gui ){
		this.gui = gui;
	}
	public void updateMouse(int[] mP, int b, int [] convertedMP){
		tb = MB; // previous mouse click
		MX = mP[0]; // new mouse position
		MY = mP[1]; 
		mapMX = convertedMP[0]; // where on the map was it clicked
		mapMY = convertedMP[1];
		MB = b; // new mouse click
	}
	public void addMoney( int n){
		money += n;
		if (money < 0){
			alive = false;
		}
	}
	public boolean getAlive(){return alive;}
	public void build(Cell[][] grid,HashMap<String,Tower> buildSet,String name,LinkedList<Tower>towers){
		// build a tower on the map based on specific conditions
		// the mouse has to be pressed and the tower has to be affordable
		if ( MB == 1 && tb == 0 && MX > 128 && gui.getVisible("main") && name != null && money >= Global.towerCost ){
			double locX = (int)(mapMX/Global.MAX_SIZE)*Global.MAX_SIZE;
			double locY = (int)(mapMY/Global.MAX_SIZE)*Global.MAX_SIZE;
			int tileX = mapMX/Global.MAX_SIZE;
			int tileY = mapMY/Global.MAX_SIZE;
			locX += Global.MAX_SIZE/2;
			locY += Global.MAX_SIZE/2;
			if(tileX >= 0 && tileX < Global.TILE_AMOUNT && tileY >= 0 && tileY < Global.TILE_AMOUNT){

				if (checkAroundCell(grid,tileX,tileY)){
					money -= Global.towerCost;
					buildSet.get(name).produce((double)locX,(double)locY);
				}
			}
		}
	}

	public boolean checkAroundCell(Cell[][] grid, int cellX, int cellY ){
		/* before a tower can be built, the area around the tower must be almost clear.
		 * Only one tower can be around the new tower*/
		int numberOfBlockedCells = 0;
		int[][] checks = {{0,0},{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,-1},{-1,1},{1,-1}};
		if(grid[cellX][cellY].getBlock() == true){
			return false;
		}
		for(int i = 0; i < checks.length;i++){
			int newCellX = cellX+checks[i][0];
			int newCellY = cellY+checks[i][1];
				if ( newCellX >= 0 && newCellX < Global.TILE_AMOUNT && newCellY >= 0 && newCellY < Global.TILE_AMOUNT ){
					if(grid[newCellX][newCellY].getBlock() == true || grid[newCellX][newCellY].getSize() != 0){
						numberOfBlockedCells++;
					}
				}
		}
		if(numberOfBlockedCells < 2){return true;}
		else{return false;}
	}

	LinkedList<Tower> selected = new LinkedList<Tower>();
	public void selection(LinkedList<Tower> choices){
		double [] tmp = { mapTX,mapTY,mapMX-mapTX,mapMY-mapTY }; // create a rectangle
		normalize(tmp); // fix the rectangle if it has negative width or height
		tmp[2] += tmp[0]; // change the width and height to coordintes
		tmp[3] += tmp[1];
		for(Tower i: choices){
			if(i.check_if_in_Box(tmp)){
				i.highlight();
				selected.add(i);
			}
		}
		if(selected.size() > 0){ // if some units are selected, bring up the upgrade menu
			gui.setVisible("upgrades",true);
			gui.setVisible("main",false);
			gui.deactivate("main");
		}
	}
	
	public void upgradeSelection(){ // upgrade the units based on what the user clicked in the "upgrade" menu
		if(selected.size() > 0 && MB == 1 && tb == 0 && gui.getVisible("upgrades") && money >= Global.upgradeCost*selected.size()){
			String name = gui.getActiveButtonName("upgrades"); // get the current active button
			if(name != null){
				if( name.equals("+10% attack") ){
					for( Unit i: selected){
						i.multiplyAP(1.1);
					}
				}
				else if( name.equals("+10% range") ){
					for( Unit i: selected){
						i.multiplyRange(1.1);
					}
				}
				else if( name.equals("+1 weapon") ){
					for(Unit i: selected ){
						i.nextLevel();
					}
				}
				else if( name.equals("+10% damage radius") ){
					for( Unit i: selected ){
						i.multiplyDamageRadius(1.1);
					}
				}
				money -= Global.upgradeCost*selected.size();
			}
		}
	}

	public void highlight(LinkedList<Tower>choices){
		if ( MB == 2){
			gui.reset();
		}
		if ( MB == 0 && selecting == false ){
			tx = MX;
			ty = MY;
			mapTX = mapMX;
			mapTY = mapMY;
		}
		else if(MB == 0 && selecting == true){
			this.selecting = false;
			selection(choices);
		}
		else if(MB == 1 && gui.getActiveButton("main") == null ){
			this.selecting = true;
			if(selected.size() != 0){
				for(Unit i: selected){
					i.dehighlight();
				}
				selected.clear();
				gui.setVisible("main",true);
				gui.setVisible("upgrades",false);
			}
		}
	}

	public void draw(Graphics g, JFrame canvas){
		g.setColor(Color.white);
		int[] box = {tx,ty,MX-tx,MY-ty};
		normalize(box);
		if (this.selecting == true){
			g.drawRect(box[0],box[1],box[2], box[3]);
		}
		g.setColor(Color.black);
		BattleMap.statList.add(" money: "+money);
		if(selected.size() != 0){
			double range = 0.0;
			double attack = 0.0;
			double level = 0.0;
			double upgradeCost = 0.0;
			for(Unit i : selected){
				range += i.getRange();
				attack += i.getAttack();
				level += i.getLevel();
			}
			range /= selected.size();
			attack /= selected.size();
			level /= selected.size();
			BattleMap.statList.add(" average level: "+(int)level ) ;
			BattleMap.statList.add(" average range: "+(int)range ) ;
			BattleMap.statList.add(" average attack: "+(int)attack ) ;
			BattleMap.statList.add(" upgrade cost: "+ Global.upgradeCost*selected.size() ) ;
		}
	}
	public void normalize(int[] box){
		if (box[2] < 0){
			box[0] += box[2];
			box[2]*=-1;
		}
		if (box[3] < 0){
		box[1] += box[3];
		box[3]*= -1;
		}
	}
	public void normalize(double[] box){
		if (box[2] < 0){
			box[0] += box[2];
			box[2]*=-1;
		}
		if (box[3] < 0){
		box[1] += box[3];
		box[3]*= -1;
		}
	}
	public void normalizeCords (double [] box){
		double temp;
		if (box[2] < box[0]){
			temp = box[0];
			box[0] = box[2];
			box[2] = temp;
		}
		if (box[3] < box[1]){
			temp = box[1];
			box[1] = box[3];
			box[3] = box[1];
		}
	}
}
