package ar.edu.itba.it.pdc.jabxy.tests;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: Apr 8, 2006
 * Time: 5:49:36 PM
 */
public class NadaHandler implements EventHandler
{
	private final NadaProtocol protocol;

	public NadaHandler (NadaProtocol protocol)
	{
		this.protocol = protocol;
	}

	// --------------------------------------------------------
	// Implementation of the InputHandler interface

	public ByteBuffer nextMessage (ChannelFacade channelFacade)
	{
		InputQueue inputQueue = channelFacade.inputQueue();
		int nlPos = inputQueue.indexOf ((byte) '\n');

		if (nlPos == -1) return null;

		if ((nlPos == 1) && (inputQueue.indexOf ((byte) '\r') == 0)) {
			inputQueue.discardBytes (2);	// eat CR/NL by itself
			return null;
		}

		return (inputQueue.dequeueBytes (nlPos + 1));
	}

	public void handleInput (ByteBuffer message, ChannelFacade channelFacade)
	{
		protocol.handleMessage (channelFacade, message);
	}


	public void starting (ChannelFacade channelFacade)
	{
//		System.out.println ("NadaHandler: starting");
	}

	public void started (ChannelFacade channelFacade)
	{
		protocol.newUser (channelFacade);
	}

	public void stopping (ChannelFacade channelFacade)
	{
//		System.out.println ("NadaHandler: stopping");
	}

	public void stopped (ChannelFacade channelFacade)
	{
		protocol.endUser (channelFacade);
	}
}
