package com.sugarcrm.api.v4.impl;

import com.google.gson.annotations.SerializedName;



/**
 * @author Till Freier
 */
public class GetEntryListResponse
{

	@SerializedName("entry_list")
	protected SugarBeanReference[] entryList;


	public SugarBeanReference[] getEntryList()
	{
		return this.entryList;
	}
}
