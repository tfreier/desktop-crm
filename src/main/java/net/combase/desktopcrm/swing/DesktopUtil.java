/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.closebutton.NullCloseButton;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;

/**
 * @author till
 *
 */
public class DesktopUtil {
	public static void openBrowser(String url)
	{
		try
		{
			Desktop.getDesktop().browse(new URI(url));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				Runtime.getRuntime().exec("google-chrome " + url);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	
	public static NotificationBuilder createNotificationBuilder()
	{
		NotificationBuilder nb = new NotificationBuilder();
		LightDefaultNotification style = new LightDefaultNotification();
		style.withCloseButton(new NullCloseButton());
		nb.withStyle(style);
		nb.withFadeInAnimation(true);
		nb.withFadeOutAnimation(true);
		nb.withPosition(Positions.NORTH_EAST);
		nb.withDisplayTime(10000);
		
		return nb;
	}
}
