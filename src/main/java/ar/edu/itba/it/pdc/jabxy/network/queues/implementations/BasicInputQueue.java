package ar.edu.itba.it.pdc.jabxy.network.queues.implementations;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class BasicInputQueue implements InputQueue {
	private final BufferFactory bufferFactory;
	private final ByteBuffer emptyBuffer;
	private ByteBuffer buffer = null;

	public BasicInputQueue(BufferFactory bufferFactory) {
		this.bufferFactory = bufferFactory;
		emptyBuffer = ByteBuffer.allocate(0).asReadOnlyBuffer();
	}

	public synchronized int fillFrom(ByteChannel channel) throws IOException {
		if (buffer == null) {
			buffer = bufferFactory.newBuffer();
		}

		return channel.read(buffer);
	}

	// -- not needed by framework

	public synchronized boolean isEmpty() {
		return (buffer == null) || (buffer.position() == 0);
	}

	public synchronized int indexOf(byte b) {
		if (buffer == null) {
			return -1;
		}

		int pos = buffer.position();

		for (int i = 0; i < pos; i++) {
			if (b == buffer.get(i)) {
				return i;
			}
		}

		return -1;
	}

	public synchronized ByteBuffer dequeueBytes(int count) {
		if ((buffer == null) || (buffer.position() == 0) || (count == 0)) {
			return emptyBuffer;
		}

		int size = Math.min(count, buffer.position());

		ByteBuffer result = ByteBuffer.allocate(size);

		buffer.flip();

		// TODO: Validate this
		// result.put(buffer.array(), 0, size);
		// buffer.position (size);
		// result.position (size);

		// TODO: this if() should be replaceable by the above
		if (buffer.remaining() <= result.remaining()) {
			result.put(buffer);
		} else {
			while (result.hasRemaining()) {
				result.put(buffer.get());
			}
		}

		if (buffer.remaining() == 0) {
			bufferFactory.returnBuffer(buffer);
			buffer = null;
		} else {
			buffer.compact();
		}

		result.flip();

		return (result);
	}

	public void discardBytes(int count) {
		dequeueBytes(count);
	}
	
	protected ByteBuffer getCurrentMessage() {
		return this.buffer;
	}
}
