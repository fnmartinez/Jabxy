package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.queues.XMLValidator;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.OutputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.XMLInputQueueImpl;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class ProxyHandlerAdapter extends AbstractHandlerAdapter{
	
	private SelectableChannel inputChannel = null;
	private SelectableChannel outputChannel = null;
	private boolean shuttingDown = false;

	public ProxyHandlerAdapter(Dispatcher dispatcher, BufferFactory bufferFactory, EventHandler eventHandler, XMLValidator validator) throws SAXException, ParserConfigurationException {
		super(dispatcher, new XMLInputQueueImpl(bufferFactory, validator), null, eventHandler);
		setOutputQueue(new OutputQueueImpl(bufferFactory, this));
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
