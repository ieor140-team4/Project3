package essentials;

import lejos.nxt.*;
import lejos.robotics.navigation.*;
import java.awt.*;
import java.util.*;

/* The mover class controls the movements of the robot. It communicates with a
 * DifferentialPilot to steer towards light and avoid obstacles. This was done
 * so only one class needs to concern itself with the robot's movements.
 */
public class Mover {
	
	private DifferentialPilot pilot;
	
	public Mover(DifferentialPilot dp) {
		pilot = dp;
	}

	/* Steers the robot towards the given angle. We use a gain constant
	 * of 0.8 which seems to work fine.
	 */
	public void goToLight(int angle) {
		System.out.println("Angle: " + 0.8*angle);
		pilot.steer(0.8 * angle);
	}
	
	//Empty for now.
	public void avoidObstacles(ArrayList<Point> obstacles) {
	}
	
	//Empty for now.
	public void backUpFromObstacle() {
	}
	
	/* Turns the robot 180 degrees so that it can head back towards the other
	 * light. Then begins to go forward so we don't have to wait for a scan
	 * to start moving - this saves a little time.
	 */
	public void turnAround() {
		pilot.rotate(180);
		pilot.steer(0);
	}

}
