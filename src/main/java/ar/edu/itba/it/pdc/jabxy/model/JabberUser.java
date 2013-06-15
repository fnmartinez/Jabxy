package ar.edu.itba.it.pdc.jabxy.model;

import java.net.InetSocketAddress;

import ar.edu.itba.it.pdc.jabxy.model.filters.FilterChain;
import ar.edu.itba.it.pdc.jabxy.model.transformations.TransformationChain;

public class JabberUser {
	private final InetSocketAddress defaultServerAddress;
	private String jId;
	private InetSocketAddress serverAddress;
	private FilterChain filters;
	private TransformationChain transformations;

	public JabberUser(String jId, InetSocketAddress defaultServerAddress) {
		this.jId = jId;
		this.defaultServerAddress = defaultServerAddress;
		this.filters = new FilterChain();
		this.transformations = new TransformationChain();
	}

	public InetSocketAddress getServerAddress() {
		if (serverAddress == null) {
			return defaultServerAddress;
		}

		return serverAddress;
	}

	public String getJID() {
		return this.jId;
	}

	public FilterChain getFilters() {
		return this.filters;
	}

	public TransformationChain getTransformations() {
		return this.transformations;
	}
}
