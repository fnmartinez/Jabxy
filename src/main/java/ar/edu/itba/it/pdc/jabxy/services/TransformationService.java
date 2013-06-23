package ar.edu.itba.it.pdc.jabxy.services;

import java.util.HashMap;

import ar.edu.itba.it.pdc.jabxy.model.transformations.Transformation;

public class TransformationService {

	private static HashMap<String,Transformation> transformations = new HashMap<String,Transformation>();
	
	private static Transformation getTransformation(String name){
		
		return transformations.get(name);
	}
	
	public static void addTransformation(String name){
		//TODO: completar...
	}
}
