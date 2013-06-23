package ar.edu.itba.it.pdc.jabxy.model.filters;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.it.pdc.jabxy.model.JabxyUser;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;

public class FilterChain {
	
	private List<Filter> filters;
	
	private JabxyUser user;
	
	public FilterChain(JabxyUser user) {
		this.filters = new LinkedList<Filter>();
		this.user = user;
	}
	
	public boolean filter(JabberMessage stanza) {
		//TODO: que devuelva quien bloqueo, o null.
		boolean accepted = true;
		for(Filter f : filters) {
			accepted = accepted && f.filter(stanza, user);
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
	
	public int countFilters(){
		return filters.size();
	}

}
