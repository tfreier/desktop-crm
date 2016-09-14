/**
 * 
 */
package net.combase.desktopcrm.domain;

import org.joda.time.DateTime;

/**
 * @author till
 *
 */
public class Meeting extends AbstractCrmObject
{
    private DateTime start;
    private String description;

    public String getDescription()
    {
	return description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public DateTime getStart()
    {
	return start;
    }

    public void setStart(DateTime start)
    {
	this.start = start;
    }

    public Meeting()
    {
	super();
    }

    public Meeting(String id, String title)
    {
	super(id, title);
    }

    @Override
    public String getCrmEntityType()
    {
	return "Calls";
    }
}
