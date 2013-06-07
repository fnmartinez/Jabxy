package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

import org.xml.sax.ContentHandler;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.InputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.OutputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.SAXInputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class ProxyHandlerAdapter extends AbstractHandlerAdapter{
	
	private SelectableChannel inputChannel = null;
	private SelectableChannel outputChannel = null;
	private boolean shuttingDown = false;

	public ProxyHandlerAdapter(Dispatcher dispatcher, BufferFactory bufferFactory, EventHandler eventHandler, ContentHandler contentHandler) {
		super(dispatcher, new SAXInputQueueImpl(bufferFactory, contentHandler), null, eventHandler);
		
	}

	@Override
	public HandlerAdapter call() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ProxyHandlerAdapter getSibling() {
		// TODO Auto-generated method stub
		return null;
	}

}
