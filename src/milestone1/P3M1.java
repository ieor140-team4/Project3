package milestone1;

import java.io.File;
import essentials.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import lejos.nxt.*;

public class P3M1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub**

		//Configure the Differential Pilot using the measurements of our robot.
		double trackWidth = 9.2 + 2.6;
		double wheelDiam = 5.56;
		DifferentialPilot pilot = new DifferentialPilot(wheelDiam,trackWidth,Motor.A,Motor.C); //need to fix
		pilot.setTravelSpeed(40);

		NXTRegulatedMotor sensorMotor = Motor.B;
		LightSensor lightSensor = new LightSensor(SensorPort.S2);
		lightSensor.setFloodlight(false);

		Mover mover = new Mover(pilot);
		Scanner scanner = new Scanner(sensorMotor, lightSensor);

		ObstacleDetector detector = new ObstacleDetector(SensorPort.S4, SensorPort.S1, SensorPort.S3);
		Avoider avoider = new Avoider(mover, scanner, detector);
		
		Navigator navigator = new Navigator(mover, scanner, detector);


		Button.waitForAnyPress();
		navigator.go(2);
		sensorMotor.rotateTo(0);

		/*
		TouchSensor ts1 = new TouchSensor(SensorPort.S1);
		TouchSensor ts2 = new TouchSensor(SensorPort.S4);

		Sound.setVolume(100);

		while (true) {
			if (ts1.isPressed()) {
				// Sound.playNote(Sound.FLUTE, 262, 200);
				while (true) {
					Sound.playSample(new File("MudkipClip.wav"), 100);
					Delay.msDelay(2120);
				}
			}
			if (ts2.isPressed()) {
				Sound.playNote(Sound.XYLOPHONE, 294, 200);
			}
			if (Motor.A.isMoving()) {
				Sound.playNote(Sound.PIANO, 330, 200);
			}
			if (Motor.B.isMoving()) {
				Sound.playNote(Sound.PIANO, 349, 200);
			}
			if (Button.LEFT.isDown()) {
				Sound.playNote(Sound.PIANO, 392, 200);
			}
			if (Button.RIGHT.isDown()) {
				Sound.playNote(Sound.PIANO, 440, 200);
			}

		}*/

	}

}
