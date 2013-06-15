package ar.edu.itba.it.pdc.jabxy.network.queues;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public interface OutputQueue {
	
	boolean isEmpty();
	int drainTo (ByteChannel channel) throws IOException;
	void setChannelFacade(ChannelFacade channelFacade);

	boolean enqueue (ByteBuffer byteBuffer);
	
}
