package ar.edu.itba.it.pdc.jabxy.model.jabber;

import org.w3c.dom.Document;

public abstract class Stanza extends JabberMessage {

	private final String type;
	private final String lang;
	private String to;
	private String from;
	
	Stanza(Document doc) {
		super(doc);
		this.type = parseType();
		this.lang = parseLang();
		this.to = parseTo();
		this.from = parseFrom();
	}

	private String parseFrom() {
		return parseAttribute("from");
	}

	private String parseTo() {
		return parseAttribute("to");
	}

	private String parseLang() {
		return parseAttribute("lang");
	}

	private String parseType() {
		return parseAttribute("type");
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getType() {
		return type;
	}

	public String getLang() {
		return lang;
	}
}
