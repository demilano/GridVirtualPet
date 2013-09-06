import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class pathFinder2 {
	
	Point destination;
	Point curPos;
	square[][] theMap;
	
	public pathFinder2(square[][] theMap){
		this.theMap = theMap;
	}
	
	public Queue<String> getPath(Point curPos, Point destination){
		this.curPos = curPos;
		this.destination = destination;
		ArrayList<Point> points = generatePath((int)destination.getX(),(int)destination.getY());
		//ArrayList<Point> points = obPath(curPos, destination, "clock");
		return generateDirections(points);
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
		for(String s : path){
			System.out.println(s);
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
	//	happyPath.add(new Point(cx,cy));
		/** Fix this shit
		 * 
		 
		if(Math.abs(stX)<Math.abs(stY)){
			if(stX!=0){ 
				incX = Math.abs(stX)/stX; 
				incY = Math.abs(stY)/stY; 
				nextStep = (Math.abs(stX)+Math.abs(stY))/Math.abs(stX);
			} else { 
				incX = 0; 
				incY = Math.abs(stY)/stY;
				nextStep = -1;
			}
		} else {
			if(stY!=0){ 
				incX = Math.abs(stX)/stX; 
				incY = Math.abs(stY)/stY; 
				nextStep = (Math.abs(stX)+Math.abs(stY))/Math.abs(stY);
			} else { 
				incY = 0; 
				incX = Math.abs(stX)/stX; 
				nextStep = -1;
			}
		}
		*/
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
		//	System.out.println(new Point(newX,newY));
			if(theMap[newX][newY].obstructed()){// || (newX==(int)destination.getX() && newY==(int)destination.getY())){ 
				return happyPath; }
		}
		return happyPath;
	}

	/** Generates path to get to somewhere
	 * 
	 */
	public ArrayList<Point> generatePath(int dx,int dy){
		destination = new Point(dx,dy);
		ArrayList<Point> happyPath = happyPath(dx,dy,(int)curPos.getX(),(int)curPos.getY());
		if(happyPath.get(happyPath.size()-1)==destination){ // If last element is ** Could do this backwards... *
			System.out.println("Happy");
			generateDirections(happyPath);
			return happyPath;
		} else {
			if(happyPath.size()==1){ happyPath.add(0,new Point((int)curPos.getX(),(int)curPos.getY())); }
			Point start = happyPath.get(happyPath.size()-2); // Gets the last point before encounters collision
			Point ob = happyPath.get(happyPath.size()-1);
			ArrayList<Point> shortest = findShortest(obPath(start, ob, "clock"), obPath(start, ob, "anti"), new ArrayList<Point>());
			shortest.remove(shortest.size()-1); // Change to remove everything after destination
			happyPath.addAll(shortest);
			return happyPath;
		}
	}
	
	public ArrayList<Point> findShortest(ArrayList<Point> p1, ArrayList<Point> p2, ArrayList<Point> oldp){
		ArrayList<Point> list1 = new ArrayList<Point>(oldp);
		ArrayList<Point> list2 = new ArrayList<Point>(oldp);
		list1.addAll(p1);
		list2.addAll(p2);
		if(list1.contains(destination) && list2.contains(destination)){
			if(list2.size()<list1.size()){ return list2; }
			else { return list1; }
		} 
		System.out.println(list1);
		Point stp1 = list1.get(list1.size()-2);
		Point stp2 = list2.get(list2.size()-2);
		Point ob1 = list1.get(list1.size()-1);
		Point ob2 = list2.get(list2.size()-1);
		if (list1.contains(destination)){
			list2 = findShortest(obPath(stp2,ob2,"clock"), obPath(stp2,ob2,"anti"),list2); 
			return findShortest(list1,list2,new ArrayList<Point>());
		} else if (list2.contains(destination)){
			list1 = findShortest(obPath(stp1,ob1,"clock"),obPath(stp1,ob1,"anti"),list1); 
			return findShortest(list1,list2,new ArrayList<Point>());
		} else {
			list1 = findShortest(obPath(stp1,ob1,"clock"),obPath(stp1,ob1,"anti"),list1); 
			list2 = findShortest(obPath(stp2,ob2,"clock"), obPath(stp2,ob2,"anti"),list2); 
			return findShortest(list1,list2,new ArrayList<Point>());
		}
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
	//	System.out.println("First face: " + face);
		Point curPoint = start;
		Point oriPoint = curPoint; // Point of origin
		int n = 0; // Trial stat
		while(n<30){
			n++; // Remove this. At the moment limits. Will be while !checkEnd
		//	System.out.println("THE FACE IS: " + face);
			if(checkEnd((int)curPoint.getX(),(int)curPoint.getY(),face)){ break; };
			if(dir.equals("anti")){
				//System.out.println("Check side" + findOpposite(nextF(face)));
				if (!checkFace(findOpposite(nextF(face)),curPoint)) { // 'CheckSide', if not obstructed
					//System.out.println("Not obstucted");
					face = findOpposite(nextF(face));
					curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
					path.add(curPoint);
				//	theMap[(int)curPoint.getX()][(int)curPoint.getY()].isObstructed(true);
					//System.out.println("Facing: " + face);
					//System.out.println(curPoint);
				} else {
					if(!checkFace(face,curPoint)){
					//	System.out.println("moving");
						curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
						path.add(curPoint);
					//	theMap[(int)curPoint.getX()][(int)curPoint.getY()].isObstructed(true);
						//System.out.println(curPoint);
					} else {
						//System.out.println(face);
						face = findOpposite(findOpposite(nextF(face)));
					//	System.out.println("Obstructed. Next face: " + face);
					}
				}
			} else {
				//System.out.println("Check side: " + nextF(face));
				if (!checkFace(nextF(face),curPoint)) { // 'CheckSide'
					face = nextF(face);
					//System.out.println("Move: " + face);
					curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
					path.add(curPoint);
					// Add curPoint to list
				//	theMap[(int)curPoint.getX()][(int)curPoint.getY()].isObstructed(true);
					//System.out.println(curPoint);
				} else {
					if(!checkFace(face,curPoint)){
						//System.out.println("Straight move: " + face);
						curPoint = movePoint(face, (int)curPoint.getX(), (int)curPoint.getY());
						// Maybe break. Just don't allow for going backward. If face = left, break
						path.add(curPoint);
					//	theMap[(int)curPoint.getX()][(int)curPoint.getY()].isObstructed(true);
						//System.out.println(curPoint);
					} else {
						//System.out.println("Obstructed: " + face);
						face = findOpposite(nextF(face));
						//System.out.println("Obstructed. Next face: " + face);
					}
				}
			}
		//	if(curPoint==oriPoint){ break; }
		//	if(curPoint==destination){ return path; }
		//	theMap[(int) curPoint.getX()][(int) curPoint.getY()].changeImg();
		}
		int cx = (int)curPoint.getX();
		int cy = (int)curPoint.getY();
		path.addAll(happyPath((int)destination.getX(),(int)destination.getY(), cx, cy)); // Adds a happy path until obstruction
		return path;
	}


	/** Checks if it can escape from the obstructed path and enter into the happy path
	 * May need editing. Depends on the relative position of the destination and whether
	 * there are two free squares on two sides of the current position
	 * 
	 * @param x Current x
	 * @param y Current y
	 * @return True if can exit, false if not
	 */
	private boolean checkEnd(int x, int y, String face) {
		int dx = (int)destination.getX();
		int dy = (int)destination.getY();
		if(x==dx && y==dy){ return true; }

		if(x<=dx && y<=dy && !theMap[x+1][y].obstructed() && !theMap[x][y+1].obstructed()
			&& !theMap[x+2][y].obstructed() && !theMap[x][y+2].obstructed()
			&& !(face.equals("left") || face.equals("up")))
		{ System.out.println("hi"); return true; } // dest is bottom right
		if(x<=dx && y>=dy && !theMap[x+1][y].obstructed() && !theMap[x][y-1].obstructed()
				&& !theMap[x+2][y].obstructed() && !theMap[x][y-2].obstructed()
				&& !(face.equals("left") || face.equals("down")))
		{ System.out.println("Sup"); return true; } 
		if(x>=dx && y<=dy && !theMap[x-1][y].obstructed() && !theMap[x][y+1].obstructed()
			&& !theMap[x-2][y].obstructed() && !theMap[x][y+2].obstructed()
			&& !(face.equals("right") || face.equals("up")))
		{ System.out.println("Yo"); return true; } 
		if(x>=dx && y>=dy && !theMap[x-1][y].obstructed() && !theMap[x][y-1].obstructed()
			&& !theMap[x-2][y].obstructed() && !theMap[x][y-2].obstructed()
			&& !(face.equals("right") || face.equals("down")))
		{ System.out.println("lol"); return true; }
		
		if(y>dy && x==dx && !theMap[x][y-1].obstructed()){
			if((y-1)==dy || (x-y)==dy){ return true; }
			if(!theMap[x][y-2].obstructed()){ System.out.println("what"); return true; }
		}
		
		if(y<dy && x==dx && !theMap[x][y+1].obstructed()){
			if((y+1)==dy || (y+2)==dy){ return true; }
			if(!theMap[x][y+2].obstructed()){ System.out.println("da"); return true; }
			
		}
		if(x>dx && y==dy && !theMap[x-1][y].obstructed()){
			if((x-1)==dx || (x-2)==dx){ return true; }
			if(!theMap[x-2][y].obstructed()){ System.out.println("fuk"); return true; }
			
		}
		//if(x<dx && y==dy && !theMap[x+1][y].obstructed()){
		//	if((x+1)==dx || (x+2)==dx){ return true; }
		//	if(!theMap[x+2][y].obstructed()){ System.out.println("omg"); return true; }
		//}
		
		boolean clear;
		if(x<dx && y==dy){
			clear = true;
			for(int i=0;i!=dx;i++){
				if(theMap[x+i][y].obstructed()){ clear = false; }
			}
			if(clear){ return true; }
		}
		if(x>dx && y==dy){
			clear = true;
			for(int i=0;i!=dx;i++){
				if(theMap[x-i][y].obstructed()){ clear = false; }
			}
			if(clear){ return true; }
		}
		if(y<dy && x==dx){
			clear = true;
			for(int i=0;i!=dy;i++){
				if(theMap[x][y+i].obstructed()){ clear = false; }
			}
			if(clear){ return true; }
		}
		if(y>dy && x==dx){
			clear = true;
			for(int i=0;i!=dy;i++){
				if(theMap[x][y-i].obstructed()){ clear = false; }
			}
			if(clear){ return true; }
		}
		return false;
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
