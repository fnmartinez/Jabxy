package ar.edu.itba.it.pdc.jabxy.services;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ar.edu.itba.it.pdc.jabxy.model.JabxyUser;
import ar.edu.itba.it.pdc.jabxy.model.jabber.JabberProtocol;
import ar.edu.itba.it.pdc.jabxy.model.jabber.OpeningStreamMessage;

public class UserService {

	private static Map<String, JabxyUser> knownUsers = new HashMap<String, JabxyUser>();

	public static JabxyUser touchUser(OpeningStreamMessage openingStreamStanza) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(openingStreamStanza.getFrom())) {
			return null;
		}

		if (knownUsers.containsKey(openingStreamStanza.getFrom())) {
			return knownUsers.get(openingStreamStanza.getFrom());
		}

		JabxyUser user = new JabxyUser(openingStreamStanza.getFrom(),
				new InetSocketAddress(openingStreamStanza.getTo(),
						JabberProtocol.DEFAULT_PORT));
		knownUsers.put(openingStreamStanza.getFrom(), user);
		
		return user;
	}
	
	public static JabxyUser getUser(String userName){
		return knownUsers.get(userName);
	}
	
	public static ArrayList<JabxyUser> getAllUsers(){
		
		ArrayList<JabxyUser> users = new ArrayList<JabxyUser>();
		
		for(String userName : knownUsers.keySet()){
			users.add(knownUsers.get(userName));
		}
		return users;
	}
}
