package EarthSim;

public interface SimCmdReceivable {
	public abstract void start() throws InterruptedException;
	public abstract void stop() throws InterruptedException;
	public abstract void pause() throws InterruptedException; 	
//	public abstract void clear() throws InterruptedException;
	public abstract void resume() throws InterruptedException;
//	public abstract void getCmd() throws InterruptedException;
	public abstract void init();
}
