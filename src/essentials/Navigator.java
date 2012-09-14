package essentials;

public class Navigator {

	private Mover mover;
	private Scanner scanner;
	private double distanceFromLight;
	
	public Navigator(Mover m, Scanner s) {
		mover = m;
		scanner = s;
		distanceFromLight = 20; // or 0
	}
	
	public void go() {
		// continually calls Scanner to get feedback
		// calls Mover to move

	}

}
