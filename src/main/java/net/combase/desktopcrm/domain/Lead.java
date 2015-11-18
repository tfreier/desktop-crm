/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * 
 * @author "Till Freier"
 *
 */
public class Lead extends AbstractCrmObject implements HasEmail
{
	private String email;

	private String firstname;

	private String lastName;

	private String accountName;

	private String campaignId;

	private String type;

	private String description;

	private String jobTitle;

	private String phone;

	private String address;

	private String city;

	private String state;

	private String zip;

	private String country;

	private String mobile;

	public Lead()
	{
		super();
	}

	public Lead(String id, String title)
	{
		super(id, title);
	}


	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	@Override
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(String campaignId)
	{
		this.campaignId = campaignId;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getJobTitle()
	{
		return jobTitle;
	}

	public void setJobTitle(String title)
	{
		this.jobTitle = title;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getZip()
	{
		return zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	@Override
	public String getCrmEntityType()
	{
		return "Leads";
	}


}
