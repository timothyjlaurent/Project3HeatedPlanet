package EarthSim;

import java.util.concurrent.SynchronousQueue;

/**
 * 
 */
public class SimCmdMsg implements SimCmdReceivable, CmdListenable{

	/**
	 * 
	 */
	public SynchronousQueue<String> messages = new SynchronousQueue<String>();
	private boolean stop;
	private boolean delivering;
	
	/**
	 * 
	 */
	public SimCmdMsg() {
	}
	
	public synchronized void deliverMsg(String msg){
		
	}
	
	public synchronized void start() {
			messages.clear();
	
			
			// TODO-TIM this may be a useful pattern:: 
			if (!delivering){
			new Thread(new Runnable(){
			     
				public void run(){
					delivering = true;
					try {
						messages.put("start");
						delivering = false;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					delivering = false;
				}
			}).start();
			}
		
	}
	
	public synchronized void stop(){
		messages.clear();
		try {
			messages.put("stop");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
	}

	public synchronized void pause(){
		messages.clear();
		try {
			messages.put("pause");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
	}
	
	public synchronized void resume(){
		messages.clear();
		try {
			messages.put("resume");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
	}
	

	public void init(){
		
	}

	@Override
	public String poll() {
		// TODO Auto-generated method stub
		return messages.poll();
	}

	@Override
	public String take() {
		// TODO Auto-generated method stub
		try {
			return messages.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}