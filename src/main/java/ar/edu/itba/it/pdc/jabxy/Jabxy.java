package ar.edu.itba.it.pdc.jabxy;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ar.edu.itba.it.pdc.jabxy.model.AdministratorFactory;
import ar.edu.itba.it.pdc.jabxy.model.JabberHandlerFactory;
import ar.edu.itba.it.pdc.jabxy.model.StanzaValidator;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.Acceptor;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.implementations.BasicAcceptor;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.NioProxyDispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.NioServerDispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.ReadWriteBlockingGuard;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueueFactory;

public class Jabxy {

	private static Executor executor = Executors.newCachedThreadPool();
	
	public static Acceptor jabberAcceptor;
	public static Acceptor configAcceptor;

	public static void main(String[] args) {
		try {
			jabberAcceptor = new BasicAcceptor(5233,
					new JabberHandlerFactory(), new NioProxyDispatcher(
							executor, new ReadWriteBlockingGuard(),
							InputQueueFactory
									.newInstance(new StanzaValidator()),
							OutputQueueFactory.newOutputQueueFactory()));
			configAcceptor = new BasicAcceptor(8888,
					new AdministratorFactory(),
					new NioServerDispatcher(executor,
							new ReadWriteBlockingGuard(),
							InputQueueFactory.newInstance(),
							OutputQueueFactory.newOutputQueueFactory()));
			configAcceptor.newThread();
			jabberAcceptor.newThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
