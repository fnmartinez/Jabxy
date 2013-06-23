package ar.edu.itba.it.pdc.jabxy.model;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberProtocol;
import ar.edu.itba.it.pdc.jabxy.model.jabber.OpeningStreamMessage;
import ar.edu.itba.it.pdc.jabxy.network.handler.ProxyEventHandler;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.ValidatedInputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;
import ar.edu.itba.it.pdc.jabxy.network.utils.ProxyChannelFacade;
import ar.edu.itba.it.pdc.jabxy.services.UserService;

public class JabxyHandler implements ProxyEventHandler{
	
	private JabxyUser user;
	private JabberProtocol protocol;
	private TransformerFactory transFactory;
	private Transformer transformer;
	private boolean establishingConnection = false;
	
	public JabxyHandler(JabberProtocol protocol) throws TransformerConfigurationException {
		transFactory = TransformerFactory.newInstance();
		transformer = transFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		this.protocol = protocol;
	}

	@Override
	public ByteBuffer nextMessage(ProxyChannelFacade channelFacade) {
		if (establishingConnection) {
			return ByteBuffer.allocate(0).asReadOnlyBuffer();
		}
		if (channelFacade.inputQueue().isEmpty()) {
			return null;
		}
		return ((ValidatedInputQueue)channelFacade.inputQueue()).dequeueValidatedMessage();		
	}
	

	@Override
	public void handleConnection(ProxyChannelFacade facade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInput(ByteBuffer message, ProxyChannelFacade channelFacade) {
		ProxyChannelFacade facade = (ProxyChannelFacade) channelFacade;
		JabberMessage msg = null;
		try {
			msg = protocol.getStanza(message);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if (user == null) {
			
		}
		if (user.getFilters().filter(msg)) {
			if (msg instanceof OpeningStreamMessage) {
				OpeningStreamMessage osStanza = (OpeningStreamMessage) msg;
				this.user = UserService.getUser(osStanza);
				try {
					facade.connectOutput(osStanza.getTo(), JabberProtocol.DEFAULT_PORT);
				} catch (ClosedChannelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.establishingConnection = true;
			} else {
				msg = user.getTransformations().transform(msg);
				facade.redirectToOutput();
			}
		}
		else {
			facade.redirectToInput();
		}
		
		StringWriter buffer = new StringWriter();
		try {
			transformer.transform(new DOMSource(msg.getDOMRepresentation()),
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
	
	public JabxyUser getUser() {
		return this.user;
	}

	@Override
	public void starting(ProxyChannelFacade channelFacade) {
		// TODO Auto-generated method stub
	}

	@Override
	public void started(ProxyChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopping(ProxyChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopped(ProxyChannelFacade channelFacade) {
		// TODO Auto-generated method stub
		
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
