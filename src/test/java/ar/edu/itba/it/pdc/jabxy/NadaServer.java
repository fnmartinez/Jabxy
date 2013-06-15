package ar.edu.itba.it.pdc.jabxy;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ar.edu.itba.it.pdc.jabxy.network.acceptor.Acceptor;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.implementations.BasicAcceptor;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.NioServerDispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.ReadWriteBlockingGuard;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandlerFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.tests.NadaProtocol;

public class NadaServer {
	private NadaServer() {
		// cannot instantiate
	}

	public static void main(String[] args) throws IOException {
		Executor executor = Executors.newCachedThreadPool();

		Dispatcher dispatcher = new NioServerDispatcher(executor,
				new ReadWriteBlockingGuard(), InputQueueFactory.newInstance(),
				OutputQueueFactory.newInstance());
		EventHandlerFactory factory = new NadaProtocol();
		Acceptor acceptor = new BasicAcceptor(1234, factory, dispatcher);

		dispatcher.start();
		acceptor.newThread();
	}
}
