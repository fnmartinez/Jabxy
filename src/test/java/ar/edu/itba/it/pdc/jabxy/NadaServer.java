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
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;
import ar.edu.itba.it.pdc.jabxy.tests.DumbBufferFactory;
import ar.edu.itba.it.pdc.jabxy.tests.NadaProtocol;

public class NadaServer
{
	private NadaServer()
	{
		// cannot instantiate
	}

	public static void main (String[] args)	throws IOException
	{
		Executor executor = Executors.newCachedThreadPool();
		BufferFactory bufFactory = new DumbBufferFactory (1024);
		Dispatcher dispatcher = new NioServerDispatcher (executor, bufFactory, new ReadWriteBlockingGuard());
		EventHandlerFactory factory = new NadaProtocol();
		Acceptor acceptor = new BasicAcceptor(1234, factory, dispatcher);

		dispatcher.start();
		acceptor.newThread();
	}
}
