import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import javax.swing.ImageIcon;


public class pet implements ActionListener {

	private int posX; // Positions on screen (may be able to calculate
	private int posY; // Based on curX,Y and where that square is
	private int curX; // Position on grid 
	private int curY;
	private Point curPos;
	private String type; // What type it is
	private String facing; //up, down, left, right
	private ImageIcon curImage;
	private boolean isMoving;
	private Queue<String> path;
	Timer movTimer;
	square[][] themap;
	Point destination;
	private boolean inTransit;
	
	private Color[][] img = new Color[32][32];
	private Color[][] img2 = new Color[32][32];
	private Color[][] img3 = new Color[32][32];
	private Color[][] img4 = new Color[32][32];
	
	
	public pet(int curX, int curY, int posX, int posY, square[][] themap){
		this.posX = posX;
		this.posY = posY;
		movTimer = new Timer(8,this);
		this.curX = curX;
		this.curY = curY;
		this.curPos = new Point(curX,curY);
		curImage = new ImageIcon("actors/chao.png");
		this.themap = themap;
		themap[curX][curY].isObstructed(true);
		inTransit = false;
		path = new LinkedList<String>();
		
		this.img = loadImage(this.img,"bin/actors/milchcow");
		this.img = loadImage(this.img2,"bin/actors/milchheadturn");
		this.img = loadImage(this.img3,"bin/actors/milchcow");
		this.img = loadImage(this.img4,"bin/actors/milchcowheadturn2");
		curImg = this.img;
	}
	
	public void generatePath(int startX,int startY,int dx,int dy){
		pathFinder pFind = new pathFinder(themap);
		path = pFind.getPath(curPos,new Point(dx,dy));
	}

	public void startMove(){
		if(!path.isEmpty()){
		movTimer.start();
		}
	}
	
	public Color[][] loadImage(Color[][] image, String name){
		try{
			Scanner scan = new Scanner(new File(name));
			for(int row=0;row<image.length;row++){
                for(int col=0;col<image[0].length;col++){
                    if(!scan.hasNextInt()){
                        scan.next();
                        image[row][col] = null;
                    } else {
                        int r = scan.nextInt();
                        int g = scan.nextInt();
                        int b = scan.nextInt();
                        image[row][col] = new Color(r,g,b);
                        if(r==0&&g==0&&b==0){
                        	//img[row][col] = new Color(0,0,0,60);
                        }
                        
                    }
                }
            }
            scan.close();
		} catch(IOException e) {}
		System.out.println("success");

		return img;
	}
	
	public void setNextImg(){
		if(curImg==img){ curImg = img2; 
		} else if (curImg==img2){ curImg = img3; 
		} else if (curImg==img3){ curImg = img4;
		} else { curImg = img; }
	}
	
	Color[][] curImg;
	

	public void draw(Graphics g){
		int pxSize = 2;
        for(int row=0;row<curImg.length;row++){
            for(int col=0;col<curImg[0].length;col++){
                if(curImg[row][col]!=null){
                    g.setColor(curImg[row][col]);
                    g.fillRect((col*pxSize)+posX,(row*pxSize)+posY,(pxSize+1),(pxSize+1)); 
                  //  this.stroke(row,col,g);

                } 
            }
        }
     //   System.out.println("Drawing");
	}
	
	  public void stroke(int r,int c,Graphics g){
		  int pxSize = 2;
	        int cPos = (c*pxSize)+posX;
	        int rPos = (r*pxSize)+posY;
	        int topLX = r*pxSize + posY; // Corners for stroke
	        int topRX = r*pxSize + posY + pxSize - 2; 
	        int botLX = c*pxSize + posY - 2; 
	        int botRX = c*pxSize + posY + pxSize - 2; 
	        int sWid = 3;
	        if(img[r][c]!=null){ // If square is not blank
	            g.setColor(Color.black);
	            if(c==0){ // If on edges
	                g.fillRect(cPos,rPos,sWid,pxSize+1);   
	            }
	            if(r==0){
	                g.fillRect(cPos,rPos,pxSize+1,sWid);            
	            }
	            if(c==img.length-1){
	                g.fillRect(cPos+pxSize-2,rPos,sWid,pxSize+1);
	            }
	            if(r==img[0].length-1){
	                g.fillRect(cPos,rPos+pxSize-2,pxSize+1,sWid);
	            }
	            // Now the rest

	            if(r!=0 && img[r-1][c] == null){ // If it isn't the first row, and joins to nothing
	                g.fillRect(cPos,rPos,pxSize,sWid);
	            }
	            if(r!=img.length-1 && img[r+1][c] == null){
	                g.fillRect(cPos,rPos+pxSize-2,pxSize+1,sWid);
	            }
	            if(c!=0 && img[r][c-1] == null){
	                g.fillRect(cPos,rPos,sWid,pxSize); 
	            }
	            if(c!=img[0].length-1 && img[r][c+1] == null){
	                g.fillRect(cPos+pxSize-2,rPos,sWid,pxSize+1); 
	            }

	            // And the corners, c is left and right, r is up and down
	            if(r != 0){ // If not the first row
	                if(c !=0){ // !first col
	                    if(img[r-1][c-1] == null && img[r-1][c] != null && img[r][c-1] != null){ // Left of and above (diag
	                        g.fillRect(cPos,rPos,sWid,sWid);
	                    }
	                }
	                if(c !=img[0].length-1){ //Above final col
	                    if(img[r-1][c+1] == null && img[r-1][c] != null && img[r][c+1] != null){ 
	                        g.fillRect(cPos+pxSize-2,rPos,sWid,sWid);
	                    }
	                }
	            }   
	            if(r != img.length-1){ // Not Bottom row
	                if(c !=0){ //Not first col
	                    if(img[r+1][c-1] == null && img[r][c-1] != null && img[r+1][c] != null){ // left and below
	                        g.fillRect(cPos,rPos+pxSize-2,sWid,sWid);
	                    }
	                }
	                if( c !=img[0].length-1){
	                    if(img[r+1][c+1] == null && img[r][c+1] != null && img[r+1][c] != null){ 
	                        g.fillRect(cPos+pxSize-2,rPos+pxSize-2,sWid,sWid);
	                    }
	                }
	            }
	        }
	    }
	
	int n; // Counts to pxSize, then restarts
	
	public void actionPerformed(ActionEvent e) {
		themap[curX][curY].isObstructed(true); // Ensure it is always obstructed, despite if actor makes it onto it
		if(!path.isEmpty()){ // If places to go
			if(inTransit==false && !checkFace(path.peek(),curPos)){ // If not moving, but can
			//	System.out.println("Not moving, but can");
				move(path.peek());
				newPos(path.peek());
				inTransit = true;
			} 
			if(inTransit){ // If moving
				if(n==63){ // At end of dest
				//	System.out.println("End of destination");
					path.poll();
					n=0;
					if(!path.isEmpty() && !checkFace(path.peek(),curPos)){
						move(path.peek());
						newPos(path.peek());
					} else {
						inTransit = false;
						movTimer.stop();
					}
				} else {
					n++;
					move(path.peek());
				}
			}
		}
	}
	
	/** Changes position of the pet, depending on the direction it is facing
	 * Also makes the square it was on not obstructed, and the square it goes to obstructed
	 * 
	 * @param dir Direction that pet is moving to
	 */
	public void newPos(String dir){
		themap[curX][curY].isObstructed(false);
		if(dir.equals("up")){ curY=curY-1; }
		else if (dir.equals("right")){ curX=curX+1; }
		else if (dir.equals("down")){ curY=curY+1; }
		else if (dir.equals("left")){ curX=curX-1; }
		curPos = new Point(curX,curY);
		themap[curX][curY].isObstructed(true);
	}
	
	/** Checks if the square the pet is facing is obstructed
	 * 
	 * @param dir Direction that the pet is facing
	 * @param curPoint The current position (whether mental or physical)
	 * @return False if obstructed, true if not obstructed
	 */
	public boolean checkFace(String face, Point curPoint){ 
		int x = (int)curPoint.getX();
		int y = (int)curPoint.getY();
		if(face.equals("up")){ return themap[x][y-1].obstructed(); }
		else if(face.equals("down")){ return themap[x][y+1].obstructed(); }
		else if(face.equals("right")){ return themap[x+1][y].obstructed(); }
		else { return themap[x-1][y].obstructed(); }
	}
	
	/** Moves the pet on screen
	 * 
	 * @param dir Direction it moves
	 */
	public void move(String dir) {
		if(dir.equals("up")){ posY=posY-1; } 
		else if (dir.equals("right")){ posX=posX+1; } 
		else if (dir.equals("down")){ posY=posY+1; } 
		else if (dir.equals("left")){ posX=posX-1; }
	}
	
	/** Moves in opposite direction of dir
	 * Used for when actor moves away from pet
	 * 
	 * @param dir Direction that actor moves
	 */
	public void moveOp(String dir) { 
		if(dir.equals("up")){ move("down");	} 
		else if(dir.equals("down")){ move("up"); }
		else if(dir.equals("left")){ move("right"); }
		else if(dir.equals("right")){ move("left"); }
	}
	
	public void setDestination(Point dest){ destination = dest; }
	public Image getImage(){ return curImage.getImage(); }
	public boolean isMoving(){ return inTransit; } // If path is empty, it's not moving anywhere (nowhere to go)
	public int getCurX(){	return curX;}
	public int getCurY(){	return curY;}
	public int getPosX(){	return posX;}
	public int getPosY(){	return posY;}
	public void setX(int X){ posX = X; }
	public void setY(int Y){ posY = Y; }
}
