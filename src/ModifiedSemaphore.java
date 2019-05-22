import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ModifiedSemaphore 
{
	private Semaphore semaphore;
	private Queue<Process> queue;
	private int permits;
	public ModifiedSemaphore(int permits)
	{
		semaphore = new Semaphore(permits,true);
		queue = new LinkedList<Process>();
		this.permits=permits;
	}
	public boolean waitMemo(Process P)
	{
		if(semaphore.tryAcquire(P.getMemorySize()))
		{
			return true;
		}
		else
		{
			queue.add(P);
			return false;
		}
	}
	public boolean waitAudio(Process P)
	{
		if(semaphore.tryAcquire(1))
		{
			return true;
		}
		else
		{
			queue.add(P);
			return false;
		}
	}
	public Process siqnalMemo(Process P)
	{
		semaphore.release(P.getMemorySize());
		if(queue.size()>0)
		{
			if(semaphore.tryAcquire(queue.peek().getMemorySize()))
			{
				return queue.remove();
			}
//			if(waitMemo(queue.peek()))
//			{
//				return queue.remove();
//			}
		}
		return null;
	}
	public Process siqnalAudio()
	{
		semaphore.release(1);
		if(queue.size()>0)
		{
			return queue.remove();
//			if(wait(queue.peek()))
//			{
//				return queue.remove();
//			}
		}
		return null;
	}
	public Queue<Process> getQueue() {
		return queue;
	}
	public void setQueue(Queue<Process> queue) {
		this.queue = queue;
	}
	public int availablePermits() {
		return semaphore.availablePermits();
	}
	public void reset() {
		this.semaphore.drainPermits();
		this.semaphore.release(this.permits);
		this.queue = new LinkedList<Process>();
	}
}
