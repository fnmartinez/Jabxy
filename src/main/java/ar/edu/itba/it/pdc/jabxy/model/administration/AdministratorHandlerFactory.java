package ar.edu.itba.it.pdc.jabxy.model.administration;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandlerFactory;

public class AdministratorHandlerFactory implements EventHandlerFactory<AdminHandler> {

	private AdminProtocol protocol;
	
	public AdministratorHandlerFactory() {
		this.protocol = new AdminProtocol();
	}
	
	@Override
	public AdminHandler newHandler() throws IllegalAccessException,
			InstantiationException {
		return new AdminHandler(protocol);
	}

}
