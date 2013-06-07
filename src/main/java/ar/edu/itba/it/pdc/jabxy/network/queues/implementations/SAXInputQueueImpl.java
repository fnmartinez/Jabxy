package ar.edu.itba.it.pdc.jabxy.network.queues.implementations;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import uk.org.retep.niosax.NioSaxParser;
import uk.org.retep.niosax.NioSaxParserFactory;
import uk.org.retep.niosax.NioSaxSource;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class SAXInputQueueImpl implements InputQueue {

	private NioSaxParser parser;
	private NioSaxSource source;
	private ContentHandler handler;
	private BufferFactory bufferFactory;
	
	public SAXInputQueueImpl(BufferFactory bufferFactory, ContentHandler handler) {
		this.bufferFactory = bufferFactory;
		this.handler = handler;

		NioSaxParserFactory factory = NioSaxParserFactory.getInstance();
		this.parser = factory.newInstance(this.handler);
	}
	
	@Override
	public int fillFrom(ByteChannel channel) throws IOException {
		ByteBuffer buffer = this.bufferFactory.newBuffer();
		
		int bytesRead = channel.read(buffer);
		
		buffer.flip();
		this.source.setByteBuffer(buffer);
		try {
			this.parser.parse(this.source);
		} catch (SAXException e) {
			throw new IOException(e);
		}
		this.source.compact();
		return bytesRead;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int indexOf(byte b) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ByteBuffer dequeueBytes(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void discardBytes(int count) {
		// TODO Auto-generated method stub

	}

}
