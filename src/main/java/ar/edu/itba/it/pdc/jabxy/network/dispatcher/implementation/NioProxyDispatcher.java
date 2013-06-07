package ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.SelectorGuard;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.ProxyHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.ServerHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public class NioProxyDispatcher implements Dispatcher, Runnable {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	private final Executor executor;
	private final Selector selector;
	private final BlockingQueue<HandlerAdapter> statusChangeQueue;
	private final BufferFactory bufferFactory;
	private final SelectorGuard guard;
	private volatile boolean dispatching = true;
	
	public NioProxyDispatcher(Executor executor, BufferFactory bufferFacotry, SelectorGuard guard) throws IOException{
		this.executor = executor;
		this.bufferFactory = bufferFacotry;
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

	private void invokeHandler(ServerHandlerAdapter adapter) {
		// TODO Auto-generated method stub
		
	}

	private void checkStatusChangeQueue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public ChannelFacade registerChannel(SelectableChannel channel,
			EventHandler handler) throws IOException {
		channel.configureBlocking(false);

		ProxyHandlerAdapter clientAdapter = new ProxyHandlerAdapter(handler, this,
				bufferFactory);

		ProxyHandlerAdapter serverAdapter = clientAdapter.getSibling();
		
		serverAdapter.registering();
		clientAdapter.registering();

		guard.acquireSelector();

		try {
			SelectionKey key = channel.register(selector, SelectionKey.OP_READ,
					clientAdapter);

			clientAdapter.setKey(key);
			clientAdapter.registered();

			return clientAdapter;
		} finally {
			guard.releaseSelector();
		}
	}

	@Override
	public void unregisterChannel(ChannelFacade key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enqueueStatusChange(HandlerAdapter adapter) {
		// TODO Auto-generated method stub

	}

	@Override
	public Thread start() {
		// TODO Auto-generated method stub
		return null;
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

}
