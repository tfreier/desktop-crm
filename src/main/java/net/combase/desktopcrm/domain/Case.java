/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * 
 * @author "Till Freier"
 *
 */
public class Case extends AbstractCrmObject {

	private String number;

	public Case(String id, String title) {
		super(id, title);
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	@Override
	public String getCrmEntityType()
	{
		return "Cases";
	}


}
