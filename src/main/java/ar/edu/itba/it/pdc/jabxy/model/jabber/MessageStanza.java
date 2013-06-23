package ar.edu.itba.it.pdc.jabxy.model.jabber;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import ar.edu.itba.it.pdc.jabxy.model.transformations.MessageTransformer;

public class MessageStanza extends Stanza {

	public MessageStanza(Document doc) {
		super(doc);
	}

	public void transformBody(MessageTransformer transformer){
		
		NodeList nodes = this.domRepresentation.getElementsByTagName("body");
		
		for(int i = 0 ; i < nodes.getLength() ; i++){
			Element body = (Element)nodes.item(i);
			Text data = (Text)(body.getChildNodes()).item(0);
			data.setData(transformer.transform(data.getData()));
		}
	}	
}
