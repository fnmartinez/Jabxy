package ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.SelectorGuard;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerFutureTask;
import ar.edu.itba.it.pdc.jabxy.network.handler.ServerHandlerAdapter;

public abstract class AbstractNioDispatcher implements Dispatcher, Runnable {
	// TODO: revisar la posibilidad de cambiar a AtomicReference el Selector
	// para evitar el SelectorGuard
	private final Logger logger = Logger.getLogger(getClass().getName());
	private final Executor executor;
	private final Selector selector;
	private final BlockingQueue<HandlerAdapter> statusChangeQueue;
	private final SelectorGuard guard;
	private volatile boolean dispatching = true;
	
	public AbstractNioDispatcher(Executor executor, SelectorGuard guard) throws IOException{
		this.executor = executor;
		this.guard = guard;
		
		statusChangeQueue = new ArrayBlockingQueue<HandlerAdapter>(100);
		selector = Selector.open();
		this.guard.setSelector(this.selector);
	}

	@Override
	public void dispatch() throws IOException {
		while (dispatching) {
			guard.selectorBarrier();

			selector.select();

			checkStatusChangeQueue();

			Set<SelectionKey> keys = selector.selectedKeys();

			for (SelectionKey key : keys) {
				ServerHandlerAdapter adapter = (ServerHandlerAdapter) key.attachment();

				invokeHandler(adapter);
			}

			keys.clear();
		}
	}

	@Override
	public void shutdown() {
		dispatching = false;

		selector.wakeup();
	}

	@Override
	public void enqueueStatusChange(HandlerAdapter adapter) {
		boolean interrupted = false;

		try {
			while (true) {
				try {
					statusChangeQueue.put(adapter);
					selector.wakeup();
					return;
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		} finally {
			if (interrupted)
				Thread.currentThread().interrupt();
		}
	}

	@Override
	public void run() {
		try {
			dispatch();
		} catch (IOException e) {
			logger.error("Unexpected I/O Exception", e);
		}

		Set<SelectionKey> keys = selector.selectedKeys();

		for (SelectionKey key : keys) {
			ServerHandlerAdapter adapter = (ServerHandlerAdapter) key.attachment();

			unregisterChannel(adapter);
		}

		try {
			selector.close();
		} catch (IOException e) {
			logger.error("Unexpected I/O Exception closing selector", e);
		}
	}

	private void invokeHandler(ServerHandlerAdapter adapter) {
		adapter.prepareToRun();
		adapter.key().interestOps(0);

		executor.execute(new HandlerFutureTask(adapter, this));
	}

	private void checkStatusChangeQueue() {
		HandlerAdapter adapter;

		while ((adapter = statusChangeQueue.poll()) != null) {
			if (adapter.isDead()) {
				unregisterChannel(adapter);
			} else {
				resumeSelection(adapter);
			}
		}
	}

	private void resumeSelection(HandlerAdapter adapter) {
		SelectionKey key = adapter.key();

		if (key.isValid())
			key.interestOps(adapter.getInterestOps());
	}

	@Override
	public Thread start() {
		Thread thread = new Thread(this);

		thread.start();

		return thread;
	}
	
	public Selector getSelector() {
		return this.selector;
	}
	
	void acquireSelector() {
		guard.acquireSelector();
	}
	
	void releaseSelector() {
		guard.releaseSelector();
	}
}
