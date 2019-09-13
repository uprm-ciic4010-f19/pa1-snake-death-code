package Game.Entities.Static;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {
    
//	private boolean isGood();
	
	private boolean good;
	public int turnsPassed;

    private Handler handler;

    public int xCoord;
    public int yCoord;

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
        this.good = true;
        this.turnsPassed = 0;
    }
    
    public boolean isGood() {
    	return good;
    }
    
    public void setGood(boolean good) {
    	this.good = good;
    }


}
