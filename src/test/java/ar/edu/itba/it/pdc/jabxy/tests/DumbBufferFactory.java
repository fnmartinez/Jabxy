package ar.edu.itba.it.pdc.jabxy.tests;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class DumbBufferFactory implements BufferFactory {
	private int capacity;

	public DumbBufferFactory(int capacity) {
		this.capacity = capacity;
	}

	public ByteBuffer newBuffer() {
		return (ByteBuffer.allocate(capacity));
	}

	public void returnBuffer(ByteBuffer buffer) {

	}

}
