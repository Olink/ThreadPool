import java.util.Random;


public class ThreadEx extends Thread{
	private boolean running = true;
	private TaskDispenser dispenser;
	
	public ThreadEx( String name, TaskDispenser d )
	{
		super( name );
		dispenser = d;
	}
	
	public void kill()
	{
		running = false;
	}
	
	public void run()
	{
		Integer t = dispenser.getNext();
		while( running || t != null )
		{
			if( t != null )
			{
				System.out.println( this.getName() + ": " + t);
				try {
					Thread.sleep( new Random().nextInt(500)+500 );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				synchronized( dispenser )
				{
					try {
						dispenser.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			t = dispenser.getNext();
		}
	}
}
