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
	
	private String extendedTitle;
	
	private String phoneInfo;
	
	
	
	/**
	 * @return the phoneInfo
	 */
	public String getPhoneInfo()
	{
		return phoneInfo;
	}

	/**
	 * @param phoneInfo the phoneInfo to set
	 */
	public void setPhoneInfo(String phoneInfo)
	{
		this.phoneInfo = phoneInfo;
	}

	/**
	 * @return the extendedTitle
	 */
	public String getExtendedTitle()
	{
		return extendedTitle;
	}

	/**
	 * @param extendedTitle the extendedTitle to set
	 */
	public void setExtendedTitle(String extendedTitle)
	{
		this.extendedTitle = extendedTitle;
	}

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
