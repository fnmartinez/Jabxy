package ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Executor;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.SelectorGuard;
import ar.edu.itba.it.pdc.jabxy.network.handler.AbstractHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.ProxyHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public class NioProxyDispatcher extends AbstractNioDispatcher{
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	private final BufferFactory bufferFactory;
	
	public NioProxyDispatcher(Executor executor, BufferFactory bufferFactory, SelectorGuard guard) throws IOException{
		super(executor, guard);
		this.bufferFactory = bufferFactory;
	}
	

	@Override
	public ChannelFacade registerChannel(SelectableChannel channel,
			EventHandler handler) throws IOException {
		channel.configureBlocking(false);

		ProxyHandlerAdapter clientAdapter;
		try {
			clientAdapter = new ProxyHandlerAdapter(this, bufferFactory, handler, (ContentHandler)handler);
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		}

		ProxyHandlerAdapter serverAdapter = clientAdapter.getSibling();
		
		serverAdapter.registering();
		clientAdapter.registering();

		acquireSelector();

		try {
			SelectionKey key = channel.register(getSelector(), SelectionKey.OP_READ,
					clientAdapter);

			clientAdapter.setKey(key);
			clientAdapter.registered();

			return clientAdapter;
		} finally {
			releaseSelector();
		}
	}

	@Override
	public void unregisterChannel(ChannelFacade key) {
		if (!(key instanceof AbstractHandlerAdapter)) {
			throw new IllegalArgumentException("Not a valid registration token");
		}

		HandlerAdapter adapter = (HandlerAdapter) key;
		SelectionKey selectionKey = adapter.key();

		acquireSelector();

		try {
			adapter.unregistering();
			selectionKey.cancel();
		} finally {
			releaseSelector();
		}

		adapter.unregistered();
	}

}
