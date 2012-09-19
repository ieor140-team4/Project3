package essentials;

import java.awt.*;

import lejos.nxt.Button;

public class Navigator {

	private Mover mover;
	private Scanner scanner;
	private double distanceFromLight;
	
	public Navigator(Mover m, Scanner s) {
		mover = m;
		scanner = s;
		distanceFromLight = 20*30.55; // or 0
	}
	
	public void go() {
		for (int i = 0; i < 8; i++) {
			while (distanceFromLight > 40) {
				scanner.scanForData();
				Point lightLocation = scanner.getLightLocation();

				System.out.println("Light location: (" + lightLocation.x + "," + lightLocation.y + ")");

				mover.goToLight(lightLocation.y);
				distanceFromLight = lightLocation.x;
			}

			mover.turnAround();
			distanceFromLight = 20*30.55;
		}
	}

}
