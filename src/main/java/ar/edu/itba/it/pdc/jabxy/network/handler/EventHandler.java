package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public interface EventHandler {
	ByteBuffer nextMessage (ChannelFacade channelFacade);
	void handleInput (ByteBuffer message, ChannelFacade channelFacade);

	void starting (ChannelFacade channelFacade);
	void started (ChannelFacade channelFacade);
	void stopping (ChannelFacade channelFacade);
	void stopped (ChannelFacade channelFacade);
}
