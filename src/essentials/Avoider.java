package essentials;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.util.Delay;

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

		keepAvoiding = true;

		boolean isLeftObstacle = true;
		boolean isRightObstacle = true;


		while (isLeftObstacle && isRightObstacle) {
			mover.travel(-5);
			
			scanner.scanAtAngle(90);
			isLeftObstacle = detector.getObstacleLocation().dist < 50;
			scanner.scanAtAngle(-90);
			isRightObstacle = detector.getObstacleLocation().dist <50;
		}


		if (!isLeftObstacle) {
			mover.turn(90, false);
			Sound.playNote(Sound.PIANO, 200, 50);
			System.out.println("Turning left now, will turn right later.");
		} else if (!isRightObstacle) {
			mover.turn(-90, false);
			Sound.playNote(Sound.PIANO, 250, 50);
			System.out.println("Turning right now, will turn left later.");
		}

		if (keepAvoiding) {
			mover.travel(30, false);
		}

	}

	public void stopAvoiding() {
		keepAvoiding = false;
	}

}
