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

	private ArrayList<Point> obstacleLocations;

	public ObstacleDetector(SensorPort lbPort, SensorPort rbPort, SensorPort usPort) {
		leftBumper = new TouchSensor(lbPort);
		rightBumper = new TouchSensor(rbPort);
		sensor = new UltrasonicSensor(usPort);

		lbPort.addSensorPortListener(new DetectorListener(true));
		rbPort.addSensorPortListener(new DetectorListener(false));
	}

	public boolean waitForRightPress() {
		return false;
	}

	public boolean waitForLeftPress() {
		return false;
	}

	public void findObstacles(int angle) {
		int distToObstacle = sensor.getDistance();

		Point obstacle = new Point(distToObstacle, angle);

		for (int i = 0; i < obstacleLocations.size(); i++) {
			if (Math.abs(obstacleLocations.get(i).y - angle) >= 5) {
				obstacleLocations.add(obstacle);
			}
		}
	}

	public ArrayList<Point> getObstacleLocations() {
		return obstacleLocations;
	}

	private class DetectorListener implements SensorPortListener {

		private boolean isLeft;

		public DetectorListener(boolean left) {
			isLeft = left;
		}

		public void stateChanged(SensorPort port, int aOldValue, int aNewValue) {
			if (aNewValue < 190) {
				System.out.println("Alert! " + aOldValue + " " + aNewValue);
				Sound.playNote(Sound.PIANO, 400, 1000);
			} else if (aNewValue > 1000) {
				System.out.println("Released!" + aOldValue + " " + aNewValue);
				Sound.playNote(Sound.PIANO, 550, 1000);
			} else {
				System.out.println("Not quite alert?");
			}
		}

	}

}
