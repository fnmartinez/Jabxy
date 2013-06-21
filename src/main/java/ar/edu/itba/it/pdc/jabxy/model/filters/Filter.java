package ar.edu.itba.it.pdc.jabxy.model.filters;

import ar.edu.itba.it.pdc.jabxy.model.JabxyUser;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberProtocol;

public abstract class Filter {

	protected JabberProtocol protocol;
	
	public Filter(JabberProtocol protocol){
		
		this.protocol = protocol;
	}
	
	public abstract boolean filter(JabberMessage stanza, JabxyUser user);
	public abstract JabberMessage getResponseMessage(JabberMessage stanza);
}
