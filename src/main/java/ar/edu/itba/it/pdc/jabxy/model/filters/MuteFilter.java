package ar.edu.itba.it.pdc.jabxy.model.filters;

import ar.edu.itba.it.pdc.jabxy.model.JabxyUser;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberProtocol;
import ar.edu.itba.it.pdc.jabxy.model.jabber.MessageStanza;

public class MuteFilter extends Filter {

	public MuteFilter(JabberProtocol protocol){
		super(protocol);
	}
	
	@Override
	public boolean filter(JabberMessage stanza, JabxyUser user) {

		if((stanza instanceof MessageStanza)){
			String JID = user.getJID();
			String from = ((MessageStanza) stanza).getFrom();
			String to = ((MessageStanza) stanza).getTo();
			boolean hit = (JID.equalsIgnoreCase(from)) || (JID.equalsIgnoreCase(to));
			
			if(hit){
				return false;				
			}
		}
		
		return true;
	}

	@Override
	public JabberMessage getResponseMessage(JabberMessage stanza){
		
		String from = ((MessageStanza) stanza).getFrom();
		String to = ((MessageStanza) stanza).getTo();
		
		return this.protocol.createErrorMessage(from, to);
	}
}
