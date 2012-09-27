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
	 * Travels 10 centimeters backwards.
	 * 
	 */
	public void backUpFromObstacle() {
		pilot.travel(-10);
	}
	
	/**
	 * Turns the robot 180 degrees so that it can head back towards the other
	 * light. Then begins to go forward so we don't have to wait for a scan
	 * to start moving - this saves a little time.
	 */
	public void turnAround() {
		pilot.rotate(200);
		pilot.steer(0);
	}
	
	/**
	 * Just stops the pilot.
	 * 
	 */
	public void stop() {
		pilot.stop();
	}

}
