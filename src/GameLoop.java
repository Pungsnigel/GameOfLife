
public class GameLoop implements Runnable{

	private GameBoard board;
	private boolean running;
	private int sleepTime;
	Thread thread;

	public GameLoop (GameBoard board){
		this.board = board;
		thread = new Thread(this);
		this.sleepTime = 200;
		thread.start();
	}

	public void stop(){
		this.running = false;
	}
	
	public void setSleepTime(int sleepTime){
		this.sleepTime = sleepTime;
	}
	

	@Override
	public void run() {
		this.running = true;
		while(running){
			board.doTurn();
			try{
				Thread.sleep(sleepTime);
			}catch (InterruptedException ex){
			}
		}
	}

}
