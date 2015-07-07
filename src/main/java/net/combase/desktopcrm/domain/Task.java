/**
 * 
 */
package net.combase.desktopcrm.domain;

import org.joda.time.DateTime;

/**
 * @author till
 *
 */
public class Task extends AbstractCrmObject
{
	private DateTime due;


	public DateTime getDue()
	{
		return due;
	}

	public void setDue(DateTime due)
	{
		this.due = due;
	}

	public Task(String id, String title)
	{
		super(id, title);
	}
}
