/**
 * 
 */
package net.combase.desktopcrm.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.combase.desktopcrm.domain.Settings;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.Extension;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeUser;

/**
 * @author "Till Freier"
 *
 */
public final class AsteriskManager
{

	public interface AsteriskCallEventListener
	{
		public void incomingCall(String number);

		public void outgoingCall(String number);
	}

	private static AsteriskServer asteriskServer = null;

	private static String extension = "";

	private static List<AsteriskCallEventListener> listeners = new ArrayList<>();

	private AsteriskManager()
	{
		super();
	}


	public static boolean setup()
	{
		final Settings settings = DataStoreManager.getSettings();

		final String hostname = settings.getAsteriskHost();
		final String username = settings.getAsteriskUser();
		final String password = settings.getAsteriskPassword();
		extension = settings.getAsteriskExtension();

		if (hostname == null || hostname.trim().isEmpty() || username == null ||
			username.trim().isEmpty() || password == null || password.trim().isEmpty())
			return false;

		if (asteriskServer != null)
			asteriskServer.shutdown();

		asteriskServer = new DefaultAsteriskServer(hostname, username, password);

		try
		{
			asteriskServer.addAsteriskServerListener(new AsteriskServerListener()
			{

				@Override
				public void onNewMeetMeUser(MeetMeUser user)
				{
					// no conference call integration at this time

				}

				@Override
				public void onNewAsteriskChannel(AsteriskChannel channel)
				{
					AsteriskManager.onNewAsteriskChannel(channel);
				}
			});
		}
		catch (ManagerCommunicationException e)
		{
			e.printStackTrace();
			asteriskServer = null;
			return false;
		}

		return true;
	}


	private static void onNewAsteriskChannel(AsteriskChannel channel)
	{
		AsteriskManager.onChannelPropertyChange(channel);
		channel.addPropertyChangeListener(new PropertyChangeListener()
		{

			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				Object source = evt.getSource();
				if (source instanceof AsteriskChannel)
				{
					AsteriskChannel ch = (AsteriskChannel)source;
					AsteriskManager.onChannelPropertyChange(ch);
				}
			}
		});
	}


	private static void onChannelPropertyChange(AsteriskChannel channel)
	{
		Extension ext = channel.getCurrentExtension();
		CallerId callerId = channel.getCallerId();
		if (ext == null || callerId == null || ext.getExtension() == null ||
			callerId.getNumber() == null)
			return;

		String number = callerId.getNumber();

		if (ext.getExtension().equals(extension))
		{
			System.out.println("call from " + number);

			if (isValid(number))
				for (AsteriskCallEventListener l : listeners)
				{
					l.incomingCall(number);
				}
		}
		else if (number.equals(extension))
		{
			System.out.println("call to " + ext.getExtension());
			if (isValid(ext.getExtension()))
				for (AsteriskCallEventListener l : listeners)
				{
					l.outgoingCall(ext.getExtension());
				}
		}
	}

	private static boolean isValid(String number)
	{
		return number != null && number.length() > 5 && !number.startsWith("record") &&
			!number.startsWith("monitor");
	}

	public static void addListener(AsteriskCallEventListener l)
	{
		listeners.add(l);
	}

	public static void removeListener(AsteriskCallEventListener l)
	{
		listeners.remove(l);
	}
}
