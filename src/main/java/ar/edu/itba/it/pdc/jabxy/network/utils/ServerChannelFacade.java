package ar.edu.itba.it.pdc.jabxy.network.utils;


public interface ServerChannelFacade extends ChannelFacade {
	int getInterestOps();
	void modifyInterestOps (int opsToSet, int opsToReset);
}
