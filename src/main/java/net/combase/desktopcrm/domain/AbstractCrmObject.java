/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * @author "Till Freier"
 */
public abstract class AbstractCrmObject
{
	private String id;

	private String viewUrl;

	private String editUrl;

	private String relatedObjectType;

	private String relatedObjectUrl;

	private String relatedObjectId;

	private String title;

	private String activitiesUrl;


	/**
	 * @return the activitiesUrl
	 */
	public String getActivitiesUrl()
	{
		return activitiesUrl;
	}


	/**
	 * @param activitiesUrl the activitiesUrl to set
	 */
	public void setActivitiesUrl(String activitiesUrl)
	{
		this.activitiesUrl = activitiesUrl;
	}


	private String assignedUser;


	public String getAssignedUser()
	{
		return assignedUser;
	}


	public void setAssignedUser(String assignedUser)
	{
		this.assignedUser = assignedUser;
	}


	public abstract String getCrmEntityType();


	public String getRelatedObjectId()
	{
		return relatedObjectId;
	}


	public void setRelatedObjectId(String relatedObjectId)
	{
		this.relatedObjectId = relatedObjectId;
	}


	public AbstractCrmObject()
	{
		super();
	}


	public AbstractCrmObject(String id, String title)
	{
		super();
		this.id = id;
		this.title = title;
	}


	public String getId()
	{
		return id;
	}


	public String getViewUrl()
	{
		return viewUrl;
	}


	public void setViewUrl(String viewUrl)
	{
		this.viewUrl = viewUrl;
	}


	public String getEditUrl()
	{
		return editUrl;
	}


	public void setEditUrl(String editUrl)
	{
		this.editUrl = editUrl;
	}


	public String getRelatedObjectType()
	{
		return relatedObjectType;
	}


	public void setRelatedObjectType(String relatedObjectType)
	{
		this.relatedObjectType = relatedObjectType;
	}


	public String getRelatedObjectUrl()
	{
		return relatedObjectUrl;
	}


	public void setRelatedObjectUrl(String realtedObjectUrl)
	{
		this.relatedObjectUrl = realtedObjectUrl;
	}


	public String getTitle()
	{
		return title;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}


	@Override
	public String toString()
	{
		return title;
	}

}
