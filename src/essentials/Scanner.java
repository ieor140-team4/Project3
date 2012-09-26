package essentials;

import java.awt.*;
import lejos.nxt.*;
import java.util.ArrayList;

public class Scanner {

	/* The scanner has access to the entire rotating sensor apparatus,
	 * so as to allow us to control the entire apparatus and get many
	 * kinds of data from it, as opposed to needing the light sensors
	 * and the ultrasonic sensors to each take control of the motor.
	 */
	private ObstacleDetector detector;
	private LightSensor sensor;
	private NXTRegulatedMotor motor;

	/* Each point is a polar point, with direction as the x
	 * and angle as the y.
	 */
	private int highestLightValue;
	private PolarPoint lightLocation;
	
	public Scanner(NXTRegulatedMotor scannerMotor, LightSensor ls) {
		sensor = ls;
		motor = scannerMotor;
		highestLightValue = 0;
	}
	
	/* Scans for data, which should include both light sources and obstacles.
	 * Rotates from -40 to 40, which should be enough to find the light source
	 * from anywhere on the course, as well as detect obstacles ahead of us.
	 */
	public void scanForData() {
		
		/* On each scan, we need to reset the highest light value so we
		 * don't keep track of what we saw last scan.
		 */
		highestLightValue = 0;
		
		/* This code is mostly copied from the ScanRecorder class in the
		 * experimental work. We rotate from -40 to 40 and at each angle
		 * we scan for the light source and any objects.
		 */
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
	
	/* Gets the light value from the sensor and overwrites the saved
	 * light value if we've found a higher one.
	 */
	public void scanForLight(int angle) {
		int lv = sensor.getLightValue();
		if (lv > highestLightValue) {
			highestLightValue = lv;
			double distance = 3385 * Math.exp(-0.06 * lv); //From regression equation.
			lightLocation = new PolarPoint((int) Math.round(distance), angle);
		}
	}
	
	//Empty for now.
	public void scanForObjects(int angle) {
		detector.findObstacles(angle);
	}
	
	/* Transforms the light reading into a distance using a 
	 * regression equation we figured out in the experimental work. Then
	 * returns a polar point that gives distance (in cm) and angle to
	 * the light source.
	 */
	public PolarPoint getLightLocation() {
		return lightLocation;
	}
	
	public void addDetector(ObstacleDetector d) {
		detector = d;
	}

}
