package ar.edu.itba.it.pdc.jabxy.model.filters;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;

public abstract class Filter {

	private Filter nextFilter;
	
	public Filter(Filter nextFilter) {
		this.nextFilter = nextFilter;
	}
	
	public boolean filter(Stanza stanza) {
		return this.doFilter(stanza) && (this.nextFilter == null? true:this.nextFilter.filter(stanza));
	}
	
	protected abstract boolean doFilter(Stanza stanza);
}
