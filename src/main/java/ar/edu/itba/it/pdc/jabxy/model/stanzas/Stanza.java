package ar.edu.itba.it.pdc.jabxy.model.stanzas;

import org.w3c.dom.Document;

public abstract class Stanza {

	private final Document domRepresentation;
	
	Stanza(Document doc) {
		this.domRepresentation = doc;
	}
	
	public Document getDOMRepresentation() {
		return this.domRepresentation;
	}
}
