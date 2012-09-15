package milestone1;

import java.io.File;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

public class P3M1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub**

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
		}

	}

}
