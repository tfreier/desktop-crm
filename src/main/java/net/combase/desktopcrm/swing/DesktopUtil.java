/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.closebutton.NullCloseButton;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;

/**
 * @author till
 *
 */
public class DesktopUtil {

	private static void open(URI uri)
	{
		if (open("kde-open", uri) || open("gnome-open", uri) || open("open", uri) ||
			open("xdg-open", uri) || open("explorer", uri))
			return;

		System.err.println("no open command worked for " + uri.toString());
	}

	private static boolean open(String util, URI uri)
	{
		try
		{
			String command = util + " " + uri.toString() + "";
			command = command.replace("+", "%20");
			System.out.println(command);
			Runtime.getRuntime().exec(command);

			return true;
		}
		catch (IOException e1)
		{
		}

		return false;
	}

	public static void openBrowser(String url)
	{
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE))
		try
		{
			Desktop.getDesktop().browse(new URI(url));

			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			}

		try
		{
			open(new URI(url));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}


	public static void openHtmlEmail(String mailTo, String subject, String body)
	{
		try
		{
			Message message = new MimeMessage(Session.getInstance(System.getProperties()));
			// message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			// create the message part
			MimeBodyPart content = new MimeBodyPart();
			// fill message
			content.setText(body, "UTF-8", "html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(content);
			// add attachments
/*
 * for (File file : attachments) { MimeBodyPart attachment = new MimeBodyPart(); DataSource source =
 * new FileDataSource(file); attachment.setDataHandler(new DataHandler(source));
 * attachment.setFileName(file.getName()); multipart.addBodyPart(attachment); }
 */
			// integration
			message.setContent(multipart);
			// store file
			File file = File.createTempFile("crmMail", ".eml");
			file.deleteOnExit();
			message.writeTo(new FileOutputStream(file));
			open(file.toURI());
		}
		catch (MessagingException | IOException ex)
		{
			ex.printStackTrace();
		}

	}

	public static void openEmail(String mailTo, String subject)
	{
		openEmail(mailTo, subject, "");
	}

	public static void openEmail(String mailTo, String subject, String body)
	{
		URI uri = null;
		if (subject == null)
			subject = "";
		if (body == null)
			body = "";
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append("mailto:").append(mailTo);
			sb.append("?SUBJECT=").append(URLEncoder.encode(subject, "UTF-8"));
			sb.append("&BODY=").append(URLEncoder.encode(body, "UTF-8"));

			uri = new URI(sb.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.MAIL))
			try
			{
				Desktop.getDesktop().mail(uri);

				return;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		open(uri);

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
