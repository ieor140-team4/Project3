package essentials;

import java.util.Random;

import lejos.nxt.Sound;

public class Avoider {

	private Mover mover;
	private Scanner scanner;
	private boolean keepAvoiding;
	private ObstacleDetector detector;

	public Avoider(Mover m, Scanner s, ObstacleDetector d) {
		mover = m;
		scanner = s;
		detector = d;
	}

	public void avoid(PolarPoint obstacleLocation) {
		System.out.println("Avoiding...");

		keepAvoiding = true;

		int turnDirection = 0; //1 for left, -1 for right
		boolean isLeftObstacle = true;
		boolean isRightObstacle = true;


		while (isLeftObstacle && isRightObstacle) {
			System.out.println("Look left and right!");
			mover.travel(-10);
			
			isLeftObstacle = scanner.scanAtAngle(90).dist < 60;
			isRightObstacle = scanner.scanAtAngle(-90).dist < 60;
			System.out.println("Obstacle to right: " + isRightObstacle + " left: " + isLeftObstacle);
		}
		
		scanner.scanAtAngle(0);

		if (!isRightObstacle && isLeftObstacle) {
			turnDirection = -1; //No right obstacle -> turn left.
			Sound.playNote(Sound.PIANO, 200, 20);
		} else if (!isLeftObstacle && isRightObstacle) {
			turnDirection = 1; //No left obstacle -> turn right.
			Sound.playNote(Sound.PIANO, 250, 20);
		} else {
			Random r = new Random();
			int randomDirection = r.nextInt(2);
			if (randomDirection == 0) {
				randomDirection--;
			}
			turnDirection = randomDirection;
			
			Sound.playNote(Sound.PIANO, 200, 5);
			Sound.playNote(Sound.PIANO, 250, 5);
			Sound.playNote(Sound.PIANO, 300, 5);
			Sound.playNote(Sound.PIANO, 350, 5);
			Sound.playNote(Sound.PIANO, 400, 5);
			Sound.playNote(Sound.PIANO, 450, 5);
		}

		if (keepAvoiding) {
			mover.turn(turnDirection * 100, false);
			mover.travel(30, false);
			mover.turn(-1 * turnDirection * 100, true);
		}

	}

	public void stopAvoiding() {
		keepAvoiding = false;
	}

}
