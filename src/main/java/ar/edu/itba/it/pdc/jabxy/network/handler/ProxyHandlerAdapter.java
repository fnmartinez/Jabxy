package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.InputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.OutputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class ProxyHandlerAdapter implements HandlerAdapter{
	
	private final Dispatcher dispatcher;
	private final InputQueue inputQueue;
	private final OutputQueue outputQueue;
	private final Object stateChangeLock = new Object();
	private EventHandler clientHandler;
	private SelectionKey key = null;
	private SelectableChannel inputChannel = null;
	private SelectableChannel outputChannel = null;
	private SelectableChannel channel = null;
	private volatile int interestOps = 0;
	private int readyOps = 0;
	private boolean shuttingDown = false;
	private volatile boolean running = false;
	private volatile boolean dead = false;

	public ProxyHandlerAdapter(Dispatcher dispatcher, BufferFactory bufferFactory, EventHandler eventHandler) {
		this.dispatcher = dispatcher;
		this.inputQueue = new InputQueueImpl(bufferFactory);
		this.outputQueue = new OutputQueueImpl(bufferFactory, this);
		this.clientHandler = eventHandler;
	}

	public ProxyHandlerAdapter(EventHandler handler,
			Dispatcher dispatcher, BufferFactory bufferFactory) {
		// TODO Auto-generated constructor stub
		this.dispatcher = dispatcher;
		this.clientHandler = handler;
		this.inputQueue = new InputQueueImpl(bufferFactory);
		this.outputQueue = new OutputQueueImpl(bufferFactory, this);
		
		
	}

	@Override
	public HandlerAdapter call() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputQueue inputQueue() {
		return this.inputQueue;
	}

	@Override
	public OutputQueue outputQueue() {
		return this.outputQueue;
	}

	@Override
	public void modifyInterestOps(int opsToSet, int opsToReset) {
		// TODO Auto-generated method stub
		synchronized (stateChangeLock) {
			this.interestOps = (interestOps | opsToSet) & ~opsToReset;
			
			if (!running) {
				dispatcher.enqueueStatusChange(this);
			}
		}
	}

	@Override
	public int getInterestOps() {
		return interestOps;
	}

	@Override
	public void setHandler(EventHandler handler) {
		this.clientHandler = handler;
	}

	@Override
	public void prepareToRun() {
		synchronized (stateChangeLock) {
			interestOps = key.interestOps();
			readyOps = key.readyOps();
			running = true;
		}
	}

	@Override
	public void setKey(SelectionKey key) {
		this.key = key;
		this.channel = key.channel();
		this.interestOps = key.interestOps();
	}

	@Override
	public SelectionKey key() {
		return this.key;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void registering() {
		clientHandler.starting(this);		
	}

	@Override
	public void registered() {
		clientHandler.started(this);
	}

	@Override
	public void unregistering() {
		clientHandler.stopping(this);
	}

	@Override
	public void unregistered() {
		clientHandler.stopped(this);
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	public ProxyHandlerAdapter getSibling() {
		// TODO Auto-generated method stub
		return null;
	}

}
