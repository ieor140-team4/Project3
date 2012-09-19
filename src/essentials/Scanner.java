package essentials;

import java.awt.*;
import lejos.nxt.*;
import java.util.ArrayList;

public class Scanner {

	private ObstacleDetector detector;
	
	private LightSensor sensor;
	private NXTRegulatedMotor motor;

	/* Each point is a polar point, with direction as the x
	 * and angle as the y.
	 */
	private ArrayList<Point> obstacleLocations;
	private Point highestLightValue;
	
	public Scanner(NXTRegulatedMotor scannerMotor, LightSensor ls) {
		detector = new ObstacleDetector();
		sensor = ls;
		motor = scannerMotor;
		obstacleLocations = new ArrayList<Point>();
		highestLightValue = new Point(0,0);
	}
	
	/* Scans for data, which should include both light sources and obstacles.
	 * Giving a center of 0 and a sweep angle of 180 will result in it scanning
	 * from -90 to 90. A center of 45 and a sweep angle of 90 will scan from
	 * 0 to 90, etc.
	 */
	public void scanForData() {
		highestLightValue = new Point(0,0);
		
		int startAngle = -40;
		
		int oldAngle = motor.getTachoCount();
		
		motor.rotateTo(startAngle);
		motor.rotate(80, true);
		while (motor.isMoving()) {
	         short angle = (short) motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	        	 scanForLight(angle);
	        	 scanForObjects(angle);
	         }
		}
		motor.rotate(-80, true);
		while (motor.isMoving()) {
	         short angle = (short) motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	        	 scanForLight(angle);
	        	 scanForObjects(angle);
	         }
		}
		
	}
	
	public void scanForLight(int angle) {
		int lv = sensor.getLightValue();
		if (lv > highestLightValue.x) {
			highestLightValue = new Point(lv, angle);
		}
	}
	
	public void scanForObjects(int angle) {
		detector.findObstacles();
		obstacleLocations = detector.getObstacleLocations();
	}
	
	public ArrayList<Point> getObstacleLocations() {
		return obstacleLocations;
	}
	
	public Point getLightLocation() {
		double distance = 3385 * Math.exp(-0.06 * highestLightValue.x); //Our regression equation
		
		Point result = new Point((int) distance, highestLightValue.y);
		
		return result;
	}

}
