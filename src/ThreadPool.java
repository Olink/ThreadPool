import java.util.ArrayList;


public class ThreadPool {
	ArrayList<ThreadEx> pool;
	boolean[] active;
	Object lock;
	boolean running = true;
	
	public ThreadPool( int n )
	{
		pool = new ArrayList<ThreadEx>();
		active = new boolean[n];
		lock = new Object();
		
		for( int i = 0; i < n; i++ )
		{
			ThreadEx t = new ThreadEx(this, i);
			t.start();
			pool.add( t );
			active[i] = false;
		}
	}
	
	public Object lock()
	{
		return lock;
	}
	
	public ThreadEx getThread()
	{
		while( running )
		{
			for( int i = 0; i < active.length; i++ )
			{
				synchronized( active )
				{
					if( !active[i] )
					{
						return pool.get( i );
					}
				}
			}
			synchronized( lock )
			{
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public void complete()
	{
		running = false;
		synchronized( lock )
		{
			lock.notify();
		}
		
		for( ThreadEx t : pool )
		{
			t.complete();
		}
	}
	
	public static void main( String[] args ) throws InterruptedException
	{
		ThreadPool p = new ThreadPool( 5 );
		Thread.sleep( 2000 );
		for( int i = 0; i < 1000; i++)
		{
			p.getThread().execute( i );
		}
		
		Thread.sleep( 5000 );
		p.complete();
	}
}
