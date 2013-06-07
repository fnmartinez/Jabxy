package ar.edu.itba.it.pdc.jabxy.network.acceptor.implementations;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import ar.edu.itba.it.pdc.jabxy.network.acceptor.Acceptor;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandlerFactory;

/**
 * Acceptor implementation for a single dispatcher type.
 * 
 */
public class BasicAcceptor implements Acceptor {

	private final Dispatcher dispatcher;
	private final EventHandlerFactory eventHandlerFactory;
	private final ServerSocketChannel listenSocket;
	private final Listener listener;
	private final List<Thread> threads = new ArrayList<Thread>();
	private Logger logger = Logger.getLogger(getClass().getName());
	private volatile boolean running = true;

	public BasicAcceptor(int port,
			EventHandlerFactory eventHandlerFactory, Dispatcher dispatcher) throws IOException {
		this.dispatcher = dispatcher;
		this.eventHandlerFactory = eventHandlerFactory;
		this.listenSocket = ServerSocketChannel.open();
		this.listenSocket.bind(new InetSocketAddress(port));
		this.listenSocket.configureBlocking(true);
		this.listener = new Listener();
	}

	@Override
	public synchronized Thread newThread() {
		Thread thread = new Thread(listener);

		threads.add(thread);

		thread.start();

		return thread;
	}

	@Override
	public synchronized void shutdown() {
		running = false;

		for (Iterator<Thread> it = threads.iterator(); it.hasNext();) {
			Thread thread = it.next();

			if ((thread != null) && (thread.isAlive())) {
				thread.interrupt();
			}
		}

		for (Iterator<Thread> it = threads.iterator(); it.hasNext();) {
			Thread thread = it.next();

			try {
				thread.join();
			} catch (InterruptedException e) {
				// nothing
			}

			it.remove();
		}

		try {
			listenSocket.close();
		} catch (IOException e) {
			logger.error("Caught an exception shutting down", e);
		}
	}

	private class Listener implements Runnable {
		public void run() {
			while (running) {
				try {
					SocketChannel client = listenSocket.accept();

					if (client == null) {
						continue;
					}

					dispatcher.registerChannel(client,
							eventHandlerFactory.newHandler());

				} catch (ClosedByInterruptException e) {
					logger.fatal("ServerSocketChannel closed by interrupt: "
							+ e, e);
					return;

				} catch (ClosedChannelException e) {
					logger.fatal(
							"Exiting, serverSocketChannel is closed: " + e, e);
					return;

				} catch (Throwable t) {
					logger.fatal("Exiting, Unexpected Throwable doing accept: "
							+ t, t);

					try {
						listenSocket.close();
					} catch (Throwable e1) { /* nothing */
					}

					return;
				}
			}
		}
	}

}
