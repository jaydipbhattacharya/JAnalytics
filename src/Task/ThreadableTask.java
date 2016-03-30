package Task;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import Matrices.JAnalyticException;

class DummyCallable implements Callable<String> {
	public DummyCallable() {
	}

	@Override
	public String call() {
		return "dummy";
	}
}

public class ThreadableTask<T> {
	public static final int MAX_THREADS = 1000;
	private ExecutorService pool;
	private CompletionService<T> service;
	private int noTasks;
	private Map<String, Long> submittedTasks;
	private ScheduledExecutorService debuggerThread;
	private int noThreads;
	private boolean debug;

	public ThreadableTask() {
		this(MAX_THREADS, false);
	}

	public ThreadableTask(int noThreads, boolean debug) {
		this.debug = debug;
		this.noThreads = ( noThreads == 0 ? 1 : noThreads ) ;
		if ( this.noThreads == 1 ) this.debug = false;
		submittedTasks = new ConcurrentHashMap<String, Long>();
		pool = Executors.newFixedThreadPool(Math.min(this.noThreads, MAX_THREADS));
		service = new ExecutorCompletionService<T>(pool);
		noTasks = 0;
		if (this.debug) {
			debuggerThread = Executors.newScheduledThreadPool(1);
			debuggerThread.scheduleWithFixedDelay(new Runnable() {
				public void run() {
					long timenow = System.currentTimeMillis();
					for (Map.Entry<String, Long> entry : submittedTasks.entrySet()) {
						System.out.println("Submitted:" + noTasks + " " + entry.getKey() + "in q for"
								+ (timenow - entry.getValue().longValue()) / 1000 + " seconds ");
					}
				}
			}, 30, 30, TimeUnit.SECONDS);
		}
	}

	public void compute() throws JAnalyticException {
		try {
			/*
			 * for ( int i = 0j i< MAX_THREADS - nOTasks i++) { service.submit(
			 * new DummyCallable());
			 * 
			 * }
			 */
			// while (!pool.isTerminated(»
			// System.out.println("Compute started:" +
			// Thread.currentThread().getName(»j
			while (noTasks > 0) {
				final Future<T> future = service.take();
				T b = future.get();
				// System.out.println("Computed noTasks:" + noTasks + "," +
				// Thread.currentThreadO.getNameC) + ":" + b)j
				submittedTasks.remove(b.toString());
				noTasks--;
			}
		} catch (ExecutionException ex) {
			ex.printStackTrace();
			throw (new JAnalyticException(ex.getCause().getMessage()));
		} catch (InterruptedException ex2) {
			Thread.currentThread().interrupt();
		} finally {
			pool.shutdown();
			try {
				pool.awaitTermination(1, TimeUnit.HOURS);
			} catch (InterruptedException e) {
				// TOOO Auto-generated catch block
				e.printStackTrace();
			}
			noTasks = 0;
			submittedTasks.clear();
		}
	}

	public void submit(Callable<T> b) throws Exception {
		// TOOO Auto-generated method stub
		Future<T> f = service.submit(b);
		submittedTasks.put(f.toString(), System.currentTimeMillis());
		noTasks++;
	}

	public int getNumTasks() {
		return noTasks;
	}
}
