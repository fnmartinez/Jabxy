package ar.edu.itba.it.pdc.jabxy.model;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import ar.edu.itba.it.pdc.jabxy.model.filters.DumbFilter;
import ar.edu.itba.it.pdc.jabxy.model.filters.Filter;
import ar.edu.itba.it.pdc.jabxy.model.transformations.Transformation;

public class JabberUser {
	private final InetSocketAddress defaultServerAddress;
	private String jId;
	private InetSocketAddress serverAddress;
	private Filter filters;
	private Transformation transformations;

	public JabberUser(String jId, InetSocketAddress defaultServerAddress) {
		this.jId = jId;
		this.defaultServerAddress = defaultServerAddress;
		this.filters = new DumbFilter();
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

	public Filter getFilter() {
		return this.filters;
	}

	public void addFilter(Class<? extends Filter> clazz)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.filters = clazz.getDeclaredConstructor(Filter.class).newInstance(
				this.filters);
	}

	public Transformation getTransformations() {
		return this.transformations;
	}

	public void addTransformation(Class<? extends Transformation> clazz)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		this.transformations = clazz.getDeclaredConstructor(
				Transformation.class).newInstance(this.transformations);
	}

}
