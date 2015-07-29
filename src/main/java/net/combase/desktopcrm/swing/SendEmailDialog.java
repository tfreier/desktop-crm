/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.combase.desktopcrm.data.CrmHelper;
import net.combase.desktopcrm.domain.EmailTemplate;
import net.combase.desktopcrm.domain.HasEmail;
import net.combase.desktopcrm.domain.Lead;

/**
 * @author "Till Freier"
 *
 */
public abstract class SendEmailDialog
{
	public static void sendEmail(JFrame frame, String subject, Collection<? extends HasEmail> to)
	{
		for (HasEmail hasEmail : to)
		{
			if (hasEmail instanceof Lead)
			{
				Lead lead = (Lead)hasEmail;

				sendEmail(frame, subject, to, lead);

				return;
			}
		}

		sendEmail(frame, subject, to, null);
	}

	public static void sendEmail(JFrame frame, String subject, Collection<? extends HasEmail> to,
		Lead contact)
	{
		Collection<EmailTemplate> temps = CrmHelper.getCachedEmailTemplates();
		Object[] possibilities = temps.toArray();
		EmailTemplate s = (EmailTemplate)JOptionPane.showInputDialog(frame,
			"Do you want to use en email template?", "Send email...", JOptionPane.YES_NO_OPTION,
			CrmIcons.MAIL, possibilities, null);


		StringBuilder toSB = new StringBuilder();
		for (HasEmail c : to)
			toSB.append(c.getEmail()).append(',');
		if (toSB.length() > 0)
			toSB.deleteCharAt(toSB.length() - 1);

		// If a string was returned, say so.
		if ((s != null))
		{
			if (subject == null || subject.trim().isEmpty())
				subject = s.getSubject();

			String body = s.getBody();
			if (contact != null)
			{
				body = replaceField(body, "name", contact.getTitle());
				body = replaceField(body, "first_name", contact.getFirstname());
				body = replaceField(body, "last_name", contact.getLastName());
				body = replaceField(body, "account_name", contact.getAccountName());
				body = replaceField(body, "description", contact.getDescription());
				body = replaceField(body, "email1", contact.getEmail());
				body = replaceField(body, "phone_work", contact.getPhone());
				body = replaceField(body, "phone_other", "");
				body = replaceField(body, "title", contact.getJobTitle());
				body = replaceField(body, "primary_address_city", contact.getCity());
				body = replaceField(body, "primary_address_state", contact.getState());
				body = replaceField(body, "primary_address_country", contact.getCountry());
				body = replaceField(body, "primary_address_street", contact.getAddress());
				body = replaceField(body, "primary_address_postalcode", contact.getZip());
				body = replaceField(body, "account_type_c", contact.getType());
			}

			DesktopUtil.openEmail(toSB.toString(), subject, body);
		}
		else
			DesktopUtil.openEmail(toSB.toString(), subject);

	}

	private static String replaceField(String str, String tag, String value)
	{
		str = str.replace("$contact_" + tag, value);
		str = str.replace("$lead_" + tag, value);
		str = str.replace("$prospect_" + tag, value);

		return str;
	}
}
