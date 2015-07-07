package com.sugarcrm.api.v4.impl;

import java.util.HashMap;

import com.sugarcrm.api.User;



/**
 * Users module response in v4 API
 * @author mmarum
 */
public class UsersResponse extends SugarBean implements User
{
	private static final long serialVersionUID = -7680828187928448556L;


	public UsersResponse()
	{
		super();
	}


	public UsersResponse(final HashMap<String, HashMap<String, String>> values_map, final String moduleName)
	{
		super(values_map, moduleName);
	}


	@Override
	public String getUserId()
	{
		return this.values.get("user_id").get("value");
	}


	@Override
	public String getUserLanguage()
	{
		return this.values.get("user_language").get("value");
	}


	@Override
	public String getUserName()
	{
		return this.values.get("user_name").get("value");
	}

}
