package ar.edu.itba.it.pdc.jabxy.network.queues;

import java.nio.ByteBuffer;

public interface MessageValidator {

	int isValidMessage(ByteBuffer message);
}
