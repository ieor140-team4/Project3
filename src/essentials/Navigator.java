package essentials;

import java.awt.*;

import lejos.nxt.Button;

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


	public Navigator(Mover m, Scanner s, ObstacleDetector d) {
		mover = m;
		detector = d;
		scanner = s;
		s.addDetector(d);
		d.setObstacleListener(this);
		distanceFromLight = 20*30.55; // or 0
		obstacle = false;
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
					scanner.scanForData();
					PolarPoint lightLocation = scanner.getLightLocation();

					System.out.println("Light @ " + lightLocation.toString());

					if (!obstacle) {
						mover.goToLight(lightLocation.angle);
					}
					distanceFromLight = lightLocation.dist;
				}
			}

			mover.turnAround();
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
		mover.stop();
		
		mover.backUpFromObstacle();
		
		System.out.println("Waiting for button to be pressed...");
		Button.waitForAnyPress();
		
		obstacle = false;
	}

}
