package ar.edu.itba.it.pdc.jabxy.model.transformations;

import ar.edu.itba.it.pdc.jabxy.model.stanzas.Stanza;

public class DumbTransformation extends Transformation {

	public DumbTransformation(Transformation transformation) {
		super(transformation);
	}

	@Override
	protected Stanza doTransformation(Stanza stanza) {
		return stanza;
	}

}
