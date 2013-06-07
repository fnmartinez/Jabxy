package ar.edu.itba.it.pdc.jabxy.network.dispatcher;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.ServerHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public interface Dispatcher {

	void dispatch() throws IOException;

	void shutdown();

	ChannelFacade registerChannel(SelectableChannel channel,
			EventHandler handler) throws IOException;

	void unregisterChannel(ChannelFacade key);

	void enqueueStatusChange(HandlerAdapter adapter);

	//TODO: Ver si va
	Thread start();
}
