package essentials;

import lejos.nxt.*;
import lejos.robotics.navigation.*;
import java.awt.*;
import java.util.*;

public class Mover {
	
	private DifferentialPilot pilot;
	
	public Mover(DifferentialPilot dp) {
		pilot = dp;
	}

	public void goToLight(int angle) {
		System.out.println("Angle: " + 0.1*angle);
		pilot.steer(0.8 * angle);
	}
	
	public void avoidObstacles(ArrayList<Point> obstacles) {
	}
	
	public void backUpFromObstacle() {
	}
	
	public void turnAround() {
		pilot.rotate(180);
		pilot.steer(0);
	}

}
