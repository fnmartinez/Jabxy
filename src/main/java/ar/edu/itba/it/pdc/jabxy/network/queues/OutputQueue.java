package ar.edu.itba.it.pdc.jabxy.network.queues;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public interface OutputQueue {
	
	boolean isEmpty();
	int drainTo (ByteChannel channel) throws IOException;

	boolean enqueue (ByteBuffer byteBuffer);
	
}
