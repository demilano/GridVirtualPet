
public class worldGenerator {
	
	/**
	 * Gets passed totalrows, cols and pxsize
	 * constructs a 2D array of squares according to a formula
	 * (will eventually be user input that creates the array, but at the
	 * moment it's just done automatically
	 * Once the array of basic squares and textures has been created,
	 * adds the details (grass borders, etc)
	 * Then will eventually add all the amimals and stuff
	 */
	private square[][] themap;
	private int totalRows;
	private int totalCols;
	private int pxSize;
	
	private int randRow;
	private int randCol;

	public worldGenerator(int totalRows, int totalCols, int pxSize) {
		// Create the blank map
		themap = new square[totalCols][totalRows];
		this.totalRows = totalRows;
		this.totalCols = totalCols;
		this.pxSize = pxSize;
		// randomly place dirt patches
		for(int i=0;i<50;i++){
			randRow = (int)(Math.random()*totalRows);
			randCol = (int)(Math.random()*totalCols);

		//	themap[randCol][randRow] = new square((randCol-1)*pxSize,(randRow-1)*pxSize,"dirt");
		}
		// fill the rest with grass
		for(int c=0;c<totalCols;c++){
			for(int r=0;r<totalRows;r++){
				if(c==0 || r == 0 || c == totalCols-1 || r == totalRows-1){
					themap[c][r]= new square((c-1)*pxSize,(r-1)*pxSize,"flower");
				} else if(themap[c][r]==null){
					themap[c][r]= new square((c-1)*pxSize,(r-1)*pxSize,"grass");
				}
			}
		}	
	//	addBorders();
	}
	
	private void addBorders() {
		// Add the shiz, border 'n that
				for(int c=0;c<totalCols;c++){
					for(int r=0;r<totalRows;r++){
					//	if(themap[c][r].type().equals("dirt")){
							// Here's the algorithm for calculating borders
							// If r-1>0 and themap[c][r-1].type().equals("grass")
									// if c-1 blah blah or blah blah	
						}
					}
				}
	

	public square[][] getMap(){
		return themap;
	}
}
			
	
	


