package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;

public class ProxyHandlerAdapter extends AbstractHandlerAdapter{
	
	private SelectableChannel inputChannel = null;
	private SelectableChannel outputChannel = null;
	private boolean shuttingDown = false;

	public ProxyHandlerAdapter(Dispatcher dispatcher, InputQueue inputQueue, OutputQueue outputQueue, EventHandler eventHandler) throws SAXException, ParserConfigurationException {
		super(dispatcher, inputQueue, outputQueue, eventHandler);
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
