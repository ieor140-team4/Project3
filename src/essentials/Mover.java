package essentials;

import lejos.nxt.*;
import lejos.robotics.navigation.*;
import java.awt.*;
import java.util.*;

/**
 * The mover class controls the movements of the robot. It communicates with a
 * DifferentialPilot to steer towards light and avoid obstacles. This was done
 * so only one class needs to concern itself with the robot's movements.
 * 
 * @author nate.kb
 */
public class Mover {

	private DifferentialPilot pilot;

	public Mover(DifferentialPilot dp) {
		pilot = dp;
	}

	/**
	 * Steers the robot towards the given angle. We use a gain constant
	 * of 0.33 which seems to work fine.
	 */
	public void goToLight(int angle) {
		System.out.println("Angle: " + angle);
		pilot.steer(0.33 * angle, angle, true);
	}

	/**
	 * Turns the robot the specified number of degrees.
	 * 
	 * @param angle         the degrees we want to rotate through
	 * @param immediateMove if true, we start moving immediately forward to save time
	 */
	public void turn(int angle, boolean immediateMove) {
		pilot.rotate(angle);
		if (immediateMove) {
			pilot.steer(0);
		}
	}

	/**
	 * Just stops the pilot.
	 * 
	 */
	public void stop() {
		pilot.stop();
	}
	
	/**
	 * A wrapper for pilot's travel method.
	 * 
	 * @param distance the number of centimeters to travel
	 */
	public void travel(double distance) {
		pilot.travel(distance);
	}
	
	/**
	 * A wrapper for pilot's other travel method.
	 * 
	 * @param distance        # cms to travel
	 * @param immediateReturn if yes, return immediately
	 */
	public void travel(double distance, boolean immediateReturn) {
		pilot.travel(distance, immediateReturn);
	}
	
	
	/** A wrapper for the pilot's isMoving() method.
	 * 
	 * @return yes if the robot is moving.
	 */
	public boolean isMoving() {
		return pilot.isMoving();
	}

}
