package ar.edu.itba.it.pdc.jabxy;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ar.edu.itba.it.pdc.jabxy.model.JabxyHandlerFactory;
import ar.edu.itba.it.pdc.jabxy.model.administration.AdministratorHandlerFactory;
import ar.edu.itba.it.pdc.jabxy.model.jabber.StanzaValidator;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.Acceptor;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.implementations.BasicSocketAcceptor;
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
		//TODO: configuracion...
		
		int defaultJabberPort = 5233;
        int defaultAdminPort = 8888;
		
		try {
			jabberAcceptor = new BasicSocketAcceptor(defaultJabberPort,
					new JabxyHandlerFactory(), new NioProxyDispatcher(
							executor, new ReadWriteBlockingGuard(),
							InputQueueFactory
									.newInstance(new StanzaValidator()),
							OutputQueueFactory.newInstance()));
			configAcceptor = new BasicSocketAcceptor(defaultAdminPort,
					new AdministratorHandlerFactory(),
					new NioServerDispatcher(executor,
							new ReadWriteBlockingGuard(),
							InputQueueFactory.newInstance(),
							OutputQueueFactory.newInstance()));
			configAcceptor.newThread();
			jabberAcceptor.newThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
