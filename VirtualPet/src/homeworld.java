

import java.awt.*;
import java.awt.event.*;
import java.util.*;


import javax.swing.*;
import javax.swing.Timer;
import static org.junit.Assert.*;

import org.junit.Test;


public class homeworld extends JPanel implements KeyListener, ActionListener, MouseListener {

	private static final int totalRows = 42; //16 min
	private static final int totalCols = 33; // 23 min
	private static final int viewRows = 9; // How many rows are visible
	private static final int viewCols = 13; // How many columns are visible
	private static final int pxSize = 64;
	private static final int iniCCol = 6*pxSize; // determine where actor in middle should be
	private static final int iniCRow =4*pxSize; 
	
	// The world generator
	private worldGenerator generator;
	private Image back;
	private Timer evTimer;
	public square themap[][]; // = new square[totalCols][totalRows]; // col by row
	private int LMV; // col by row, left most visible 
	private int TMV; // So that not everything is drawn, keeps track of what's visible (actually one outside visible when static, for drawing smoothly when moving
	private int BMV; // bottom most visible, and right most visible etc
	private int RMV;
	private actor Gako; // The main event, like no king was before
	private int curR; // Where actor is currently, grid-wise
	private int curC;
	private boolean moving; // If in motion, certain things have to be calculated
	private Queue<String> actions; // Action queue for smooth movement (A work-around a key being held down, pausing, then repeating rapidly)
	private pet Pet1;
	private pet Pet2;
	private Set<pet> pets;
	
	public homeworld(){
		LRcent = true; // actor is horizontally centered (so camera moves)
		UDcent = true; // vertically
		moving = false; // Not moving
		actions = new LinkedList<String>();
		addKeyListener(this);
		addMouseListener(this);
		//setBackground(Color.getHSBColor((float)Math.random(),1,1));
		setBackground(Color.getHSBColor(0,0,0));
		setFocusable(true);
		Gako = new actor(pxSize); // The main actor, name can be rechosen later
		Gako.setFace("down");
		evTimer = new Timer(8,this); // The main refresh timer. Determines speed of actor... may not be the most efficient
		evTimer.start();
		LMV=0; // The boundaries of the viewing space (+1, because when you move, it becomes visible) Note this!! Also relates to array numbering
		TMV=0;
		BMV=viewRows+1; // The +1 so that the squares just off screen and drawn preemptively
		RMV=viewCols+1; 
		curC = (iniCCol/pxSize)+1; // Current position (of the main actor. Noted in this class for ease of access)
		curR = (iniCRow/pxSize)+1;
		facC = curC;
		facR = curR+1;
		generator = new worldGenerator(totalRows,totalCols,pxSize);
		themap = generator.getMap();
		
		pets = new HashSet();
		//pets.add(new pet(4,5,themap[4][5].getX(),themap[4][5].getY(),themap));
		//pets.add(new pet(12,8,themap[12][8].getX(),themap[12][8].getY(),themap));
		
		//themap[curC][curR].isObstructed(true);

		
		//for(pet p : pets){
		//	p.startMove();
		//}
		/*
		ArrayList<Point> trial = Pet1.happyPath(15, 12);
		for(Point p : trial){
			themap[(int)p.getX()][(int)p.getY()].isObstructed(true);
		}
	*/
		themap[3][4] = new square(2*pxSize,3*pxSize,"flower");
		themap[4][3] = new square(3*pxSize,2*pxSize,"flower");
		themap[4][4] = new square(3*pxSize,3*pxSize,"flower");
		//themap[4][5] = new square(3*pxSize,4*pxSize,"flower");
		themap[5][3] = new square(4*pxSize,2*pxSize,"flower");
		themap[5][4] = new square(4*pxSize,3*pxSize,"flower");
		themap[5][5] = new square(4*pxSize,4*pxSize,"flower");
		themap[5][6] = new square(4*pxSize,5*pxSize,"flower");
		themap[6][3] = new square(5*pxSize,2*pxSize,"flower");
		themap[6][4] = new square(5*pxSize,3*pxSize,"flower");
		themap[6][5] = new square(5*pxSize,4*pxSize,"flower");
		themap[6][6] = new square(5*pxSize,5*pxSize,"flower");
		themap[6][7] = new square(5*pxSize,6*pxSize,"flower");
		themap[7][3] = new square(6*pxSize,2*pxSize,"flower");
		themap[7][4] = new square(6*pxSize,3*pxSize,"flower");
		themap[7][5] = new square(6*pxSize,4*pxSize,"flower");
		themap[8][4] = new square(7*pxSize,3*pxSize,"flower");
		
		//themap[11][7] = new square(10*pxSize,6*pxSize,"flower");
		themap[11][8] = new square(10*pxSize,7*pxSize,"flower");
		themap[11][9] = new square(10*pxSize,8*pxSize,"flower");
		themap[12][7] = new square(11*pxSize,6*pxSize,"flower");
		themap[12][8] = new square(11*pxSize,7*pxSize,"flower");
		// Test for pathing
		
		themap[1][1] = new square(0,0,"grassTop");
		themap[2][1] = new square(1*pxSize,0,"grassTop");
		themap[3][1] = new square(2*pxSize,0,"grassTop");
		themap[4][1] = new square(3*pxSize,0,"grassTop");
		themap[5][1] = new square(4*pxSize,0,"grassTop");
		themap[6][1] = new square(5*pxSize,0,"grassTop");
		themap[7][1] = new square(6*pxSize,0,"grassTop");
		themap[8][1] = new square(7*pxSize,0,"grassTop");
		themap[9][1] = new square(8*pxSize,0,"grassTop");
		themap[10][1] = new square(9*pxSize,0,"grassTop");
		themap[11][1] = new square(10*pxSize,0,"grassTop");
		themap[12][1] = new square(11*pxSize,0,"grassTop");
		
		themap[1][2] = new square(0,pxSize,"dirtTop");
		themap[2][2] = new square(1*pxSize,pxSize,"dirtTop");
		themap[3][2] = new square(2*pxSize,pxSize,"dirtTop");
		themap[4][2] = new square(3*pxSize,pxSize,"dirtTop");
		themap[5][2] = new square(4*pxSize,pxSize,"dirtTop");
		themap[6][2] = new square(5*pxSize,pxSize,"dirtTop");
		themap[7][2] = new square(6*pxSize,pxSize,"dirtTop");
		themap[8][2] = new square(7*pxSize,pxSize,"dirtTop");
		themap[9][2] = new square(8*pxSize,pxSize,"dirtTop");
		themap[10][2] = new square(9*pxSize,pxSize,"dirtTop");
		themap[11][2] = new square(10*pxSize,pxSize,"dirtTop");
		themap[12][2] = new square(11*pxSize,pxSize,"dirtTop");
		
		themap[1][3] = new square(0,2*pxSize,"dirt");
		themap[2][3] = new square(1*pxSize,2*pxSize,"dirt");
		themap[3][3] = new square(2*pxSize,2*pxSize,"dirt");
		themap[4][3] = new square(3*pxSize,2*pxSize,"dirt");
		themap[5][3] = new square(4*pxSize,2*pxSize,"dirt");
		themap[6][3] = new square(5*pxSize,2*pxSize,"dirt");
		themap[7][3] = new square(6*pxSize,2*pxSize,"dirt");
		themap[8][3] = new square(7*pxSize,2*pxSize,"dirt");
		themap[9][3] = new square(8*pxSize,2*pxSize,"dirt");
		themap[10][3] = new square(9*pxSize,2*pxSize,"dirt");
		themap[11][3] = new square(10*pxSize,2*pxSize,"dirt");
		themap[12][3] = new square(11*pxSize,2*pxSize,"dirt");

		
		
		
		Pet1 = new pet(2,4,themap[2][4].getX(),themap[2][4].getY(),themap);
		pets.add(Pet1);
	}
	
	/** Painting the World
	 *  At the moment this is called by the evtimer...
	 *  Will probably have to look into more efficicent ways of doing this
	 */
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		//Draw array squares
		for(int c=LMV;c<=RMV;c++){ // Left most to and including rightmost
			for(int r=TMV;r<=BMV;r++){
				g2d.drawImage(themap[c][r].getImage(),themap[c][r].getX(),themap[c][r].getY(),pxSize,pxSize,null);
				if(themap[c][r].isObject()){
					g2d.drawImage(themap[c][r].getBackImage(),themap[c][r].getX(),themap[c][r].getY(),pxSize,pxSize,null);
					g2d.drawImage(themap[c][r].getImage(),themap[c][r].getX(),themap[c][r].getY(),pxSize,pxSize,null);
					
				}
			}
		}
		//Draw Grid
	
		for(int c=0;c<=pxSize*viewCols;c=c+pxSize){
			for(int r=0;r<=pxSize*viewRows;r=r+pxSize){
				g2d.drawRect(c,r,pxSize,pxSize);
				g2d.drawString(c/pxSize+","+r/pxSize, c-pxSize, r);
			}
		}

		g2d.drawOval(themap[facC][facR].getX(),themap[facC][facR].getY(),pxSize,pxSize); // Where actor is facing
		
		// Draw Actor
		g2d.drawImage(Gako.getImage(),Gako.getX(),Gako.getY(),pxSize,pxSize,null);
		Pet1.draw(g);
	}
	
	/** Moves actor. More specifically, changes actors position on screen if necessary 
	 * 'Invalid' checks if it has reached a boundary. At the moment, this is merely 
	 * the end of the map - total rows and cols
	 * If valid, moves the entire map (but for efficiency only what is displayed is redrawn)
	 * 
	 */
	public void shift(String dir){
		themap[facC][facR].isObstructed(true); // If it got there in the first place, it couldn't have been obstructed
		if(dir.equals("left") && invalid("left")){
			Gako.moveLeft();
		} else if (dir.equals("right") && invalid("right")){
			Gako.moveRight();
		} else if (dir.equals("up") && invalid("up")){
			Gako.moveUp();
		} else if (dir.equals("down") && invalid("down")){
			Gako.moveDown();;
		} else {
			for(pet p : pets){ p.moveOp(dir); }
			//Pet1.moveOp(dir);
		// Situation where you can move anywhere, no boundaries
		for(int c=0;c<totalCols;c++){
			for(int r=0;r<totalRows;r++){
				if(dir.equals("up")){ themap[c][r].moveDown();}
				else if (dir.equals("down")){ themap[c][r].moveUp();}
				else if (dir.equals("left")){ themap[c][r].moveRight();}
				else if (dir.equals("right")){ themap[c][r].moveLeft();}
			}
		}
		}
		themap[curC][curR].isObstructed(false);
	}
	
	/** Checks whether or not it is necessary 
	 * to reposition the actor, or the world around the actor
	 * depending if it has reached the bounds of the world 
	 */
	public boolean invalid(String dir){
		if(dir.equals("left")){
			if (LMV==0) { return true; }
			if(LMV-1<0){ return true; }
			if(Gako.getX()>iniCCol){ LRcent = false; return true; }
		} else if(dir.equals("right")){
			if (RMV == themap.length-1) { return true; }
			if(RMV+1>=totalCols){ return true; }
			if(Gako.getX()<iniCCol){ LRcent = false; return true; }
		} else if(dir.equals("up")){ 
			if (TMV==0) { return true; }
			if(TMV-1<0){ return true; }
			if(Gako.getY()>iniCRow){ UDcent = false; return true; }
		} else if(dir.equals("down")){
			if (BMV == themap[0].length-1) { return true; }
			if(BMV+1>=totalRows){ return true; }
			if(Gako.getY()<iniCRow){ UDcent = false; return true; }
		} // Please make sure correct
		return false;
	}


	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_DOWN){
			doMove("down");
		} else if (e.getKeyCode() == e.VK_UP){
			doMove("up");
		}
		else if (e.getKeyCode() == e.VK_LEFT){
			doMove("left");
		} else if (e.getKeyCode() == e.VK_RIGHT){
			doMove("right");
	}
	}
	
	/** Simple method of moving while
	 * allowing for the keyboard press speed etc. 
	 * If not already moving, queues up 2 squares of moving. 
	 * Queue is immediately popped and processed
	 * 			If key is released and no change has been made to direction (therefore queue won't be empty)
	 * 			then queue is cleared. This will stop the movement in that direction.
	 * 
	 */
	public void doMove(String dir){
		if(!moving){
			actions.add(dir);
			actions.add(dir);
		} else { // if it is moving
			actions.clear();
			actions.add(dir);
			actions.add(dir);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == e.VK_DOWN){
			if(!actions.isEmpty() && actions.peek().equals("down")){
				actions.clear();
			}
		} else if (e.getKeyCode() == e.VK_UP){
			if(!actions.isEmpty() && actions.peek().equals("up")){
				actions.clear();
			}
		}
		else if (e.getKeyCode() == e.VK_LEFT){
			if(!actions.isEmpty() && actions.peek().equals("left")){
				actions.clear();
			}
		} else if (e.getKeyCode() == e.VK_RIGHT){
			if(!actions.isEmpty() && actions.peek().equals("right")){
				actions.clear();
			}
	}
	}

	public void keyTyped(KeyEvent e) {		
	}

	int n; // Counts to pxSize, then resets to 0
	int counter = 0; // Counts to 100 (or so), on each tick it checks through to do stuff, like start a new event
	int m; // Constant timer. 

	public void actionPerformed(ActionEvent e) {

		m++;
		if(m==2000){
			m=300;
		}
		if(m%40==0 && m>300){ Pet1.setNextImg(); }
	
		/** The movement process
		 *  All the following (on the main timer refresh)
		 */
		if(!actions.isEmpty()){ // If there is an action queued
			if(moving){
				if(n==pxSize-1){ // If end of last destination (moves 1 pixel at a time, so when count is at pixel size it's at the end)
					newPos(Gako.direction()); // Repositions actor
					String action = actions.peek();
					Gako.setFace(action); // Sets new facing of actor
					newFace(action); // Changes the marker as to where the actor is facing (the oval)
					n=0; // Reset counter
					if(!themap[facC][facR].obstructed()){
						shift(action); // send in new direction if not obstructed
					} else {
					//Obstruction!
					Gako.nextImg(8); // Changes to default still image (image/8 = filename 1)
					moving = false;
					}
				} else { // if not at end of destination, keep going
					shift(Gako.direction());
					n++;
					if(n%8==0){ Gako.nextImg(n);} // NOT THE BEST ANIMATOR. Image changes at every multiple of 8, based on n
					
				}
			} else { // If not moving (which at the moment means there should be 2 direction commands queued
				String action = actions.poll();
				if(!Gako.isFacing(action)){
				Gako.setFace(action);
				Gako.nextImg(8);
				}
				newFace(action);
				n = 0;
				if(!themap[facC][facR].obstructed()){
				shift(action);
				moving = true;				
				}
			}
		} else { // If no actions are queued (When the key is released and the actions are cleared
			if(moving){ // If already moving, keep moving
				if(n==pxSize-1){ // If at end of dest.
					newPos(Gako.direction());
					moving = false;
					Gako.nextImg(8);
					n=0;
				} else {
					shift(Gako.direction());
					n++;
					if(n%8==0){ Gako.nextImg(n);}				
				}
			}
		}		
		/** Eventus! Like, checking if a critter wants to move,
		 * or wants to be hungry (based on internal probabilities)
		 */
		if(counter==500){
			// At the moment we'll just make it move every 100
			// Remember that when it moves, the first thing that must happen
			// Is that tile becomes obstructed (so you don't have player obstruction)		
			//Pet1.moveRand(themap);
			
			// If path queued but not moving, start moving
			//if(!Pet1.isMoving()){
			//Pet1.startMove();
			//}
			for(pet p : pets){ 
				if(!p.isMoving()){
					p.startMove();
				}
			}
			
			counter=0;
		}	
		counter++;
		repaint();
	}
	
	private int facR;
	private int facC;
	private boolean UDcent;
	private boolean LRcent;
	
	/** Changes to new position in the array, including recalculating
	 * the viewing area and what should be repainted 
	 */
	private void newPos(String dir){
		if(dir.equals("down")){
			curR = curR+1; // Changes current row
			if(Gako.getY()==iniCRow){ // If centered after move...
				if(UDcent){ // If previously centered, then change what's drawn
					TMV++;
					BMV++;
				} else { UDcent = true;} // Else, not say it's centered
			} else { UDcent = false;}
			System.out.println(TMV + " " + LMV);
		} else if (dir.equals("up")){
			curR = curR-1;
			if(Gako.getY()==iniCRow){
				if(UDcent){
					TMV--;
					BMV--;
				} else { UDcent = true;}
			} else { UDcent = false;}	
			System.out.println(TMV + " " + LMV);		
		} else if (dir.equals("left")){
			curC = curC-1;
			if(Gako.getX()==iniCCol){
				if(LRcent){
					LMV--;
					RMV--;
				} else { LRcent = true;}
			} else { LRcent = false;}
			System.out.println(TMV + " " + LMV);
		} else if (dir.equals("right")){
			curC = curC+1;	
			if(Gako.getX()==iniCCol){
				if(LRcent){
					LMV++;
					RMV++;
				} else { LRcent = true; }
			} else { LRcent = false;}
			System.out.println(TMV + " " + LMV);
		}
		newFace(dir);
	}
	
	private void newFace(String dir){
		if(dir.equals("down")){
			facR = curR+1;
			facC = curC;
		} else if (dir.equals("up")){
			facR = curR-1;
			facC = curC;
		} else if (dir.equals("left")){
			facC = curC-1;
			facR = curR;
		} else if (dir.equals("right")){
			facC = curC+1;
			facR = curR;
		}
	}
	
	public int getCurRow(){
		return curR;
	}
	
	public int getCurCol(){
		return curC;
	}


	public boolean isOb(int x, int y) {
		return themap[x][y].obstructed();
	}


	boolean hasSelection = false;
	int newPX;
	int newPY;
	
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		System.out.println(((e.getX()/pxSize)+1) + " " + ((e.getY()/pxSize)+1));
		int sqX = (e.getX()/pxSize)+1;
		int sqY = (e.getY()/pxSize)+1;
		/*
		if(!hasSelection){
			themap[sqX][sqY].select();
			newPX = sqX;
			newPY = sqY;
			hasSelection = true;
		} else {
			Pet1 = new pet(newPX,newPY,themap[newPX][newPY].getX(),themap[newPX][newPY].getY(),themap);
			Pet1.generatePath(newPX,newPY,sqX,sqY);
			Pet1.startMove();
			hasSelection = false;
		}
		*/
		Pet1.generatePath(Pet1.getCurX(),Pet1.getCurY(),sqX,sqY);
		Pet1.startMove();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	}

