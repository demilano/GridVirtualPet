import static org.junit.Assert.*;

import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;


public class petTest {

	private ArrayList<Point> aHappyPath;
	private pet petTest;
	
	private square[][] testObstructionMap = new worldGenerator(10, 10, 40).getMap();
	
	public petTest(){
		testObstructionMap[3][2].isObstructed(true);
		testObstructionMap[4][1].isObstructed(true);
		testObstructionMap[4][2].isObstructed(true);
		testObstructionMap[4][3].isObstructed(true);
		testObstructionMap[5][1].isObstructed(true);
		testObstructionMap[5][2].isObstructed(true);
		testObstructionMap[5][3].isObstructed(true);
		testObstructionMap[5][4].isObstructed(true);
		testObstructionMap[6][1].isObstructed(true);
		testObstructionMap[6][2].isObstructed(true);
		testObstructionMap[6][3].isObstructed(true);
		testObstructionMap[6][4].isObstructed(true);
		testObstructionMap[6][5].isObstructed(true);
		testObstructionMap[7][1].isObstructed(true);
		testObstructionMap[7][2].isObstructed(true);
		testObstructionMap[7][3].isObstructed(true);
		testObstructionMap[8][2].isObstructed(true);
	}

	public void newHappyPath() {
		petTest = new pet(0, 0, 0, 0, (new worldGenerator(10, 10, 40)).getMap());
		aHappyPath = new ArrayList<Point>();
		aHappyPath.add(new Point(1,0));
		aHappyPath.add(new Point(1,1));
		aHappyPath.add(new Point(2,1));
		aHappyPath.add(new Point(2,2));
		aHappyPath.add(new Point(3,2));
		aHappyPath.add(new Point(3,3));
		aHappyPath.add(new Point(4,3));
		aHappyPath.add(new Point(4,4));
		aHappyPath.add(new Point(5,4));
		aHappyPath.add(new Point(5,5));
		for(Point p : aHappyPath){
			System.out.println("HappyPath : " + p.toString());
		}
	}
	
	public void newHardHappy() {
		petTest = new pet(0, 0, 0, 0, (new worldGenerator(10, 10, 40)).getMap());
		aHappyPath = new ArrayList<Point>();
		aHappyPath.add(new Point(1,0));
		aHappyPath.add(new Point(1,1));
		aHappyPath.add(new Point(1,2));
		aHappyPath.add(new Point(2,2));
		aHappyPath.add(new Point(2,3));
		aHappyPath.add(new Point(2,4));
		aHappyPath.add(new Point(3,4));
		aHappyPath.add(new Point(3,5));
		aHappyPath.add(new Point(3,6));
		aHappyPath.add(new Point(4,6));
		aHappyPath.add(new Point(4,7));
		aHappyPath.add(new Point(4,8));
		aHappyPath.add(new Point(5,8));
		aHappyPath.add(new Point(5,9));
		aHappyPath.add(new Point(5,10));
		for(Point p : aHappyPath){
			System.out.println("HappyPath : " + p.toString());
		}
	}
	/**
	@Test public void trySimpleHappy() {
		Point dest = new Point(5,5);
		newHappyPath();
		ArrayList<Point> genPath = petTest.happyPath(5, 5, 0, 0);
		for(Point p : genPath){
			System.out.println("New Path : " + p.toString());
		}
	//	assertTrue(genPath.containsAll(aHappyPath));
		assertTrue(genPath.get(genPath.size()-2).equals(dest));

	}
	
	@Test public void tryHardHappy(){
		Point dest = new Point(5,10);
		newHardHappy();
		ArrayList<Point> genPath = petTest.happyPath(5, 10, 0, 0);
		for(Point p : genPath){
			System.out.println("New Path : " + p.toString());
		}
	//	assertTrue(genPath.containsAll(aHappyPath));
		assertTrue(genPath.get(genPath.size()-1).equals(dest));
	}

	@Test public void tryBackwards(){
		Point dest = new Point(5,-7);
		newHardHappy();
		ArrayList<Point> genPath = petTest.happyPath(5, -7, 0, 0);
		for(Point p : genPath){
			System.out.println("New Path : " + p.toString());
		}
		assertTrue(genPath.get(genPath.size()-1).equals(dest));
	}
	
	@Test public void trySkewed(){
		Point dest = new Point(3,-17);
		newHardHappy();
		ArrayList<Point> genPath = petTest.happyPath(3, -17, 0, 0);
		for(Point p : genPath){
			System.out.println("New Path : " + p.toString());
		}
		assertTrue(genPath.get(genPath.size()-1).equals(dest));
	}
	
	@Test public void tryGoingUp(){
		petTest = new pet(4, 5, 0, 0, (new worldGenerator(10, 10, 40)).getMap());
		Point dest = new Point(12,4);
		ArrayList<Point> genPath = petTest.happyPath(12, 4, 0, 0);
		for(Point p : genPath){
			System.out.println("New Path : " + p.toString());
		}
		assertTrue(genPath.get(genPath.size()-1).equals(dest));
	}
	
	/**
	@Test public void tryNewFace(){
		petTest = new pet(2,2,0,0, testObstructionMap);
		Point dest = new Point(9,2);
		assertEquals(petTest.findOpposite("down"), "up");
		assertEquals(petTest.findOpposite("up"), "down");
		assertEquals(petTest.findOpposite("right"), "left");
		assertEquals(petTest.findOpposite("left"), "right");
	}
	*/
	/**
	@Test public void findFirstFace(){
		petTest = new pet(2,2,0,0, testObstructionMap);
		Point dest = new Point(9,2);
		Point obstruction = new Point(3,2);
		Point curPoint = new Point(2,2);
		assertEquals(petTest.findFirstFace(curPoint.getX()-obstruction.getX(), curPoint.getY()-obstruction.getY(), "anti"), "down");
		assertEquals(petTest.findFirstFace(curPoint.getX()-obstruction.getX(), curPoint.getY()-obstruction.getY(), "clock"), "up");
		obstruction = new Point(2,1);
		assertEquals(petTest.findFirstFace(curPoint.getX()-obstruction.getX(), curPoint.getY()-obstruction.getY(), "anti"), "right");
		assertEquals(petTest.findFirstFace(curPoint.getX()-obstruction.getX(), curPoint.getY()-obstruction.getY(), "clock"), "left");
	}
	*/
	/**
	@Test public void tryMove(){
		petTest = new pet(2,2,0,0, testObstructionMap);
		Point curPoint = new Point(2,2);
		assertEquals(petTest.movePoint("up", (int)curPoint.getX(), (int)curPoint.getY()), new Point(2,1));
		assertEquals(petTest.movePoint("down", (int)curPoint.getX(), (int)curPoint.getY()), new Point(2,3));
		assertEquals(petTest.movePoint("left", (int)curPoint.getX(), (int)curPoint.getY()), new Point(1,2));
		assertEquals(petTest.movePoint("right", (int)curPoint.getX(), (int)curPoint.getY()), new Point(3,2));
	}
	*/
	@Test public void testCheckFace(){
		petTest = new pet(2,2,0,0, testObstructionMap);
		Point curPoint = new Point(2,2);
		assertTrue(petTest.checkFace("right", curPoint));
		assertFalse(petTest.checkFace("left", curPoint));
		curPoint = new Point(5,0);
		assertTrue(petTest.checkFace("down", curPoint));
	}
	/**
	@Test public void testObstructedPath(){
		petTest = new pet(2,2,0,0, testObstructionMap);
		Point curPoint = new Point(2,2);
		Point dest = new Point(9,2);
		Point obstruction = new Point(3,2);
		petTest.obPath(curPoint, obstruction, "anti");
	}

	ArrayList<Point> p1;
	ArrayList<Point> p2;
	
	@Test public void testShortestPath(){
		p1 = new ArrayList<Point>();
		p2 = new ArrayList<Point>();
		p1.add(new Point(3,1));
		p1.add(new Point(4,1));
		p1.add(new Point(5,1));
		p1.add(new Point(6,1));
		p2.add(new Point(3,2));
		p2.add(new Point(3,3));
		p2.add(new Point(6,1));
		petTest = new pet(2,2,0,0, testObstructionMap);
		petTest.setDestination(new Point(6,1));
		assertEquals(petTest.findShortest(p1, p2, new ArrayList<Point>()),p2);
	}
		*/
}
