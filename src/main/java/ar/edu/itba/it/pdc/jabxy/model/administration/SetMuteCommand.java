package ar.edu.itba.it.pdc.jabxy.model.administration;

import ar.edu.itba.it.pdc.jabxy.model.administration.AdminProtocol.AdminProtocolActions;

public class SetMuteCommand implements Command {

	@Override
	public String getName() {
		return "mute";
	}

	@Override
	public String execute(String[] args) {
		//TODO: hacer esto!
		
		return "Comando ejecutado correctamente.";
	}

	@Override
	public String shortHelp() {
		return "[set|help] mute [<USERNAME>|all]";
	}

	@Override
	public String descriptiveHelp() {
		return "Con este comando se podra callar a los usuarios.\n[set|help] mute [<USERNAME>|all]";
	}

	@Override
	public boolean acceptsAction(AdminProtocolActions action) {
		return false;
	}

}
