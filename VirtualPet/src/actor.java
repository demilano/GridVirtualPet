import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class actor {

	private String name;
	private String facing; //up, down, left, right
	private Map<String,Queue<ImageIcon>> anim; // Animation sequence
	private int posX; // Co-ordinates for paint
	private int posY;
	private ImageIcon curImage;
	//private Timer animator;

	public actor(int pxSize) {
		posX = 6*pxSize;
		posY = 4*pxSize;
		curImage = new ImageIcon("bin/gako_down_01.png");
		facing = "down";
		anim = new HashMap<String,Queue<ImageIcon>>();
		anim.put("down",new LinkedList<ImageIcon>());
		anim.put("up",new LinkedList<ImageIcon>());
		anim.put("left",new LinkedList<ImageIcon>());
		anim.put("right",new LinkedList<ImageIcon>());
		for(int i=1;i<=4;i++){
			for(String s : anim.keySet()){	
                anim.get(s).offer(new ImageIcon("bin/gako_"+s+"_0"+i+".png"));
            }
		}
	}
	
	public int getX(){ return posX; }
	
	public int getY(){ return posY; }

	public Image getImage() {
		return curImage.getImage();
	}

	public void setFace(String dir) {
		facing = dir;
	//	curImage = new ImageIcon("gako_"+dir+"_01.png");
	}

	public boolean isFacing(String dir) {
		return facing.equals(dir);
	}
	
	public String direction(){
		return facing;
	}
	/*
	public void moving(boolean movin){
		if(movin){
			animator.start();
		} else {
			animator.stop();
		}
	}

	public void actionPerformed(ActionEvent e) {
        ImageIcon ans = anim.get(facing).peek();
        anim.get(facing).offer(ans);
        anim.get(facing).poll();
        curImage = ans;
	}
	*/
	public void nextImg(int frame){
		int i = frame/8;
		curImage = new ImageIcon("bin/gako_"+facing+"_0"+i+".png");
				/*
		ImageIcon ans = anim.get(facing).peek();
        anim.get(facing).offer(ans);
        anim.get(facing).poll();
        curImage = ans;
        */
	}
	
	public void moveUp(){ posY=posY-1;}
	public void moveDown(){ posY=posY+1;}
	public void moveLeft(){ posX=posX-1;}
	public void moveRight(){ posX=posX+1;}
}
