package ar.edu.itba.it.pdc.jabxy.model.filters;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;

public class FilterChain {
	
	private List<Filter> filters;
	
	public FilterChain() {
		this.filters = new LinkedList<Filter>();
	}
	
	public boolean filter(JabberMessage stanza) {
		boolean accepted = true;
		for(Filter f : filters) {
			accepted = accepted && f.filter(stanza);
			if (!accepted) {
				break;
			}
		}
		return accepted;
	}
	
	public void add(Filter f) {
		this.filters.add(f);
	}
	
	public void remove(Filter f) {
		this.filters.remove(f);
	}

}
