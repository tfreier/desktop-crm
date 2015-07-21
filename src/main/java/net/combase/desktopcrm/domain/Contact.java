/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * 
 * @author "Till Freier"
 *
 */
public class Contact extends AbstractCrmObject
{

	private String email;

	public Contact(String id, String title)
	{
		super(id, title);
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}


}
