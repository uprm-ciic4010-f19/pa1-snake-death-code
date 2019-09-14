package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.Entities.Static.Apple;
import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;
	public double speed = 5;
	public int xCoord;
	public int yCoord;

	public int moveCounter;

	private State GameOver;
	
	
	// Score
    public int score = 0;
    public double trackScore;


    public int getScore() {
        return this.score;
    }
	

	public String direction;// is your first name one?
	public String facing;

	public Player(Handler handler) {
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction = "Right";
		facing = "Right";
		justAte = false;
		lenght = 1;

	
		
		
	}

	  
	public void tick() {
		moveCounter++;
	
		if (moveCounter >= speed) {
			checkCollisionAndMove();
			moveCounter = 0;
		}
		

        //pause at the touch of the P key
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
            State.setState(handler.getGame().pauseState);
        }
		
		// increase snake speed
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD)) {
			speed = speed - 0.5;
		}
		// decrease snake speed
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			speed = speed + 0.5;
		}

		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)) {
			direction = "Up";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)) {
			direction = "Down";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)) {
			direction = "Left";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)) {
			direction = "Right";
		}

		
		
    
		//Add the N button (Adds a New Tail!)	
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {

	     handler.getWorld().body.addLast(new Tail(xCoord, yCoord,handler));
	             Eat();
	             if (handler.getWorld().appleOnBoard == false) {
	                 handler.getWorld().appleOnBoard=true;
	             
//	                  this.speed --;
//		             if (this.score == 0 ) {
//			             this.score = 0;}
//		             else {this.score -=9;}
//			                 
	                 }

	             
	             }
		
        // Direction Handler, also, Backtracking Prevention has been added
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction != "Down"){
            direction="Up";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction != "Up"){
            direction="Down";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction != "Right"){
            direction="Left";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction != "Left"){
            direction="Right";
        }
	}


	public void checkCollisionAndMove() {
		handler.getWorld().playerLocation[xCoord][yCoord] = false;
		int x = xCoord;
		int y = yCoord;
        switch (direction){
        case "Left":
            if(xCoord==0){
            	// Teleport to the Right
            	xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
            }else{
                xCoord--;
            }
            break;
        case "Right":
            if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                // Teleport to the Left
            	xCoord = 0;
            	
            }else{
                xCoord++;
            }
            break;
        case "Up":
            if(yCoord==0){
            	// Teleport Down
            	 yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                
            }else{
                yCoord --;
            }
            break;
        case "Down":
            if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                // Teleport Up
            	 yCoord = 0;
            	
            }else{
                yCoord++;
            }
            break;
    }
		handler.getWorld().playerLocation[xCoord][yCoord] = true;

		if (handler.getWorld().appleLocation[xCoord][yCoord]) {
			Eat();
		}

		if (!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body
					.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y, handler));
		}
		
		// Kill the snake if it hits with itself
		for (int i = 0; i < handler.getWorld().body.size(); i++) {
			if (xCoord == handler.getWorld().body.get(i).x && yCoord==handler.getWorld().body.get(i).y) {
				kill();
			}
		}

	}

	public void render(Graphics g, Boolean[][] playeLocation) {
		
		//Custom Colors for Apple
		Color appleColor = new Color(190, 0, 45);
		Color appleRottenColor = new Color(124, 128, 95);
		
		
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.white);

				if (playeLocation[i][j]) {
					
					//Coordinate System on Grid, for testing.
					//System.out.println(handler.getWorld().GridWidthHeightPixelCount);
					
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
				}
				
				// Draw a Good Apple
				if (handler.getWorld().appleLocation[i][j]) {
					if(handler.getGame().gameState.world.apple.isGood()) {
						g.setColor(appleColor);
					}
					else {
				// Draw a Rotten Apple, great job Slowpoke.
						g.setColor(appleRottenColor);
					
					}
					
					
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
				}

			}
		}
		
		
		//Draw the Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ubuntu", Font.BOLD, 20));
        g.drawString("Score " + this.getScore(), 50, 50);

	}

	public void Eat() {
		handler.getWorld().appleLocation[xCoord][yCoord] = false;
		handler.getWorld().appleOnBoard = false;

		if (handler.getGame().gameState.world.apple.isGood()) {
			lenght++;
			Tail tail = null;
			switch (direction) {
			case "Left":
				if (handler.getWorld().body.isEmpty()) {
					if (this.xCoord != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = new Tail(this.xCoord + 1, this.yCoord, handler);
					} else {
						if (this.yCoord != 0) {
							tail = new Tail(this.xCoord, this.yCoord - 1, handler);
						} else {
							tail = new Tail(this.xCoord, this.yCoord + 1, handler);
						}
					}
				} else {
					if (handler.getWorld().body.getLast().x != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler);
					} else {
						if (handler.getWorld().body.getLast().y != 0) {
							tail = new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler);
						} else {
							tail = new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler);

						}
					}

				}
				break;
			case "Right":
				if (handler.getWorld().body.isEmpty()) {
					if (this.xCoord != 0) {
						tail = new Tail(this.xCoord - 1, this.yCoord, handler);
					} else {
						if (this.yCoord != 0) {
							tail = new Tail(this.xCoord, this.yCoord - 1, handler);
						} else {
							tail = new Tail(this.xCoord, this.yCoord + 1, handler);
						}
					}
				} else {
					if (handler.getWorld().body.getLast().x != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
					} else {
						if (handler.getWorld().body.getLast().y != 0) {
							tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler));
						} else {
							tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler));
						}
					}

				}
				break;
			case "Up":
				if (handler.getWorld().body.isEmpty()) {
					if (this.yCoord != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = (new Tail(this.xCoord, this.yCoord + 1, handler));
					} else {
						if (this.xCoord != 0) {
							tail = (new Tail(this.xCoord - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(this.xCoord + 1, this.yCoord, handler));
						}
					}
				} else {
					if (handler.getWorld().body.getLast().y != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler));
					} else {
						if (handler.getWorld().body.getLast().x != 0) {
							tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler));
						}
					}

				}
				break;
			case "Down":
				if (handler.getWorld().body.isEmpty()) {
					if (this.yCoord != 0) {
						tail = (new Tail(this.xCoord, this.yCoord - 1, handler));
					} else {
						if (this.xCoord != 0) {
							tail = (new Tail(this.xCoord - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(this.xCoord + 1, this.yCoord, handler));
						}
			
					}
				} else {
					if (handler.getWorld().body.getLast().y != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler));
					} else {
						if (handler.getWorld().body.getLast().x != 0) {
							tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler));
						}
					}

				}
				break;
			}
			handler.getWorld().body.addLast(tail);
			handler.getWorld().playerLocation[tail.x][tail.y] = true;
            
			// Add the Score
			this.score += 9;

            this.speed--;
            
            // Compute rotten score
            if(handler.getGame().gameState.world.apple.isGood()) {
            	this.score += Math.sqrt(2*score+1); 
            }
		}
	}

	// Kill the snake, because you hate it
	public void kill() {
		lenght = 0;
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j] = false;
				State.setState(handler.getGame().gameOverState);
			}
		}
		
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
