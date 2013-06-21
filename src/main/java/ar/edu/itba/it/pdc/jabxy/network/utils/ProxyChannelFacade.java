package ar.edu.itba.it.pdc.jabxy.network.utils;

public interface ProxyChannelFacade extends ChannelFacade {
	void redirectToInput();
	void redirectToOutput();
	void connectOutput(String url, int port);
}
