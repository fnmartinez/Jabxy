package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public interface ServerEventHandler extends EventHandler{
	ByteBuffer nextMessage (ChannelFacade channelFacade);
	void handleInput (ByteBuffer message, ChannelFacade channelFacade);
}
