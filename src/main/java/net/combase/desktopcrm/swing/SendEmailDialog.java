/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.combase.desktopcrm.data.CrmHelper;
import net.combase.desktopcrm.domain.EmailTemplate;
import net.combase.desktopcrm.domain.HasEmail;

/**
 * @author "Till Freier"
 *
 */
public abstract class SendEmailDialog
{
	public static void sendEmail(JFrame frame, String subject, Collection<? extends HasEmail> to)
	{
		Collection<EmailTemplate> temps = CrmHelper.getCachedEmailTemplates();
		Object[] possibilities = temps.toArray();
		EmailTemplate s = (EmailTemplate)JOptionPane.showInputDialog(frame,
			"Do you want to use en email template?", "Send email...", JOptionPane.YES_NO_OPTION,
			CrmIcons.MAIL, possibilities, null);


		StringBuilder toSB = new StringBuilder();
		for (HasEmail contact : to)
			toSB.append(contact.getEmail()).append(',');
		if (toSB.length() > 0)
			toSB.deleteCharAt(toSB.length() - 1);

		// If a string was returned, say so.
		if ((s != null))
		{
			if (subject == null || subject.trim().isEmpty())
				subject = s.getSubject();

			String body = s.getBody();
			Iterator<? extends HasEmail> iterator = to.iterator();
			if (iterator.hasNext())
			{
				HasEmail next = iterator.next();
				body = body.replace("$contact_name", next.getFirstname());
				body = body.replace("$contact_first_name", next.getFirstname());
			}

			DesktopUtil.openEmail(toSB.toString(), subject, body);
		}
		else
			DesktopUtil.openEmail(toSB.toString(), subject);

	}
}
