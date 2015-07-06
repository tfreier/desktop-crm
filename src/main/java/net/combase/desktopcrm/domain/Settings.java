/**
 * 
 */
package net.combase.desktopcrm.domain;

import java.io.Serializable;

/**
 * @author till
 *
 */
public class Settings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2756760963012353808L;
	private String crmUrl;
	private String user;
	private String password;
	public String getCrmUrl() {
		return crmUrl;
	}
	public void setCrmUrl(String crmUrl) {
		this.crmUrl = crmUrl;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
