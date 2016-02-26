/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * 
 * @author "Till Freier"
 *
 */
public class Contact extends Lead
{
	private String accountId;


	public Contact(String id, String title)
	{
		super(id, title);
	}

	@Override
	public String getCrmEntityType()
	{
		return "Contacts";
	}


	public String getAccountId()
	{
		return accountId;
	}


	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

}
