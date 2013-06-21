package ar.edu.itba.it.pdc.jabxy.model.transformations;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;
import ar.edu.itba.it.pdc.jabxy.model.jabber.MessageStanza;
import ar.edu.itba.it.pdc.jabxy.model.jabber.Stanza;

public class TransformationL33t implements Transformation, MessageTransformer {
	
	@Override
	public JabberMessage transform(JabberMessage message) {
		
		if((message instanceof MessageStanza) && ((Stanza)message).getType() != "error"){

			((MessageStanza)message).transformBody(this);
		}
		
		return message;
	}

	@Override
	public String transform(String body) {
		
		body = body.replaceAll("a", "4");
		body = body.replaceAll("e", "3");
		body = body.replaceAll("i", "1");
		body = body.replaceAll("o", "0");
		body = body.replaceAll("c", "<");
		
		return body;
	}
}
