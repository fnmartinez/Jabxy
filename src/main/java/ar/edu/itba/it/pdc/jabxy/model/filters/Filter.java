package ar.edu.itba.it.pdc.jabxy.model.filters;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;

public interface Filter {

	boolean filter(Stanza stanza);
}
