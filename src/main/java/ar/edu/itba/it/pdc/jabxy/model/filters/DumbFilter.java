package ar.edu.itba.it.pdc.jabxy.model.filters;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;

public class DumbFilter extends Filter {

	public DumbFilter() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean doFilter(Stanza stanza) {
		return true;
	}

}
