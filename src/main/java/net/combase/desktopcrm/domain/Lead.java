/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * 
 * @author "Till Freier"
 *
 */
public class Lead extends AbstractCrmObject implements HasEmail
{
	private String email;

	private String firstname;

	public Lead(String id, String title)
	{
		super(id, title);
	}

	@Override
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

}
