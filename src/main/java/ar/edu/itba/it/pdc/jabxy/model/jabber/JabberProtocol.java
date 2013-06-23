package ar.edu.itba.it.pdc.jabxy.model.jabber;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class JabberProtocol {
	
	public static enum StanzaType {
		//TODO: cambiar por strings que valgan la pena
		MESSAGE (null,null,null),
		PRESENCE (null,null,null),
		IQ (null,null,null),
		UNKNOWN(null, null, null);
		
		private final String uri;
		private final String localName;
		private final String qName;
		
		private StanzaType(String uri, String localName, String qName) {
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
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
	}

	public static final int DEFAULT_PORT = 5222;
	
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	private static JabberProtocol instance;
	
	private JabberProtocol() throws ParserConfigurationException {
		this.factory = DocumentBuilderFactory.newInstance();
		this.builder = factory.newDocumentBuilder();
	}
	
	public static JabberProtocol getInstance() throws ParserConfigurationException {
		if (instance == null) {
			try {
				instance = new JabberProtocol();
			} catch (ParserConfigurationException e) {
				throw new ParserConfigurationException("Could not instantiate Jabber Protocol");
			}
		}
		return instance;
	}
	
	public Stanza getStanza(ByteBuffer byteBuffer) throws SAXException, IOException {
		Document doc = builder.parse(new ByteArrayInputStream(byteBuffer.array()));
		
		String tagName = doc.getDocumentElement().getTagName();
		
		Stanza stanza = null;
		if (StanzaType.MESSAGE.getLocalName().equalsIgnoreCase(tagName)) {
			stanza = new MessageStanza(doc);
		} else if (StanzaType.PRESENCE.getLocalName().equalsIgnoreCase(tagName)) {
			stanza = new PresenceStanza(doc);
		} else if (StanzaType.IQ.getLocalName().equalsIgnoreCase(tagName)) {
			stanza = new IQStanza(doc);
		} else {
			stanza = new UnknownStanza(doc);
		}
		
		return stanza;
	}
}
