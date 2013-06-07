package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.nio.channels.SelectionKey;
import java.util.concurrent.Callable;

import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public interface HandlerAdapter extends Callable<HandlerAdapter>, ChannelFacade{

	void prepareToRun();

	void setKey(SelectionKey key);

	SelectionKey key();

	boolean isDead();

	// Called when registering, but before the handler is active
	void registering();

	// Called when the handler is registered, but before the first message
	void registered();

	// Called when unregistration has been requested, but while the
	// handler is still active and able to interact with the framework.
	// Extension Point: This implementation simply calls through to
	// the client handler, which may or may not be running. Either
	// the client code must take steps to protect its internal state,
	// or logic could be added here to wait until the handler finishes.
	void unregistering();

	// Called when the handler has been unregistered and is no longer active.
	// If unregistering() waits for the handler to finish, then this
	// one should be safe. If not, then this function has the same
	// concurrency concerns as does unregistering().
	void unregistered();

	void die();

}