package EarthSim;

public interface CmdListenable {

	public String poll();
	public String take(); //blocks
	
}
