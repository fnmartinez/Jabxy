package ar.edu.itba.it.pdc.jabxy.model.transformations;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;

public class TransformationChain {
	
	private List<Transformation> transformations;
	
	public TransformationChain() {
		this.transformations = new LinkedList<Transformation>();
	}
	
	public Stanza transform(Stanza stanza) {
		for(Transformation t: transformations) {
			stanza = t.transform(stanza);
		}
		return stanza;
	}
	
	public void add(Transformation t) {
		this.transformations.add(t);
	}
	
	public void remove(Transformation t) {
		this.transformations.remove(t);
	}
}
