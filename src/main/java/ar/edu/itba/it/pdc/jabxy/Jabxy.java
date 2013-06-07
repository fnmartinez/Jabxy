package ar.edu.itba.it.pdc.jabxy;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ar.edu.itba.it.pdc.jabxy.model.AdministratorFactory;
import ar.edu.itba.it.pdc.jabxy.model.JabberHandlerFactory;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.Acceptor;
import ar.edu.itba.it.pdc.jabxy.network.acceptor.implementations.BasicAcceptor;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.NioServerDispatcher;
import ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation.ReadWriteBlockingGuard;
import ar.edu.itba.it.pdc.jabxy.tests.DumbBufferFactory;

public class Jabxy {

	private static Executor executor = Executors.newCachedThreadPool();
	
	public static void main(String[] args) {
		try {
			Acceptor jabberAcceptor = new BasicAcceptor(5233, new JabberHandlerFactory(), new NioServerDispatcher(executor, new DumbBufferFactory(1024), new ReadWriteBlockingGuard()));
			Acceptor configAcceptor = new BasicAcceptor(8888, new AdministratorFactory(), new NioServerDispatcher(executor, new DumbBufferFactory(1024), new ReadWriteBlockingGuard()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
