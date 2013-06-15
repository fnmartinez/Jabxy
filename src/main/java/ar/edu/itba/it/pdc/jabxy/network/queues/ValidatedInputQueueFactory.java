package ar.edu.itba.it.pdc.jabxy.network.queues;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ar.edu.itba.it.pdc.jabxy.network.queues.exceptions.QueueBuildingException;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.ValidatedInputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.implementations.XMLInputQueue;
import ar.edu.itba.it.pdc.jabxy.network.utils.BufferFactory;

public class ValidatedInputQueueFactory extends InputQueueFactory {

	private MessageValidator validator;
	private BufferFactory bufferFactory;
	
	public ValidatedInputQueueFactory(MessageValidator validator,
			BufferFactory bufferFactory) {
		this.bufferFactory = bufferFactory;
		this.validator = validator;
	}

	@Override
	public InputQueue newInputQueue() throws QueueBuildingException {
		if (XMLValidator.class.isAssignableFrom(this.validator.getClass())) {
			try {
				return new XMLInputQueue(bufferFactory, ((XMLValidator)validator));
			} catch (SAXException e) {
				throw new QueueBuildingException(e);
			} catch (ParserConfigurationException e) {
				throw new QueueBuildingException(e);
			}
		}
		return new ValidatedInputQueue(bufferFactory, validator);
	}

}
