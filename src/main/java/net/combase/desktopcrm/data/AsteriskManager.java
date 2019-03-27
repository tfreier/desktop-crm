/**
 * 
 */
package net.combase.desktopcrm.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.Extension;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeUser;

import net.combase.desktopcrm.domain.Settings;



/**
 * yealink call url:
 * http://10.1.0.11/cgi-bin/ConfigManApp.com?number=
 * 
 * @author "Till Freier"
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


	public static void dial(String number)
	{
		final Settings settings = DataStoreManager.getSettings();
		String dialUrl = settings.getDialUrl();

		if (dialUrl == null || dialUrl.trim().isEmpty())
		{
			System.out.println("no dial url configured");
			return;
		}

		number = number.replaceAll("[^0-9]*", "");

		String urlStr = dialUrl.replaceAll("\\$0", number);
		System.out.println("dial url: " + urlStr);
		try
		{
			HttpGet get = new HttpGet(urlStr);
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
			provider.setCredentials(AuthScope.ANY, credentials);
			HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("url call status code: " + statusCode);
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		if (number != null && number.matches("^[0-9]*$") && number.length() > 5)
			return true;

		System.out.println("ignore call " + number);

		return false;
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
