import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** A Path finder that doesn't just find the smallest path, but attempts to simulate a non-omniscient entity 
 * and the path choice they may take
 * 
 * @author Lord Mumford
 *
 */
public class pathFinder {
	
	Point destination;
	Point curPos;
	square[][] theMap;
	
	public pathFinder(square[][] theMap){
		this.theMap = theMap;
	}
	
	public Queue<String> getPath(Point curPos, Point destination){
		this.curPos = curPos;
		this.destination = destination;
		ArrayList<Point> points = generatePath((int)destination.getX(),(int)destination.getY());
		return generateDirections(points);
	}
	
	public ArrayList<Point> generatePath(int dx,int dy){
		// 1. Try happy path. If it fails...
		// 2. Get last position of happy path failing.
		// Path1 = obPath("clock"); // Starts at point, ends at same point (goes right round baby right round) or ends at destination
		// Path 2 = obPath("anti");
		// If contains destination, return the smaller of the two
		// Else, Point closest = findClosestPoint(Path1); // Finds closest point to the destination
		// This point will have the smallest number of steps to get there (doesn't matter about obstructions).
		// Then, remove all from after this point in both paths.
		// Try the happy path from this new point
		// If happy path reaches an obstruction, rinse and repeat
		// While(Path1.contains(desination)){ etc
		// Then compare the sizes and return the smaller one
		destination = new Point(dx,dy);
		ArrayList<Point> happyPath = happyPath(dx,dy,(int)curPos.getX(),(int)curPos.getY());
		if(happyPath.contains(destination)){ 
			System.out.println("Happy");
			happyPath.remove(happyPath.size()-1);
			generateDirections(happyPath);
			return happyPath;
		} else {
			if(happyPath.size()==1){ happyPath.add(0,new Point((int)curPos.getX(),(int)curPos.getY())); }
			Point start; //= happyPath.get(happyPath.size()-2); // Gets the last point before encounters collision
			Point ob; //= happyPath.get(happyPath.size()-1);
			ArrayList<Point> thePath = new ArrayList<Point>(); // The main path
			thePath.addAll(happyPath);
			ArrayList<Point> clockP; // The clockwise path
			ArrayList<Point> antiP; // "" anti"" ""
			Point closestP;
		//	System.out.println(thePath);
			while(!thePath.contains(destination)){ // While the paths don't contain the destination
				start = thePath.get(thePath.size()-2);
				ob = thePath.get(thePath.size()-1);
				clockP = obPath(start, ob, "clock");
				antiP = obPath(start, ob, "anti");
				closestP = findClosestP(clockP);
			//	System.out.println(closestP);
				int lastC = clockP.indexOf(closestP);
				int lastA = antiP.indexOf(closestP);
				clockP.subList(lastC+1, clockP.size()).clear();
				antiP.subList(lastA+1, antiP.size()).clear();
				if(clockP.size()>antiP.size()){
					clockP.addAll(happyPath(dx,dy,(int)closestP.getX(),(int)closestP.getY()));
					antiP.addAll(happyPath(dx,dy,(int)closestP.getX(),(int)closestP.getY()));
					thePath.addAll(antiP);
					System.out.println("Clock size: " + clockP.size());
					System.out.println("Anti size: " + antiP.size());
				} else {
					clockP.addAll(happyPath(dx,dy,(int)closestP.getX(),(int)closestP.getY()));
					antiP.addAll(happyPath(dx,dy,(int)closestP.getX(),(int)closestP.getY()));
					thePath.addAll(clockP);
					System.out.println("Clock size: " + clockP.size());
					System.out.println("Anti size: " + antiP.size());
				}
			}
			//thePath.remove(0);
			//thePath.remove(1);
			int indDest = thePath.indexOf(destination);
			System.out.println(indDest);
			thePath.subList(indDest+1, thePath.size()).clear();
			System.out.println(thePath);
			return thePath;
		}
	}
	
	
	
	
	private Point findClosestP(ArrayList<Point> points) {
		Point closest = points.get(0);
		int smallestSteps = 999;
		int dx = (int) destination.getX();
		int dy = (int) destination.getY();
		int cx;
		int cy;
		int steps;
		for(Point p : points){
			cx = (int)p.getX();
			cy = (int)p.getY();
			steps = Math.abs(dx-cx) + Math.abs(dy-cy);
			if(steps<smallestSteps){
				closest = p;
				smallestSteps = steps;
			//	System.out.println("The closest is: " + closest);
			}
		}
		return closest;
	}

	/** Generates directions for path
	 * @return 
	 * 
	 */
	private Queue<String> generateDirections(ArrayList<Point> pPath){
		Queue<String> path = new LinkedList();
		Point curPoint = curPos;
		for(Point p : pPath){
			if(!theMap[(int)p.getX()][(int)p.getY()].obstructed()){
			if(p.getX()>curPoint.getX()){ path.offer("right"); }
			else if(p.getX()<curPoint.getX()){ path.offer("left"); }
			else if(p.getY()>curPoint.getY()){ path.offer("down"); }
			else if(p.getY()<curPoint.getY()){ path.offer("up"); }
			curPoint = p;
			}
		}
		return path;
	}
	
	/** The Happy Path
	 * Creates a natural beeline towards the destination. It turns based on the shortest 
	 * path. It turns at regular intervals based on the total steps divided by the shortest path
	 * Either returns one past the destination, or returns when an obstruction is encountered.
	 * In which case, happyPath.size()-2 is the last position it could access, and happyPath.size()-1
	 * is the position of the obstruction
	 * 
	 * @param dx Destination x co-ordinate
	 * @param dy Destination y co-ordinate
	 * @param cx Current x co-ordinate (based on mental traversal. Doesn't necessarily represent current position of pet)
	 * @param cy Current y co-ordinate
	 * @return An ArrayList up to and including destination, or up to and including an obstruction
	 */
	public ArrayList<Point> happyPath(int dx,int dy, int cx, int cy){
		ArrayList<Point> happyPath = new ArrayList<Point>();
		int stX = dx - cx; // X steps
		int stY = dy - cy; // Y steps
		int newX = cx;
		int newY = cy;	
		int incX = 4; // increment
		int incY = 4; 
		int nextStep;
		if(dx==cx && dy==cy){
			happyPath.add(new Point(cx,cy));
			happyPath.add(new Point(cx,cy+1));
			return happyPath;
		}
		int abstX = Math.abs(stX);
		int abstY = Math.abs(stY);

		if(abstX<abstY){
			incY = abstY/stY;
			if(abstX==0){
				incX = 0;
				nextStep = -1;
			} else {
				incX = abstX/stX;
				nextStep = ((abstX+abstY)/abstX);
			}
		} else {
			incX = abstX/stX;
			if(abstY==0){
				incY = 0;
				nextStep = -1;
			} else {
				incY = abstY/stY;
				nextStep = ((abstX+abstY)/abstY);
			}
		}
		for(int i=1;i<=abstX+abstY+1;i++){
			if(abstX<abstY){
				if(i%nextStep==0 && newX!=dx){ // Case One: if x path smaller
					newX = newX + incX;
					happyPath.add(new Point(newX,newY));
				} else { // Case Two
					newY = newY + incY;
					happyPath.add(new Point(newX,newY));
				}
			} else {
				if(i%nextStep==0 && newY!=dy){ // Case Three: if y path smaller
					newY = newY + incY;
					happyPath.add(new Point(newX,newY));
				} else { // Case Four
					newX = newX + incX;
					happyPath.add(new Point(newX,newY));
				}
			}
			if(theMap[newX][newY].obstructed()){
				return happyPath; }
		}
		return happyPath;
	}
	/** Obstruction path. Finds a path around an obstruction
	 * 
	 * @param start Where the obstruction begins
	 * @param obs Where obstruction encountered (to calculate direction)
	 * @param dest Final destination
	 * @param dir Clock-wise around obstruction, or anticlockwise
	 * @return Returns a list of points up until the obstruction path then happy path ends
	 */
	public ArrayList<Point> obPath(Point start, Point obs, String dir){
		ArrayList<Point> path = new ArrayList<Point>();
		String face = findFirstFace(start.getX()-obs.getX(),start.getY()-obs.getY(),dir);
		Point curPoint = start;
		Point oriPoint = curPoint; // Point of origin
		int n=0;
		while(curPoint!=destination){
			System.out.println(curPoint);
			if(dir.equals("anti")){
				if (!checkFace(findOpposite(nextF(face)),curPoint)) { // 'CheckSide', if not obstructed
					face = findOpposite(nextF(face));
					curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
					path.add(curPoint);
					if(curPoint.getX()==oriPoint.getX() && curPoint.getY()==oriPoint.getY()){ break; }
				} else {
					if(!checkFace(face,curPoint)){
						curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
						path.add(curPoint);
						if(curPoint.getX()==oriPoint.getX() && curPoint.getY()==oriPoint.getY()){ break; }
					} else {
						face = findOpposite(findOpposite(nextF(face)));
					}
				}
			} else {
				if (!checkFace(nextF(face),curPoint)) { // 'CheckSide'
					face = nextF(face);
					curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
					path.add(curPoint);
					if(curPoint.getX()==oriPoint.getX() && curPoint.getY()==oriPoint.getY()){ break; }
				} else {
					if(!checkFace(face,curPoint)){
						curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
						path.add(curPoint);
						if(curPoint.getX()==oriPoint.getX() && curPoint.getY()==oriPoint.getY()){ break; }
					} else {
						face = findOpposite(nextF(face));
					}
				}
			}
			
			
			if(n==0 && oriPoint!=curPoint){ oriPoint = curPoint; n++; }
		}
		return path;
	}

	/** Currently used for mental traversal in the obPath method
	 * Changes the position of the point
	 * 
	 * @param face Direction facing
	 * @param x Position x
	 * @param y Position y
	 * @return The new position of the point
	 */
	private Point movePoint(String face, int x, int y){
		if(face.equals("up")){ return new Point(x,y-1); }
		else if(face.equals("down")){ return new Point(x,y+1); }
		else if(face.equals("right")){ return new Point(x+1,y); }
		else { return new Point(x-1,y); }
	}

	/** The next face in the sequence after the pet has turned around
	 * 
	 * @param face Current facing
	 * @return The next facing
	 */
	private String nextF(String face){
		String newFace = "";
		if(face.equals("up")){ newFace = "right"; }
		else if (face.equals("right")){ newFace = "down"; }
		else if (face.equals("down")){ newFace = "left"; }
		else { newFace = "up"; }
		return newFace;
	}
	
	/** Finds the first direction the pet should face after reaching an
	 * obstruction based on whether it is going clockwise or anticlockwise
	 * 
	 * @param x Current x co-ordinate
	 * @param y Current y co-ordinate
	 * @param dir Clockwise or anti-clockwise
	 * @return Returns opposite face if anti, or face if clock
	 */
	private String findFirstFace(double x, double y, String dir) {
		String face;
		if(x==-1){ face = "up"; }
		else if(x==1){ face = "down"; }
		else if(y==-1){ face = "right"; }
		else { face = "left"; }
		if(dir.equals("anti")){
			return findOpposite(face);
		} else {
			return face;
		}
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
		if(face.equals("up")){ return theMap[x][y-1].obstructed(); }
		else if(face.equals("down")){ return theMap[x][y+1].obstructed(); }
		else if(face.equals("right")){ return theMap[x+1][y].obstructed(); }
		else { return theMap[x-1][y].obstructed(); }
	}
	
	/** Returns opposite direction. Used for methods involving going
	 * clockwise or anticlockwise
	 * 
	 * @param dir Direction of facing
	 * @return Opposite direction of facing
	 */
	private String findOpposite(String dir){
		if(dir.equals("up")){	return "down";	}
		if(dir.equals("down")){ return "up";	}
		if(dir.equals("left")){ return "right";	}
		else { return "left"; }
	}
}
