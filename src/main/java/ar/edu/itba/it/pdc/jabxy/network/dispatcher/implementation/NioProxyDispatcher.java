package ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Executor;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.SelectorGuard;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerFutureTask;
import ar.edu.itba.it.pdc.jabxy.network.handler.ProxyEventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.ProxyHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.exceptions.QueueBuildingException;
import ar.edu.itba.it.pdc.jabxy.network.utils.ProxyChannelFacade;

public class NioProxyDispatcher<T extends ProxyEventHandler> extends AbstractNioDispatcher<T, ProxyChannelFacade, ProxyHandlerAdapter<T>> {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	public NioProxyDispatcher(Executor executor, SelectorGuard guard, InputQueueFactory inputQueueFactory, OutputQueueFactory outputQueueFactory) throws IOException{
		super(executor, guard, inputQueueFactory, outputQueueFactory);
	}
	
	@Override
	public ProxyChannelFacade registerChannel(SelectableChannel channel,
			T handler) throws IOException {
		channel.configureBlocking(false);

		ProxyHandlerAdapter<T> clientAdapter;
		try {
			clientAdapter = new ProxyHandlerAdapter<T>(this, inputQueueFactory.newInputQueue(), outputQueueFactory.newOutputQueue(), handler);
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (QueueBuildingException e) {
			throw new IOException(e);
		}

		clientAdapter.registering();

		acquireSelector();

		try {
			SelectionKey key = channel.register(getSelector(), SelectionKey.OP_READ,
					clientAdapter);

			clientAdapter.setInputKey(key);
			clientAdapter.registered();

			return clientAdapter;
		} finally {
			releaseSelector();
		}
	}

	@Override
	public void unregisterChannel(ProxyChannelFacade facade) {
		if (!(facade instanceof ProxyHandlerAdapter<?>)) {
			throw new IllegalArgumentException("Not a valid registration token");
		}

		@SuppressWarnings("unchecked")
		ProxyHandlerAdapter<T> adapter = (ProxyHandlerAdapter<T>) facade;

		acquireSelector();

		try {
			adapter.unregistering();
			adapter.inputKey().cancel();
			adapter.outputKey().cancel();
		} finally {
			releaseSelector();
		}

		adapter.unregistered();
	}

	public void registerSibling(ProxyHandlerAdapter<T> proxyHandlerAdapter) throws ClosedChannelException {
		// TODO Auto-generated method stub
		ProxyHandlerAdapter<T> sibling = proxyHandlerAdapter.getSibling();

		SelectableChannel channel = sibling.channel1();
		acquireSelector();

		try {
			SelectionKey key = channel.register(getSelector(), SelectionKey.OP_CONNECT,
					sibling);

			sibling.setInputKey(key);
			sibling.registered();

			return;
		} finally {
			releaseSelector();
		}
	}

	@Override
	protected void invokeHandler(ProxyHandlerAdapter<T> adapter, SelectionKey key) {
		adapter.prepareToRun(key);
		key.interestOps(0);
		executor.execute(new HandlerFutureTask<T, ProxyChannelFacade, ProxyHandlerAdapter<T>>(adapter, this, key));
	}
}
