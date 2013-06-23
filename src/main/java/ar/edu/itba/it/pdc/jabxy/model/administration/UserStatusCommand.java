package ar.edu.itba.it.pdc.jabxy.model.administration;

import java.util.ArrayList;

import ar.edu.itba.it.pdc.jabxy.model.JabxyUser;
import ar.edu.itba.it.pdc.jabxy.model.administration.AdminProtocol.AdminProtocolActions;
import ar.edu.itba.it.pdc.jabxy.model.filters.FilterChain;
import ar.edu.itba.it.pdc.jabxy.model.transformations.TransformationChain;
import ar.edu.itba.it.pdc.jabxy.services.UserService;

public class UserStatusCommand implements Command {

	@Override
	public String getName() {
		return "status";
	}

	@Override
	public String execute(String[] args) {
		
		ArrayList<JabxyUser> users = new ArrayList<JabxyUser>();
		
		if(args.length == 1 && args[0].equalsIgnoreCase("all")){
			users = UserService.getAllUsers();
		}else{
			for(String user : args){
				JabxyUser u = UserService.getUser(user);
				if(u == null){
					return "ERROR: usuario '"+user+"' inexistente.";
				}
				users.add(u);
			}
		}
		
		String res = "";
		
		for(JabxyUser user : users){
			FilterChain filters = user.getFilters();
			TransformationChain transformations = user.getTransformations();
			res += "\nUsuario "+user.getJID()+" aplicando "+filters.countFilters()+" filtros y "+transformations.countTransformations()+"transformaciones.";
		}
		
		return res;
	}

	@Override
	public String shortHelp() {
		return "[get|set|help] status [<USERNAME>|all]";
	}

	@Override
	public String descriptiveHelp() {
		return "Con este comando se podra ver el estado de los usuarios.\n[get|set|help] status [<USERNAME>|all]";
	}

	@Override
	public boolean acceptsAction(AdminProtocolActions action) {
		//TODO: ver que onda con esto...
		return true;
	}

}
