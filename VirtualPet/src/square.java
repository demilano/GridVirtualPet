import java.awt.Image;

import javax.swing.*;


public class square {

	private int x;
	private int y;
	private ImageIcon backImage;
	private ImageIcon curImage;
	private ImageIcon defImage;
	private ImageIcon oImage = new ImageIcon("textures/ograss.png");
	private boolean obstructed;
	private String type;
	private boolean object;
	
	public square(int posx,int posy,String type){
		this.x = posx;
		this.y = posy;
		if(type.equals("flower")){
			obstructed = true;
			backImage = new ImageIcon("bin/textures/ograss.png"); // At the moment grass default, but will probably change depending on world generator
			object = true;
			curImage = new ImageIcon("bin/textures/ograss.png");
			defImage = curImage;
		} else { 
			obstructed = false;
			curImage = new ImageIcon("bin/textures/" + type + ".png"); 
			defImage = curImage;
			object = false;
			if(type.equals("grassTop")){
				backImage = new ImageIcon("bin/textures/grass.png");
			}
		}

	}
	
	public String type(){
		return type;
	}
	
	public void changeImg(){
		curImage = new ImageIcon("textures/grass.png");
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean obstructed(){
		return obstructed;
	}
	
	public void isObstructed(boolean obstruct){
		if(obstruct){
		//	curImage = oImage;
		} 
		this.obstructed = obstruct;
	}

	public Image getBackImage() {
		return backImage.getImage();
	}
	
	public void moveUp(){ y=y-1;}
	public void moveDown(){ y=y+1;}
	public void moveLeft(){ x=x-1;}
	public void moveRight(){ x=x+1;}

	public boolean isObject() {
		return object;
	}

	public Image getImage() {
		return curImage.getImage();
	}
	
	private boolean selected;
	
	public void select() {
		selected = true;
	}

	
}
