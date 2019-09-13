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

		// needs fix
//         
//    if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
//     Tail tail=new Tail(handler.getWorld().body.getLast().x,handler.getWorld().body.getLast().y,handler);
//     handler.getWorld().body.addLast(tail); 
//      }

//        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
//       	handler.getWorld().playerLocation = new Boolean[0][0];
//        }
//        
//        
//        
	}
//}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
//	lenght++;
//    Tail tail= null;
//    

	public void checkCollisionAndMove() {
		handler.getWorld().playerLocation[xCoord][yCoord] = false;
		int x = xCoord;
		int y = yCoord;
		switch (direction) {
		case "Left":
			if (xCoord == 0) {
				kill();
			} else if (facing != "Right") {
				xCoord--;
				facing = direction;
			}
			else {
				direction = facing;
			}
			break;
		case "Right":
			if (xCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
				kill();
			} else if (facing != "Left") {
				xCoord++;
				facing = direction;
			}
			else {
				direction = facing;
			}
			break;
		case "Up":
			if (yCoord == 0) {
				kill();
			} else if (facing != "Down") {
				yCoord--;
				facing = direction;
			}
			else {
				direction = facing;
			}
			break;
		case "Down":
			if (yCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
				kill();
			} else if (facing != "Up") {
				yCoord++;
				facing = direction;
			}
			else {
				direction = facing;
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

	}

	public void render(Graphics g, Boolean[][] playeLocation) {
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.white);

				if (playeLocation[i][j]) {
//                	System.out.println(handler.getWorld().GridWidthHeightPixelCount);
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
				}
				
				if (handler.getWorld().appleLocation[i][j]) {
					if(handler.getGame().gameState.world.apple.isGood()) {
						g.setColor(Color.white);
					}
					else {
						g.setColor(Color.magenta);
					}
					
					
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
				}

			}
		}

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
						System.out.println("Tu biscochito");
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
		}
	}

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
