package com.sugarcrm.api.v4.impl;

import com.google.gson.annotations.SerializedName;

/**
 * GetEntry response for v4 API
 * @author mmarum
 *
 */
public class SetEntryResponse {

	@SerializedName("id")
	protected String id;
  
	public String getId()
	{
		return id;
  }
}
