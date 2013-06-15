package ar.edu.itba.it.pdc.jabxy.model.administration;

import ar.edu.itba.it.pdc.jabxy.model.administration.AdminProtocol.AdminProtocolActions;

public interface Command {
	
	String getName();
	
	String execute(String[] args);
	
	String shortHelp();
	
	String descriptiveHelp();
	
	boolean acceptsAction(AdminProtocolActions action);
	
}
