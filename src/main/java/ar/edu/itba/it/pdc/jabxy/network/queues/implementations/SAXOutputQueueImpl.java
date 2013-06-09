package ar.edu.itba.it.pdc.jabxy.network.queues.implementations;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class SAXOutputQueueImpl implements OutputQueue {
	
	
	public SAXOutputQueueImpl(BufferFactory bufferFactory, DefaultHandler handler) throws ParserConfigurationException, SAXException {
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int drainTo(ByteChannel channel) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean enqueue(ByteBuffer byteBuffer) {
		// TODO Auto-generated method stub
		return false;
	}

}
