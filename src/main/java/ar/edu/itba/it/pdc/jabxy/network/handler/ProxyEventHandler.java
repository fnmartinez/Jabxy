package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.utils.ProxyChannelFacade;

public interface ProxyEventHandler extends EventHandler{

	ByteBuffer nextMessage (ProxyChannelFacade channelFacade);
	void handleConnection(ProxyChannelFacade facade);
	void handleInput (ByteBuffer message, ProxyChannelFacade channelFacade);

	void starting (ProxyChannelFacade channelFacade);
	void started (ProxyChannelFacade channelFacade);
	void stopping (ProxyChannelFacade channelFacade);
	void stopped (ProxyChannelFacade channelFacade);
}
