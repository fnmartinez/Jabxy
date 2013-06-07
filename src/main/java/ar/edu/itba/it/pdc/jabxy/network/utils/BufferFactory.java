package ar.edu.itba.it.pdc.jabxy.network.utils;

import java.nio.ByteBuffer;

public interface BufferFactory {

	ByteBuffer newBuffer();
	void returnBuffer (ByteBuffer buffer);
}
