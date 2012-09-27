package essentials;

import java.awt.Point;
import java.util.ArrayList;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

/**
 * 
 * @author nate.kb
 *
 */
public class ObstacleDetector {

	private TouchSensor leftBumper;
	private TouchSensor rightBumper;
	private UltrasonicSensor sensor;

	private PolarPoint obstacleLocation;
	private ObstacleListener obstacleListener;

	public ObstacleDetector(SensorPort lbPort, SensorPort rbPort, SensorPort usPort) {
		leftBumper = new TouchSensor(lbPort);
		rightBumper = new TouchSensor(rbPort);
		sensor = new UltrasonicSensor(usPort);

		lbPort.addSensorPortListener(new TouchDetectorListener(true));
		rbPort.addSensorPortListener(new TouchDetectorListener(false));
	}

	/**
	 * Looks to see if there is an obstacle in the path of the robot.
	 * <p>
	 * Upon finding an object within the specified distance and angle
	 * thresholds (25 cm in front of the robot and within 15 degrees
	 * of the head), it calls any attached obstacle listeners' obstacle
	 * found methods.
	 * 
	 * @param angle the angle that the head is pointing at right now.
	 */
	public void findObstacles(int angle) {
		int distToObstacle = sensor.getDistance();

		PolarPoint obstacle = new PolarPoint(distToObstacle, angle);

		int distThreshold = 25;
		int angleThreshold = 15;

		if ((obstacle.dist < distThreshold) && 
				(Math.abs(obstacle.angle) < angleThreshold)) {
			obstacleLocation = obstacle;

			Sound.playNote(Sound.PIANO, 420, 50);
			if (obstacleListener != null) {
				obstacleListener.objectFound(obstacleLocation);
			}
		}
	}

	/**
	 *  
	 * @return The stored obstacle's location. Only stored if within dist/angle thresholds.
	 */
	public PolarPoint getObstacleLocation() {
		return obstacleLocation;
	}

	/**
	 * Attaches an obstacle listener to the obstacle detector.
	 * 
	 * @param listener The listener we want to attach.
	 */
	public void setObstacleListener(ObstacleListener listener) {
		obstacleListener = listener;
	}

	/**
	 * This is a listener that waits for a press on one of the touch sensors.
	 * Then it tells the obstacleListener where that obstacle is located and
	 * calls its obstacleFound method.
	 * 
	 * @author nate.kb
	 *
	 */
	private class TouchDetectorListener implements SensorPortListener {

		private boolean isLeft;

		/**
		 * 
		 * @param left True if it's the left touch sensor, false if it's the right.
		 */
		public TouchDetectorListener(boolean left) {
			isLeft = left;
		}

		/**
		 * When the touch sensor becomes pressed, it passes the information
		 * to the obstacleListener if there is one attached.
		 * 
		 */
		public void stateChanged(SensorPort port, int aOldValue, int aNewValue) {
			if ((aNewValue < 190) && (aOldValue > 190)) {
				System.out.println("Alert! " + aOldValue + " " + aNewValue);
				Sound.playNote(Sound.PIANO, 400, 50);

				if (obstacleListener != null) {
					if (isLeft) {
						obstacleLocation = new PolarPoint((int) Math.round(7 * 2.54), 25);
					} else {
						obstacleLocation = new PolarPoint((int) Math.round(7 * 2.54), -25);
					}
					obstacleListener.objectFound(obstacleLocation);
				}


			} else if ((aNewValue > 1000) && (aOldValue > 0)) {
				System.out.println("Released!" + aOldValue + " " + aNewValue);
				Sound.playNote(Sound.PIANO, 550, 50);
			}
		}

	}

}
