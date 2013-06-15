package ar.edu.itba.it.pdc.jabxy.network.dispatcher.implementation;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.SelectorGuard;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.handler.ServerHandlerAdapter;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueueFactory;
import ar.edu.itba.it.pdc.jabxy.network.queues.exceptions.QueueBuildingException;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public class NioServerDispatcher extends AbstractNioDispatcher {
	// TODO: revisar la posibilidad de cambiar a AtomicReference el Selector
	// para evitar el SelectorGuard
	private final Logger logger = Logger.getLogger(getClass().getName());

	public NioServerDispatcher(Executor executor, SelectorGuard guard, InputQueueFactory inputQueueFactory, OutputQueueFactory outputQueueFactory) throws IOException {
		super(executor, guard, inputQueueFactory, outputQueueFactory);
	}

	@Override
	public ChannelFacade registerChannel(SelectableChannel channel,
			EventHandler handler) throws IOException {
		channel.configureBlocking(false);

		ServerHandlerAdapter adapter;
		try {
			adapter = new ServerHandlerAdapter(this, inputQueueFactory.newInputQueue(), outputQueueFactory.newOutputQueue(), handler);
		} catch (QueueBuildingException e) {
			throw new IOException(e);
		}

		adapter.registering();

		acquireSelector();

		try {
			SelectionKey key = channel.register(getSelector(), SelectionKey.OP_READ,
					adapter);

			adapter.setKey(key);
			adapter.registered();

			return adapter;
		} finally {
			releaseSelector();
		}
	}

	@Override
	public void unregisterChannel(ChannelFacade key) {
		if (!(key instanceof ServerHandlerAdapter)) {
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
