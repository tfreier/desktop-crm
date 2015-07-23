/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * @author "Till Freier"
 *
 */
public class AbstractCrmObject
{
	private String id;
	private String viewUrl;
	private String relatedObjectType;
	private String realtedObjectUrl;
	private String title;


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

	public String getRelatedObjectType()
	{
		return relatedObjectType;
	}

	public void setRelatedObjectType(String relatedObjectType)
	{
		this.relatedObjectType = relatedObjectType;
	}

	public String getRealtedObjectUrl()
	{
		return realtedObjectUrl;
	}

	public void setRealtedObjectUrl(String realtedObjectUrl)
	{
		this.realtedObjectUrl = realtedObjectUrl;
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
