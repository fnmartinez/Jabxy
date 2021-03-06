package ar.edu.itba.it.pdc.jabxy.network.dispatcher;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public interface Dispatcher<H extends EventHandler, F extends ChannelFacade, A extends HandlerAdapter<H>> {

	void dispatch() throws IOException;

	void shutdown();

	F registerChannel(SelectableChannel channel,
			H handler) throws IOException;

	void unregisterChannel(F key);

	void enqueueStatusChange(A adapter, Object handle);

	Thread start();
}
