import java.util.ArrayList;
import java.util.Random;


public class TaskDispenser {
	private ArrayList<Integer> tasks;
	private Random rand = new Random();
	
	public TaskDispenser( int n )
	{
		tasks = new ArrayList<Integer>();
		
		for( int o = 0; o < n; o++ )
		{
			tasks.add( o );
		}
		
		synchronized( this )
		{
			this.notifyAll();
		}
	}
	
	public synchronized void AddTask( int n )
	{
		tasks.add( n );
		this.notifyAll();
	}
	
	public synchronized Integer getNext()
	{
		if( tasks.isEmpty() )
		{
			return null;
		}
		
		return tasks.remove(0);
	}
}
