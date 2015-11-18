/**
 * 
 */
package net.combase.desktopcrm.domain;

import org.joda.time.DateTime;

/**
 * 
 * @author "Till Freier"
 *
 */
public class Task extends AbstractCrmObject
{
	private DateTime due;

	private String parentType;

	private String parentId;

	private String description;


	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getParentType()
	{
		return parentType;
	}

	public void setParentType(String parentType)
	{
		this.parentType = parentType;
	}

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

	@Override
	public String getCrmEntityType()
	{
		return "Tasks";
	}
}
