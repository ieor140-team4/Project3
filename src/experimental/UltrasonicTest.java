package experimental;

import java.util.ArrayList;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Datalogger;

public class UltrasonicTest {

	// Instance variables
	private UltrasonicSensor us;
	private NXTRegulatedMotor motor;
	private Datalogger dl;

	public void scanTo(int limitAngle) {
		int oldAngle = motor.getTachoCount();
		motor.rotateTo(limitAngle, true);
		int distance;
		
		while (motor.isMoving())
		{
			short angle = (short) motor.getTachoCount();
			if (Math.abs(angle - oldAngle) > 10)
			{
				distance = us.getDistance();
				oldAngle = angle;
				dl.writeLog(angle);
				dl.writeLog(distance);
				
				System.out.println("Angle" + angle + "-Dist" + distance);
			}

			Thread.yield();
		}
	}
	
	public UltrasonicTest() {
		us = new UltrasonicSensor(SensorPort.S3);
		motor = Motor.B;
		dl = new Datalogger();
	}
	
	public void rotateTo(int angle) {
		motor.rotateTo(angle);
	}

	public static void main(String[] args) {
		UltrasonicTest usr = new UltrasonicTest();
		Button.waitForAnyPress();
		usr.rotateTo(-80);
		usr.scanTo(80);
		usr.scanTo(-80);
		usr.rotateTo(0);
		
		Button.waitForAnyPress();
		
		usr.dl.transmit();
	}

}
