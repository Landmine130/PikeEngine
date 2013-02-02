package misc;

public class Benchmark {

	private long startTime;
	
	public void startTimer() {
		startTime = System.nanoTime();
	}
	
	public double timeElapsed() {
		return (System.nanoTime() - startTime) / 1000000000.0;
	}
	
	public void printTimeElapsed() {
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
	}
	
}
