/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * @author till
 *
 */
public class Opportunity extends AbstractCrmObject {

	public Opportunity(String id, String title) {
		super(id, title);
	}

	@Override
	public String getCrmEntityType()
	{
		return "Opportunities";
	}

}
