package com.sugarcrm.api.v4.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.sugarcrm.api.SugarEntity;



/**
 * SugarBean response for the v4 API
 * @author Till Freier
 */
public class SugarBeanReference implements Serializable, SugarEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6410638831658700281L;

	@SerializedName("module_name")
	protected String moduleName;

	@SerializedName("id")
	protected String id;


	// Needed for Gson
	public SugarBeanReference()
	{

	}


	public SugarBeanReference(final String module_name)
	{
		this.moduleName = module_name;
	}


	public SugarBeanReference(final String module_name, final String guid)
	{
		this(module_name);

		this.id = guid;
	}


	public SugarBeanReference(final SugarEntity entity)
	{
		this(entity.getModuleName(), entity.getId());

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String get(final String fieldName)
	{
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<HashMap<String, String>> getData()
	{
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getFieldNames()
	{
		return null;
	}


	@Override
	public String getId()
	{
		return this.id;
	}


	@Override
	public String getModuleName()
	{
		return this.moduleName;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String set(final String fieldName, final String value)
	{
		return null;
	}

}
