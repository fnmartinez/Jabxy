package ar.edu.itba.it.pdc.jabxy.model.administration;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.handler.ServerEventHandler;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public class AdminHandler implements ServerEventHandler {

	private AdminProtocol protocol;

	public AdminHandler(AdminProtocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public ByteBuffer nextMessage(ChannelFacade channelFacade) {
		InputQueue inputQueue = channelFacade.inputQueue();
		int nlPos = inputQueue.indexOf((byte) '\n');

		if (nlPos == -1)
			return null;

		if ((nlPos == 1) && (inputQueue.indexOf((byte) '\r') == 0)) {
			inputQueue.discardBytes(2); // eat CR/NL by itself
			return null;
		}

		return (inputQueue.dequeueBytes(nlPos + 1));
	}

	@Override
	public void handleInput(ByteBuffer message, ChannelFacade facade) {
		ByteBuffer response = protocol.handleMessage(message);
		facade.outputQueue().enqueue(response);
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
