package ar.edu.itba.it.pdc.jabxy.network.utils;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;

public interface ChannelFacade {
	InputQueue inputQueue();
	OutputQueue outputQueue();
	void setHandler (EventHandler handler);
	int getInterestOps();
	void modifyInterestOps (int opsToSet, int opsToReset);
}
