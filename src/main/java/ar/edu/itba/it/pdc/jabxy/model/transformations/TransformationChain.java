package ar.edu.itba.it.pdc.jabxy.model.transformations;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberMessage;

public class TransformationChain {
	
	private List<Transformation> transformations;
	
	public TransformationChain() {
		this.transformations = new LinkedList<Transformation>();
	}
	
	public JabberMessage transform(JabberMessage message) {
		for(Transformation t: transformations) {
			message = t.transform(message);
		}
		return message;
	}
	
	public void add(Transformation t) {
		this.transformations.add(t);
	}
	
	public void remove(Transformation t) {
		this.transformations.remove(t);
	}
	
	public int countTransformations(){
		return transformations.size();
	}
}
