package ar.edu.itba.it.pdc.jabxy.model;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

public class StanzaHandler implements EventHandler{
	
	private JabberUser user;
	private JabberProtocol protocol;
	private TransformerFactory transFactory;
	private Transformer transformer;
	
	StanzaHandler() throws TransformerConfigurationException {
		transFactory = TransformerFactory.newInstance();
		transformer = transFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	}

	@Override
	public ByteBuffer nextMessage(ChannelFacade channelFacade) {
		if (channelFacade.inputQueue().isEmpty()) {
			return null;
		}
		
		// TODO: should exist a method to dequeue the bytes I want or the the finished message
		return channelFacade.inputQueue().dequeueBytes(0);		
	}

	@Override
	public void handleInput(ByteBuffer message, ChannelFacade channelFacade) {
		Stanza stanza = null;
		try {
			stanza = protocol.getStanza(message);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		if (user.getFilters().filter(stanza)) {
			stanza = user.getTransformations().transform(stanza);
		}
		
		StringWriter buffer = new StringWriter();
		try {
			transformer.transform(new DOMSource(stanza.getDOMRepresentation()),
			      new StreamResult(buffer));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = buffer.toString();
		ByteBuffer bf = ByteBuffer.allocate(str.length());
		bf.put(str.getBytes());
		
		channelFacade.outputQueue().enqueue(bf);
	}
	
	public JabberUser getUser() {
		return this.user;
	}

	@Override
	public void starting(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void started(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopping(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopped(ChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

}
