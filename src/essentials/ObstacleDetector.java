package essentials;

import java.awt.Point;
import java.util.ArrayList;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

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

	public void findObstacles(int angle) {
		int distToObstacle = sensor.getDistance();

		PolarPoint obstacle = new PolarPoint(distToObstacle, angle);

		if (obstacle.dist < 10) {
			obstacleLocation = obstacle;
		}
	}

	public PolarPoint getObstacleLocation() {
		return obstacleLocation;
	}
	
	public void setObstacleListener(ObstacleListener listener) {
		obstacleListener = listener;
	}

	private class TouchDetectorListener implements SensorPortListener {

		private boolean isLeft;

		public TouchDetectorListener(boolean left) {
			isLeft = left;
		}

		public void stateChanged(SensorPort port, int aOldValue, int aNewValue) {
			if (aNewValue < 190) {
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
				
				
			} else if (aNewValue > 1000) {
				System.out.println("Released!" + aOldValue + " " + aNewValue);
				Sound.playNote(Sound.PIANO, 550, 50);
				
				obstacleListener.objectReleased();
			} else {
				System.out.println("Not quite alert?");
			}
		}

	}
	
	private class SonicDetectorListener implements SensorPortListener {
		
		public void stateChanged(SensorPort port, int aOldValue, int aNewValue) {
			
		}
	}

}
