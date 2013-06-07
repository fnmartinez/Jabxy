package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;

public class HandlerFutureTask extends FutureTask<HandlerAdapter> implements Runnable {

	private final HandlerAdapter adapter;
	private Logger logger = Logger.getLogger(HandlerFutureTask.class);
	private Dispatcher dispatcher;

	public HandlerFutureTask (HandlerAdapter adapter, Dispatcher dispatcher)
	{
		super (adapter);
		this.adapter = adapter;
		this.dispatcher = dispatcher;
	}

	protected void done()
	{
		dispatcher.enqueueStatusChange(adapter);

		try {
			// Get result returned by call(), or cause
			// deferred exception to be thrown.  We know
			// the result will be the adapter instance
			// stored above, so we ignore it.
			get();

		// Extension point: You may choose to extend the
		// InputHandler and HandlerAdapter classes to add
		// methods for handling these exceptions.  This
		// method is still running in the worker thread.
		} catch (ExecutionException e) {
			adapter.die();
			logger.error("Handler died", e.getCause());
		} catch (InterruptedException e) {
			Thread.interrupted();
			logger.error("Handler interrupted", e);
		}
	}

}
