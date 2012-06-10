
public class ThreadEx extends Thread{
	ThreadPool pool;
	Object wait;
	int index;
	boolean running = true;
	int count;
	
	public ThreadEx( ThreadPool pool, int index )
	{
		this.pool = pool;
		this.index = index;
		wait = new Object();
	}
	
	public void execute( int c )
	{
		count = c;
		synchronized( pool.active )
		{
			pool.active[index] = true;
		}
		
		synchronized( wait )
		{
			wait.notifyAll();
		}
	}
	
	public void complete()
	{
		running = false;
		synchronized( wait )
		{
			wait.notify();
		}
	}
	public void run()
	{
		while( running )
		{
			synchronized( wait )
			{
				try {
					wait.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.err.println( "Test Thread( " + index + " ) waking up ( " + count + " )...");
			if( !running )
				return;
			
			System.err.println( "Thread( " + index + " ) executing ( " + count + " )...");
			for( int i = 0;i <= 10; i++ )
			{
				System.out.println(index + ": " + i);
			}
			
			Object lock = pool.lock();
			
			synchronized( pool.active )
			{
				pool.active[index] = false;
			}
			
			synchronized( lock )
			{
				lock.notifyAll();
			}
			
			System.err.println( "Thread( " + index + " ) finishing ( " + count + " )...");
		}
	}
}
