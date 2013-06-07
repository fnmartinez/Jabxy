package ar.edu.itba.it.pdc.jabxy.tests;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandler;
import ar.edu.itba.it.pdc.jabxy.network.handler.EventHandlerFactory;
import ar.edu.itba.it.pdc.jabxy.network.utils.ChannelFacade;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: Mar 19, 2007
 * Time: 3:22:29 PM
 */
public class NadaProtocol implements EventHandlerFactory
{
	Map<ChannelFacade, NadaUser> users =
		Collections.synchronizedMap (new HashMap<ChannelFacade, NadaUser>());

	// --------------------------------------------------
	// Implementation of InputHandlerFactory interface

	public EventHandler newHandler() throws IllegalAccessException, InstantiationException
	{
		return new NadaHandler (this);
	}

	// --------------------------------------------------

	void newUser (ChannelFacade facade)
	{
		NadaUser user = new NadaUser (facade);

		users.put (facade, user);
		user.send (ByteBuffer.wrap ((user.getNickName() + "\n").getBytes()));
	}

	void endUser (ChannelFacade facade)
	{
		users.remove (facade);
	}

	public void handleMessage (ChannelFacade facade, ByteBuffer message)
	{
		broadcast (users.get (facade), message);
	}

	private void broadcast (NadaUser sender, ByteBuffer message)
	{
		synchronized (users) {
			for (NadaUser user : users.values()) {
				if (user != sender) {
					sender.sendTo (user, message);
				}
			}
		}
	}

	// ----------------------------------------------------

	private static class NadaUser
	{
		private final ChannelFacade facade;
		private String nickName;
		private ByteBuffer prefix = null;
	private static int counter = 1;

		public NadaUser (ChannelFacade facade)
		{
			this.facade = facade;
	setNickName ("nick-" + counter++);
		}

		public void send (ByteBuffer message)
		{
			facade.outputQueue().enqueue (message.asReadOnlyBuffer());
		}

		public void sendTo (NadaUser recipient, ByteBuffer message)
		{
			recipient.send (prefix);
			recipient.send (message);
		}

		public String getNickName ()
		{
			return nickName;
		}

		public void setNickName (String nickName)
		{
			this.nickName = nickName;

			String prefixStr = "[" + nickName + "] ";

			prefix = ByteBuffer.wrap (prefixStr.getBytes());
		}
	}
}
