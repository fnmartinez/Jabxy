package ar.edu.itba.it.pdc.jabxy.network.queues.implementations;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.Deque;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.org.retep.niosax.NioSaxParser;
import uk.org.retep.niosax.NioSaxParserFactory;
import uk.org.retep.niosax.NioSaxParserHandler;
import uk.org.retep.niosax.NioSaxSource;
import uk.org.retep.util.state.TriState;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class SAXInputQueueImpl extends DefaultHandler implements InputQueue, NioSaxParserHandler {

	private NioSaxParser parser;
	private NioSaxSource source;
	private ContentHandler handler;
	private ByteBuffer buffer;
	private Deque<Element> newElements;
	private Deque<Element> closedElements;
	private BufferFactory bufferFactory;
	
	public SAXInputQueueImpl(BufferFactory bufferFactory, ContentHandler handler) throws SAXException {
		this.bufferFactory = bufferFactory;
		this.handler = handler;

		NioSaxParserFactory factory = NioSaxParserFactory.getInstance();
		this.parser = factory.newInstance(this.handler);
		this.parser.startDocument();
	}
	
	@Override
	public synchronized int fillFrom(ByteChannel channel) throws IOException {
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
	public synchronized boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int indexOf(byte b) {
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

	// XXX: NioSaxParserHandler interface implementation
	
	@Override
	public void xmlDeclaration(String versionInfo, String encoding,
			TriState standalone) {
		// TODO Auto-generated method stub
	}
	
	// XXX: ContentHandler interface Overridal

	@Override
	public void setDocumentLocator(Locator locator) {
		this.handler.setDocumentLocator(locator);		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.endDocument();
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.startPrefixMapping(prefix, uri);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.startElement(uri, localName, qName, atts);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		
		this.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.characters(ch, start, length);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.processingInstruction(target, data);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
		this.handler.skippedEntity(name);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		// TODO:
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		// TODO:
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		// TODO:
	}
	
	private class Element {
		private String uri;
		private String localName;
		private String qName;
		private Attributes attributes;
		
		public Element(String uri, String localName, String qName, Attributes attributes) {
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
			this.attributes = attributes;
		}
		
		
	}
}
