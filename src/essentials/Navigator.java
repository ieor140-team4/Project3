package essentials;

import java.awt.*;
import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.Sound;

/**
 * The navigator is the top level class which controls the Mover and Scanners
 * of the robot. It keeps track of how far we are from the light and keeps track
 * of how to move and scan simultaneously, and what to do when we sense objects.
 * 
 * @author nate.kb
 */
public class Navigator implements ObstacleListener {

	private Mover mover;
	private Scanner scanner;
	private double distanceFromLight;
	private ObstacleDetector detector;
	private boolean obstacle;
	
	private boolean keepAvoiding;
	private int heading;


	public Navigator(Mover m, Scanner s, ObstacleDetector d) {
		mover = m;
		detector = d;
		scanner = s;
		s.addDetector(d);
		d.setObstacleListener(this);
		distanceFromLight = 20*30.55; // or 0
		obstacle = false;
		heading = 0;
	}

	/**
	 * Controls the robot and makes it go through the specified number
	 * of laps. Within each lap, we repeat a cycle of scan-update-steer,
	 * until a scan tells us we are within 40cm of the light. Then we
	 * turn around and reset our distance from the light.
	 * 
	 * @param numLaps The number of 1-way laps the robot should go.
	 */
	public void go(int numLaps) {
		int turnDist = 60;

		for (int i = 0; i < numLaps; i++) {
			while (distanceFromLight > turnDist) {
				if (!obstacle) {
					System.out.println("1.");
					scanner.scanForData(-30,60);
					PolarPoint lightLocation = scanner.getLightLocation();

					System.out.println("Light @ " + lightLocation.toString());

					if (!obstacle) {
						mover.goToLight(lightLocation.angle);
					}
					distanceFromLight = lightLocation.dist;
				}
			}

			mover.turn(180, false);
			mover.turn(1, true);
			distanceFromLight = 20*30.55;
		}
	}

	/** 
	 * When an object is found in the obstacle detector, this method
	 * implements the obstacle avoiding stuff. Right now, all that means
	 * is it stops, backs up from the obstacle, and waits for a button press.
	 * 
	 */
	public void objectFound(PolarPoint p) {
		System.out.println("Object found! " + detector.getObstacleLocation().toString());
		
		obstacle = true;
		keepAvoiding = false;
		
		mover.stop();
		avoid(p);
		
		obstacle = false;
	}
	
	/**
	 * 
	 * @param obstacleLocation
	 */
	public void avoid(PolarPoint obstacleLocation) {
		System.out.println("Avoiding...");

		keepAvoiding = true;

		int turnDirection = 0; //1 for left, -1 for right
		
		int scanAngle = 0;
		PolarPoint leftCorner = new PolarPoint(0,90);
		PolarPoint rightCorner = new PolarPoint(0,-90);
		int prevDist = 0;
		
		while ((leftCorner.angle >= 70) && (rightCorner.angle <= -70)) {
			mover.travel(-20);
		
		boolean isObjectAtAngle = scanner.scanAtAngle(scanAngle).dist < 50;
		while (scanAngle < 90 ) {
			scanAngle += 10;
			PolarPoint obstacle = scanner.scanAtAngle(scanAngle);
			isObjectAtAngle = obstacle.dist < 80;
			if (!isObjectAtAngle) {
				leftCorner = new PolarPoint(prevDist, scanAngle--);
				break;
			} else {
				prevDist = obstacle.dist;
			}
		}
		
		scanAngle = 0;
		isObjectAtAngle = scanner.scanAtAngle(scanAngle).dist < 50;
		while (scanAngle > -90 ) {
			scanAngle -= 10;
			PolarPoint obstacle = scanner.scanAtAngle(scanAngle);
			isObjectAtAngle = obstacle.dist < 80;
			if (!isObjectAtAngle) {
				rightCorner = new PolarPoint(prevDist, scanAngle--);
				break;
			} else if (scanAngle > leftCorner.angle) {
				rightCorner = new PolarPoint(0, leftCorner.angle + 10);
			} else {
				prevDist = obstacle.dist;
			}
		}
		
		}
		boolean isLeftObstacle = Math.abs(leftCorner.angle) > Math.abs(rightCorner.angle);
		boolean isRightObstacle = !isLeftObstacle;

		scanner.scanAtAngle(0);
		
		PolarPoint cornerToAvoid = new PolarPoint(0,0);

		if (isRightObstacle) {
			turnDirection = 1; //Right obstacle -> turn left.
			cornerToAvoid = leftCorner;
			
			Sound.playNote(Sound.PIANO, 200, 5);
			Sound.playNote(Sound.PIANO, 300, 5);
			Sound.playNote(Sound.PIANO, 400, 5);
			Sound.playNote(Sound.PIANO, 800, 5);
		} else {
			turnDirection = -1; //Left obstacle -> turn right.
			cornerToAvoid = rightCorner;
			
			Sound.playNote(Sound.PIANO, 800, 5);
			Sound.playNote(Sound.PIANO, 400, 5);
			Sound.playNote(Sound.PIANO, 300, 5);
			Sound.playNote(Sound.PIANO, 200, 5);
		}

		if (keepAvoiding) {
			mover.turn(turnDirection * 95, false);
			heading += turnDirection * 95;
			mover.travel((cornerToAvoid.dist * Math.abs(Math.sin(Math.toRadians(cornerToAvoid.angle)))) + 15, false);
		}
		
		scanner.scanAtAngle(0);
		mover.turn(-heading, false);
		heading = 0;

	}

}
