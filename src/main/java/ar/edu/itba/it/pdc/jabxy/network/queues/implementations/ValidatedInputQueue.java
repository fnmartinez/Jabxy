package ar.edu.itba.it.pdc.jabxy.network.queues.implementations;

import java.nio.ByteBuffer;

import ar.edu.itba.it.pdc.jabxy.network.queues.MessageValidator;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public abstract class ValidatedInputQueue extends BasicInputQueue {

	protected MessageValidator validator;
	
	public ValidatedInputQueue(BufferFactory bufferFactory, MessageValidator validator) {
		super(bufferFactory);
		this.validator = validator;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() || validator.isValidMessage(getCurrentMessage()) != -1;
	}
	
	public abstract ByteBuffer dequeueValidatedMessage();
	
}
