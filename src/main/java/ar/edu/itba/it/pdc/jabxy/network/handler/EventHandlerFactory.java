package ar.edu.itba.it.pdc.jabxy.network.handler;

public interface EventHandlerFactory<H extends EventHandler> {
	H newHandler() throws IllegalAccessException, InstantiationException;

}
