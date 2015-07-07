/**
 * 
 */
package net.combase.desktopcrm.domain;

import org.joda.time.DateTime;

/**
 * @author till
 *
 */
public class Call extends AbstractCrmObject
{
	private DateTime start;



	public DateTime getStart() {
		return start;
	}



	public void setStart(DateTime start) {
		this.start = start;
	}



	public Call(String id, String title)
	{
		super(id, title);
	}
}
