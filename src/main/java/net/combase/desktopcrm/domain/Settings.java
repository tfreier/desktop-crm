/**
 * 
 */
package net.combase.desktopcrm.domain;

import java.io.Serializable;

/**
 * @author Till Freier
 *
 */
public class Settings implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2756760963012353808L;
    private String crmUrl;
    private String user;
    private String password;
    private int gmtOffset = 0;
    private String accountCriteria;
    private String asteriskHost;
    private String asteriskUser;
    private String asteriskPassword;
    private String asteriskExtension;

    private String dialUrl;

    private boolean callReminder = true;
    private boolean meetingReminder = true;
    private boolean taskReminder = true;
    private boolean leadReminder = true;
    private boolean opportunityReminder = true;
    private boolean caseReminder = true;

    public boolean isMeetingReminder()
    {
	return meetingReminder;
    }

    public void setMeetingReminder(boolean meetingReminder)
    {
	this.meetingReminder = meetingReminder;
    }

    public String getDialUrl()
    {
	return dialUrl;
    }

    public void setDialUrl(String dialUrl)
    {
	this.dialUrl = dialUrl;
    }

    public String getAsteriskHost()
    {
	return asteriskHost;
    }

    public void setAsteriskHost(String asteriskHost)
    {
	this.asteriskHost = asteriskHost;
    }

    public String getAsteriskUser()
    {
	return asteriskUser;
    }

    public void setAsteriskUser(String asteriskUser)
    {
	this.asteriskUser = asteriskUser;
    }

    public String getAsteriskPassword()
    {
	return asteriskPassword;
    }

    public void setAsteriskPassword(String asteriskPassword)
    {
	this.asteriskPassword = asteriskPassword;
    }

    public String getAsteriskExtension()
    {
	return asteriskExtension;
    }

    public void setAsteriskExtension(String asteriskExtension)
    {
	this.asteriskExtension = asteriskExtension;
    }

    public boolean isCallReminder()
    {
	return callReminder;
    }

    public void setCallReminder(boolean callReminder)
    {
	this.callReminder = callReminder;
    }

    public boolean isTaskReminder()
    {
	return taskReminder;
    }

    public void setTaskReminder(boolean taskReminder)
    {
	this.taskReminder = taskReminder;
    }

    public boolean isLeadReminder()
    {
	return leadReminder;
    }

    public void setLeadReminder(boolean leadReminder)
    {
	this.leadReminder = leadReminder;
    }

    public boolean isOpportunityReminder()
    {
	return opportunityReminder;
    }

    public void setOpportunityReminder(boolean opportunityReminder)
    {
	this.opportunityReminder = opportunityReminder;
    }

    public boolean isCaseReminder()
    {
	return caseReminder;
    }

    public void setCaseReminder(boolean caseReminder)
    {
	this.caseReminder = caseReminder;
    }

    public String getAccountCriteria()
    {
	return accountCriteria;
    }

    public void setAccountCriteria(String accountCriteria)
    {
	this.accountCriteria = accountCriteria;
    }

    public int getGmtOffset()
    {
	return gmtOffset;
    }

    public void setGmtOffset(int gmtOffset)
    {
	this.gmtOffset = gmtOffset;
    }

    public String getCrmUrl()
    {
	return crmUrl;
    }

    public void setCrmUrl(String crmUrl)
    {
	this.crmUrl = crmUrl;
    }

    public String getUser()
    {
	return user;
    }

    public void setUser(String user)
    {
	this.user = user;
    }

    public String getPassword()
    {
	return password;
    }

    public void setPassword(String password)
    {
	this.password = password;
    }

}
