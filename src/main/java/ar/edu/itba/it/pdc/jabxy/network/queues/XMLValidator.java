package ar.edu.itba.it.pdc.jabxy.network.queues;

import org.xml.sax.ContentHandler;

public interface XMLValidator extends ContentHandler, MessageValidator {

	int isValidMessage();
}
