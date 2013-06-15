package ar.edu.itba.it.pdc.jabxy.model.transformations;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;

public abstract class Transformation {
	
	private Transformation nextTransformation;
	
	public Transformation(Transformation transformation) {
		this.nextTransformation = transformation;
	}
	
	public Stanza transform(Stanza stanza) {
		return this.nextTransformation.transform(doTransformation(stanza));
	}
	
	protected abstract Stanza doTransformation(Stanza stanza);

}
