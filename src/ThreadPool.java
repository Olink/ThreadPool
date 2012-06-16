import java.util.ArrayList;


public class ThreadPool {
	ArrayList<ThreadEx> pool;
	TaskDispenser disp;
	
	boolean running = true;
	int runningThreads = 0;
	
	public ThreadPool( int n )
	{
		pool = new ArrayList<ThreadEx>();
		disp = new TaskDispenser( 30 );
		for( int i = 0; i < n; i++ )
		{
			ThreadEx t = new ThreadEx("Thread " + i, disp);
			t.start();
			pool.add( t );
		}
	}
	
	public void complete()
	{
		running = false;

		for( ThreadEx t : pool )
		{
			t.kill();
		}
		
		synchronized( disp )
		{
			disp.notifyAll();
		}
	}
	
	public static void main( String[] args ) throws InterruptedException
	{
		ThreadPool p = new ThreadPool( 5 );
		
		Thread.sleep( 10000 );
		
		for( int i = 0; i < 50; i++ )
		{
			p.disp.AddTask( i );
		}
		
		
		p.complete();
	}
}
