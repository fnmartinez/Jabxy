package ar.edu.itba.it.pdc.jabxy.network.handler;

public interface EventHandlerFactory {
	EventHandler newHandler() throws IllegalAccessException, InstantiationException;

}
