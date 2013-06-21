package ar.edu.itba.it.pdc.jabxy.model.jabber;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

public abstract class JabberMessage {
	
	protected final Document domRepresentation;
	
	JabberMessage(Document doc) {
		this.domRepresentation = doc;
	}
	
	public Document getDOMRepresentation() {
		return this.domRepresentation;
	}
	
	protected String parseAttribute(String attName) {
		NamedNodeMap map = domRepresentation.getAttributes();
		if (map == null || map.getNamedItem(attName) == null) {
			return null;
		}
		return map.getNamedItem(attName).getTextContent();
	}

}
