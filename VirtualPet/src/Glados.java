import static org.junit.Assert.fail;

import org.junit.Test;


	public class Glados {

		@Test public void continuousMovementTest(){
			homeworld testWorld = new homeworld();
			int n = testWorld.getCurRow();
			while(n<100){
			testWorld.doMove("down");
			n++;
			if(testWorld.getCurRow()!=n){
				fail("Failed at " + n);
			}
			}
		}
	}