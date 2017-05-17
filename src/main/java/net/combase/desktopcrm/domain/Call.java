/**
 * 
 */
package net.combase.desktopcrm.domain;

import org.joda.time.DateTime;



/**
 * @author till
 */
public class Call extends AbstractCrmObject
{
	private DateTime start;

	private CallType type;

	private String description;

	private boolean planned;

	private String parentType;

	private String parentId;
	
	private String extendedTitle;

	

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


	/**
	 * @return the parentType
	 */
	public String getParentType()
	{
		return parentType;
	}


	/**
	 * @param parentType the parentType to set
	 */
	public void setParentType(String parentType)
	{
		this.parentType = parentType;
	}


	/**
	 * @return the parentId
	 */
	public String getParentId()
	{
		return parentId;
	}


	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}


	/**
	 * @return the planned
	 */
	public boolean isPlanned()
	{
		return planned;
	}


	/**
	 * @param planned the planned to set
	 */
	public void setPlanned(boolean planned)
	{
		this.planned = planned;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	public CallType getType()
	{
		return type;
	}


	public void setType(CallType type)
	{
		this.type = type;
	}


	public DateTime getStart()
	{
		return start;
	}


	public void setStart(DateTime start)
	{
		this.start = start;
	}


	public Call()
	{
		super();
	}


	public Call(String id, String title)
	{
		super(id, title);
	}


	@Override
	public String getCrmEntityType()
	{
		return "Calls";
	}
}
