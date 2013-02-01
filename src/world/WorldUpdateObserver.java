package world;

public interface WorldUpdateObserver {
	
	public void worldUpdated(World w, double timeElapsed);
	public void worldStarted(World w);
	public void worldPaused(World w);
	public void worldResumed(World w);
	public void worldClosed(World w);
}
