package ar.edu.itba.it.pdc.jabxy.network.queues;

import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;
import ar.edu.itba.it.pdc.jabxy.tests.DumbBufferFactory;

public abstract class OutputQueueFactory {

	private static BufferFactory defaultBufferFactory = new DumbBufferFactory(1024);
	
	public static OutputQueueFactory newOutputQueueFactory() {
		return new BasicOutputFactory(defaultBufferFactory);
	}
	
	public abstract OutputQueue newOutputQueue();
}
