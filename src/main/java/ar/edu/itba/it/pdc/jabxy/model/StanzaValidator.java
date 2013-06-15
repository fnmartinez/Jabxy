package ar.edu.itba.it.pdc.jabxy.model;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ar.edu.itba.it.pdc.jabxy.network.queues.XMLValidator;

public class StanzaValidator extends DefaultHandler implements XMLValidator {

	private boolean startingCommunication = false;
	private String[] validTags = {"stream", "message", "presence", "IQ"};
	
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
			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		//Se llama siempre que dentro del tag haya algo, no importa si es contenido u otro tag hijo.
	}

	@Override
	public boolean isValidXML() {
		// TODO Auto-generated method stub
		return false;
	}

}
