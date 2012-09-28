package essentials;

import java.awt.*;
import lejos.nxt.*;
import java.util.ArrayList;

/**
 * The scanner has access to the entire rotating sensor apparatus,
 * so as to allow us to control the entire apparatus and get many
 * kinds of data from it, as opposed to needing the light sensors
 * and the ultrasonic sensors to each take control of the motor.
 * <p>
 * It controls the light sensor directly, and calls the ultrasonic sensor
 * indirectly through ObstacleDetector, which keeps track of its readings
 * by itself.
 * <p>
 * The scanner uses the rotating assembly to sweep back and forth, scanning
 * for both light and objects in the path.
 * 
 * @author nate.kb
 */
public class Scanner {

	private ObstacleDetector detector;
	private LightSensor sensor;
	private NXTRegulatedMotor motor;

	private int highestLightValue;
	private PolarPoint lightLocation;
	
	public Scanner(NXTRegulatedMotor scannerMotor, LightSensor ls) {
		sensor = ls;
		motor = scannerMotor;
		highestLightValue = 0;
	}
	
	/**
	 * Scans for data, which should include both light sources and obstacles.
	 * Rotates from -30 to 30, which should be enough to find the light source
	 * from anywhere on the course, as well as detect obstacles ahead of us.
	 * <p>
	 * However, doing such a large sweep may decrease performance as it takes longer
	 * to complete each scan.
	 * 
	 * @param startAngle the angle that the scan starts from. -90 = 90 degrees right of forward
	 * @param scanAngle  the angle that the scan rotates through
	 */
	public void scanForData(int startAngle, int scanAngle) {
		
		/* 
		 * On each scan, we need to reset the highest light value so we
		 * don't keep track of what we saw last scan.
		 */
		highestLightValue = 0;
		
		/* 
		 * This code is mostly copied from the ScanRecorder class in the
		 * experimental work. We rotate from the start angle throuhg the
		 * scan angle and at each angle we scan for the light source and
		 * any objects.
		 */
		int oldAngle = motor.getTachoCount();
		
		motor.rotateTo(startAngle);
		motor.rotate(scanAngle, true);
		while (motor.isMoving()) {
	         short angle = (short) motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	        	 scanForLight(angle);
	        	 scanForObjects(angle);
	         }
		}
		motor.rotate(-scanAngle, true);
		while (motor.isMoving()) {
	         short angle = (short) motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	        	 scanForLight(angle);
	        	 scanForObjects(angle);
	         }
		}
		
	}
	
	/**
	 * Gets the light value from the sensor and overwrites the saved
	 * light value if we've found a higher one.
	 * 
	 * @param  angle the angle at which the scanner is pointing right now
	 *  
	 */
	public void scanForLight(int angle) {
		int lv = sensor.getLightValue();
		if (lv > highestLightValue) {
			highestLightValue = lv;
			double distance = 3385 * Math.exp(-0.06 * lv); //From regression equation.
			lightLocation = new PolarPoint((int) Math.round(distance), angle);
		}
	}
	
	/**
	 * Tells the obstacle detector to find any obstacles that we're looking at.
	 * 
	 * @param angle the angle the robot is currently facing towards
	 */
	public void scanForObjects(int angle) {
		int distThreshold = 30;
		int angleThreshold = 20;
		detector.findObstacles(angle, distThreshold, angleThreshold);
	}
	
	/**
	 * Transforms the light reading into a distance using a 
	 * regression equation we figured out in the experimental work. Then
	 * returns a polar point that gives distance (in cm) and angle to
	 * the light source.
	 * 
	 * @return the location of the light in polar coordinates from the front of the robot
	 * @see    PolarPoint
	 */
	public PolarPoint getLightLocation() {
		return lightLocation;
	}
	
	/**
	 * Gives the Scanner an obstacleDetector which it uses to scan for
	 * obstacles in the path of the robot.
	 * 
	 * @param d the obstacle detector we add
	 */
	public void addDetector(ObstacleDetector d) {
		detector = d;
	}

}
