/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.util.Collection;

import org.joda.time.DateTime;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.CallType;
import net.combase.desktopcrm.domain.Contact;

/**
 * @author "Till Freier"
 *
 */
public class CrmTest
{
	public static void main(String[] args)
	{
		
		CrmManager.setup();
		
		Collection<Contact> findContacts = CrmManager.findContacts("Till");
		
		Call c = new Call();
		c.setStart(new DateTime());
		if (findContacts != null && !findContacts.isEmpty())
		{
			Contact contact = findContacts.iterator().next();
			c.setRelatedObjectId(contact.getAccountId());
			c.setRelatedObjectType("Accounts");
			c.setContactId(contact.getId());
			
		}
		c.setTitle("Test Call");
		c.setDescription("just a test");
		c.setType(CallType.OUTBOUND);

		CrmManager.createCall(c);
	}
}
