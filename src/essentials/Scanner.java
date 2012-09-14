package essentials;

import java.awt.*;

public class Scanner {

	private ObstacleDetector detector;
	private LightSeeker seeker;
	
	Point[] obstacleLocations;
	
	public void scanForLight() {
		
	}
	
	public void scanForObjects() {
		detector.findObstacles();
		obstacleLocations = detector.getObstacleLocations();
	}
	
	public Point[] getObstacleLocations() {
		return obstacleLocations;
	}

}
