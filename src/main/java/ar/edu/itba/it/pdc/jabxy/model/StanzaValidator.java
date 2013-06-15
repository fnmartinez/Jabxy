package ar.edu.itba.it.pdc.jabxy.model;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ar.edu.itba.it.pdc.jabxy.network.queues.XMLValidator;

public class StanzaValidator extends DefaultHandler implements XMLValidator {

	private boolean startingCommunication = false;
	private boolean waitingForStreamClose = false;
	private Deque<String> tagDeque = new LinkedList<String>();
	
	@Override
	public void startDocument() throws SAXException {
		this.startingCommunication = true;
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		if (startingCommunication && ((StringUtils.isNotBlank(localName) && localName.equalsIgnoreCase("stream:stream")) ||
				(StringUtils.isNotBlank(qName) && qName.equalsIgnoreCase("stream")))) {
			this.waitingForStreamClose = true;
		}else{
			tagDeque.push(qName);
		}
		
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if(qName == tagDeque.peek()){
			tagDeque.pop();
		}else if(((StringUtils.isNotBlank(localName) && localName.equalsIgnoreCase("stream:stream")) ||
				(StringUtils.isNotBlank(qName) && qName.equalsIgnoreCase("stream")))){
			this.waitingForStreamClose = false;
		}else{
			throw new SAXException();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public int isValidMessage() {
		
		if(this.tagDeque.size() == 0){
			return 1;
		}else{
			return -1;
		}
	}

	@Override
	public int isValidMessage(ByteBuffer message) {
		// TODO Auto-generated method stub
		return 0;
	}

}
