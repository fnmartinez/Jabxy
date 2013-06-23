package ar.edu.itba.it.pdc.jabxy.services;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class ConfigurationService {
	
	public static void init(){
		
		try{
            XMLConfiguration configs = new XMLConfiguration("config.xml");
            int defaultJabberPort = Integer.parseInt(configs.getString("defaultJabberPort"));
            int defaultAdminPort = Integer.parseInt(configs.getString("defaultAdminPort"));        
        }catch(ConfigurationException e){
        	int defaultJabberPort = 5233;
            int defaultAdminPort = 8888;       
        }
		//hacer variables publicas!!
	}
}
