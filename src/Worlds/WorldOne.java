package Worlds;

import Game.Entities.Static.Apple;
import Main.Handler;

import java.awt.*;
import java.util.Random;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class WorldOne extends WorldBase{

    public WorldOne (Handler handler) {
        super(handler);

        //has to be a number bigger than 20 and even
        GridWidthHeightPixelCount = 60;
        GridPixelsize = (800/GridWidthHeightPixelCount);
        playerLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        appleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];

    }

    int counter = 0;
    
    @Override
    public void tick() {
        super.tick();
        counter++;
//        System.out.println(counter);
        player.tick();
        
        if(!appleOnBoard){
            appleOnBoard=true;
            int appleX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);
            int appley = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);

            //change coordinates till one is selected in which the player isnt standing
            boolean goodCoordinates=false;
            do{
                if(!handler.getWorld().playerLocation[appleX][appley]){
                    goodCoordinates=true;
                }
            }while(!goodCoordinates);

            apple = new Apple(handler,appleX,appley);
            appleLocation[appleX][appley]=true;

        }
        
        //rotten beginning 
        else {
        	if (counter % player.speed == 0) {
        		apple.turnsPassed++;
        		if (apple.turnsPassed == 60) {
        			apple.setGood(false); 
//        			System.out.println("Apple is rotten :(");
        		} 
        				 
        		
        	}   
        }
    }

    @Override
    public void render(Graphics g){
        super.render(g);
        player.render(g,playerLocation);
    }

}
