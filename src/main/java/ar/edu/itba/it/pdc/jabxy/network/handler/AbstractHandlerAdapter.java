package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;

public abstract class AbstractHandlerAdapter implements HandlerAdapter {

	private final Dispatcher dispatcher;
	private final InputQueue inputQueue;
	private OutputQueue outputQueue;
	private final Object stateChangeLock = new Object();
	private EventHandler eventHandler;
	private SelectionKey key = null;
	private SelectableChannel channel;
	private volatile int interestOps = 0;
	private int readyOps = 0;
	private volatile boolean running = false;
	private volatile boolean dead = false;

	public AbstractHandlerAdapter(Dispatcher dispatcher, InputQueue inputQueue,
			OutputQueue outputQueue, EventHandler eventHandler) {
		this.dispatcher = dispatcher;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
		this.eventHandler = eventHandler;
	}

	@Override
	public abstract HandlerAdapter call() throws Exception;

	@Override
	public InputQueue inputQueue() {
		return this.inputQueue;
	}

	@Override
	public OutputQueue outputQueue() {
		return this.outputQueue;
	}

	@Override
	public void setHandler(EventHandler handler) {
		this.eventHandler = handler;
	}

	@Override
	public int getInterestOps() {
		return interestOps;
	}

	@Override
	public void modifyInterestOps(int opsToSet, int opsToReset) {
		synchronized (stateChangeLock) {
			interestOps = (interestOps | opsToSet) & (~opsToReset);

			if (!running) {
				dispatcher.enqueueStatusChange(this);
			}
		}
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
		setChannel(key.channel());
		interestOps = key.interestOps();
	}

	@Override
	public SelectionKey key() {
		return key;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void registering() {
		eventHandler.starting(this);
	}

	@Override
	public void registered() {
		eventHandler.started(this);

	}

	@Override
	public void unregistering() {
		eventHandler.stopping(this);

	}

	@Override
	public void unregistered() {
		eventHandler.stopped(this);

	}

	@Override
	public void die() {
		this.dead = true;
	}

	public SelectableChannel getChannel() {
		return channel;
	}

	public void setChannel(SelectableChannel channel) {
		this.channel = channel;
	}
	
	public int getReadyOps() {
		return readyOps;
	}

	public void setOutputQueue(OutputQueue outputQueue){
		this.outputQueue = outputQueue;
	}
	
}
