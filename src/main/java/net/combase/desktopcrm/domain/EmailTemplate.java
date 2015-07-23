/**
 * 
 */
package net.combase.desktopcrm.domain;

/**
 * 
 * @author "Till Freier"
 *
 */
public class EmailTemplate extends AbstractCrmObject {

	private String body;
	private String htmlBody;
	private String subject;

	public EmailTemplate(String id, String title) {
		super(id, title);
	}


	public String getHtmlBody()
	{
		return htmlBody;
	}


	public void setHtmlBody(String htmlBody)
	{
		this.htmlBody = htmlBody;
	}


	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}


}
