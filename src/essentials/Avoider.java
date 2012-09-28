package essentials;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class Avoider {

	private Mover mover;
	private Scanner scanner;
	private boolean keepAvoiding;
	
	public Avoider(Mover m, Scanner s) {
		mover = m;
		scanner = s;
	}
	
	public void avoid(PolarPoint obstacleLocation) {
		
		keepAvoiding = true;
		
		boolean turnRight;
		
		mover.travel(-5, false);
		
		if (obstacleLocation.angle > 0) {
			mover.turn(-100 + obstacleLocation.angle, false);
			turnRight = false;
			Sound.playNote(Sound.PIANO, 200, 50);
			System.out.println("Turning left now, will turn right later.");
		} else {
			mover.turn(100 + obstacleLocation.angle, false);
			turnRight = true;
			Sound.playNote(Sound.PIANO, 250, 50);
			System.out.println("Turning right now, will turn left later.");
		}
		
		mover.travel(30, false);
		
		if (turnRight && keepAvoiding) {
			Sound.playNote(Sound.PIANO, 250, 50);
			System.out.println("Turning -90. 'right'");
			mover.turn(-90, true);
		} else if (keepAvoiding) {
			Sound.playNote(Sound.PIANO, 200, 50);
			System.out.println("Turning 90. 'left'");
			mover.turn(90, true);
		}
		
	}
	
	public void stopAvoiding() {
		keepAvoiding = false;
	}
	
}
