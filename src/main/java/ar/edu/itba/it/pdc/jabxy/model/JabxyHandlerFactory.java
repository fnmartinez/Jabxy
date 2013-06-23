package ar.edu.itba.it.pdc.jabxy.model;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberProtocol;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandlerFactory;

public class JabxyHandlerFactory implements EventHandlerFactory<JabxyHandler> {

	@Override
	public JabxyHandler newHandler() throws IllegalAccessException,
			InstantiationException {
		try {
			return new JabxyHandler(JabberProtocol.getInstance());
		} catch (TransformerConfigurationException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			throw new InstantiationException("Unnable to instantiate JabxyHandler");
		}
	}

}
