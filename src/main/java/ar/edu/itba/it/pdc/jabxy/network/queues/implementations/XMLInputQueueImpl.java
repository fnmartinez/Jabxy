package ar.edu.itba.it.pdc.jabxy.network.queues.implementations;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.util.Deque;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
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
import ar.edu.itba.it.pdc.jabxy.network.queues.XMLValidator;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

import com.google.common.base.Charsets;

public class XMLInputQueueImpl extends DefaultHandler implements InputQueue, NioSaxParserHandler {

	private NioSaxParser parser;
	private NioSaxSource source;
	private XMLValidator validator;
	private Deque<Element> openedElements;
	private Deque<Element> closedElements;
	private Document docClosedElements;
	private BufferFactory bufferFactory;
	private String leftOver;

	public XMLInputQueueImpl(BufferFactory bufferFactory, XMLValidator validator)
			throws SAXException, ParserConfigurationException {
		this.bufferFactory = bufferFactory;
		this.validator = validator;

		NioSaxParserFactory factory = NioSaxParserFactory.getInstance();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		this.docClosedElements = builder.newDocument();
		this.parser = factory.newInstance(this);
		this.parser.startDocument();
		this.leftOver = "";
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
		return this.validator.isValidXML();
	}

	@Override
	public int indexOf(byte b) {
		return 0;
	}

	@Override
	public ByteBuffer dequeueBytes(int count) {
		StringBuffer sb = new StringBuffer(this.leftOver);
		while (!this.closedElements.isEmpty()) {
			Element e = this.closedElements.pop();
			int endOfElement = 0;
			if ((endOfElement = sb.indexOf("</")) != -1) {
				endOfElement--;
				sb.indexOf(e.toString(), endOfElement);
			} else {
				sb.append(e.toString());
			}
			if (sb.length() >= count) {
				break;
			}
		}

		this.leftOver = sb.substring(count, sb.length());

		ByteBuffer bf = ByteBuffer.allocate(sb.length());
		bf.put(sb.substring(0, count).getBytes());
		return bf;
	}

	@Override
	public void discardBytes(int count) {
		this.leftOver = this.leftOver.substring(count);
	}

	public String getClosedElements() {
		StringBuffer sb = new StringBuffer();
		while (!this.closedElements.isEmpty()) {
			Element e = this.closedElements.poll();
			this.closedElements.pop();
			int endOfElement = 0;
			if ((endOfElement = sb.indexOf("</")) != -1) {
				endOfElement--;
				sb.indexOf(e.toString(), endOfElement);
			} else {
				sb.append(e.toString());
			}
		}

		return sb.toString();
	}

	// XXX: ContentHandler interface Overridal

	@Override
	public void setDocumentLocator(Locator locator) {
		this.validator.setDocumentLocator(locator);
	}

	@Override
	public void startDocument() throws SAXException {
		this.validator.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		this.validator.endDocument();
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		this.validator.startPrefixMapping(prefix, uri);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		this.validator.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (StringUtils.isEmpty(localName) && StringUtils.isEmpty(qName)) {
			throw new SAXException(
					"Namespaces and qualified names not available.");
		}
		this.openedElements.add(new Element(uri, localName, qName, atts));
		this.validator.startElement(uri, localName, qName, atts);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (StringUtils.isEmpty(localName) && StringUtils.isEmpty(qName)) {
			throw new SAXException(
					"Namespaces and qualified names not not avilable");
		}
		Element e = this.openedElements.pop();
		if ((!StringUtils.isEmpty(e.qName) && e.getQName().equalsIgnoreCase(qName))
				|| (!StringUtils.isEmpty(e.localName) && e.getLocalName().equalsIgnoreCase(localName))) {
			e.close();
			this.closedElements.push(e);
		}
		this.validator.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		this.openedElements.peek().setContent(ch, start, length);
		this.validator.characters(ch, start, length);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		this.validator.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		this.validator.processingInstruction(target, data);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		this.validator.skippedEntity(name);
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
		private final String uri;
		private final String localName;
		private final String qName;
		private final Attributes attributes;
		private StringBuffer content;
		private boolean closed;

		public Element(String uri, String localName, String qName,
				Attributes attributes) {
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
			this.attributes = attributes;
			this.closed = false;
		}

		public void setContent(char cs[], int start, int len) {
			content.append(cs, start, len);
		}

		public boolean isClosed() {
			return this.closed;
		}

		public void close() {
			this.closed = true;
		}

		public String getUri() {
			return this.uri;
		}

		public String getLocalName() {
			return this.localName;
		}

		public String getQName() {
			return this.qName;
		}

		public Attributes getAttributes() {
			return this.attributes;
		}

		public String getContent() {
			return this.content.toString();
		}
		
		public String getTagname() {
			return (!StringUtils.isEmpty(this.qName) ? this.qName
					: this.localName);
		}

		public String toString() {
			// TODO
			StringBuffer sb = new StringBuffer();

			sb.append('<');
			sb.append(getTagname());
			sb.append(' ');
			sb.append("xmlns='" + this.uri + "'");
			for (int i = 0; i < this.attributes.getLength(); i++) {
				sb.append(' ');
				if (!StringUtils.isEmpty(uri)) {
					sb.append("xmlns='" + this.uri + "'");
				}
				if (!StringUtils.isEmpty(attributes.getQName(i))) {
					sb.append(attributes.getQName(i));
				} else if (!StringUtils.isEmpty(attributes.getLocalName(i))) {
					sb.append(attributes.getLocalName(i));
				}
				sb.append("=\"");
				sb.append(attributes.getValue(i));
				sb.append("\"");
			}
			sb.append('>');
			sb.append(content);
			if (closed) {
				sb.append("</");
				sb.append(getTagname());
				sb.append('>');
			}
			return sb.toString();
		}
		
	}

	@Override
	public void xmlDeclaration(String versionInfo, String encoding,
			TriState standalone) {
		// TODO Auto-generated method stub
		if (!Charset.forName(encoding).equals(Charsets.UTF_8)) {
			// Cortar conexión
		}
		
	}
}
