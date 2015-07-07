package com.sugarcrm.api.v4.impl;

import com.sugarcrm.api.SugarSession;
import com.sugarcrm.api.User;



/**
 * Sugar login API response in v4
 * @author mmarum
 */
public class SugarLoginResponse extends SugarBean implements SugarSession
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1676345351737529385L;


	@Override
	public String getSessionID()
	{
		return this.id;
	}


	@Override
	public User getUser()
	{
		final User user = new UsersResponse(this.values, this.moduleName);
		return user;
	}

}
