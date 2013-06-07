package ar.edu.itba.it.pdc.jabxy.model;

import java.nio.ByteBuffer;

import org.xml.sax.helpers.DefaultHandler;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public class StanzaHandler extends DefaultHandler implements EventHandler {

	@Override
	public ByteBuffer nextMessage(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleInput(ByteBuffer message, ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void starting(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void started(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopping(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopped(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	
}
