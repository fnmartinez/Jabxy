package ar.edu.itba.it.pdc.jabxy.model.transformations;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;

public interface Transformation {
	
	public JabberMessage transform(JabberMessage message);

}
