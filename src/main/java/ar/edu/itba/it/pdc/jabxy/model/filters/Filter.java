package ar.edu.itba.it.pdc.jabxy.model.filters;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;

public interface Filter {

	boolean filter(JabberMessage stanza);
}
