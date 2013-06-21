package ar.edu.itba.it.pdc.jabxy.model.jabber;

import org.w3c.dom.Document;

public class OpeningStreamMessage extends JabberMessage {
	
	private String to;
	private String from;
	private String id;
	private String lang;
	private String version;

	OpeningStreamMessage(Document doc) {
		super(doc);
		this.to = parseTo();
		this.from = parseFrom();
		this.id = parseId();
		this.lang = parseLang();
		this.version = parseVersion();
	}

	private String parseVersion() {
		return parseAttribute("version");
	}

	private String parseLang() {
		return parseAttribute("lang");
	}

	private String parseId() {
		return parseAttribute("id");
	}

	private String parseFrom() {
		return parseAttribute("from");
	}

	private String parseTo() {
		return parseAttribute("to");
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
