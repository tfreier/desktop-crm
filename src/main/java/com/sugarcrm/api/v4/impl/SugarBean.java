package com.sugarcrm.api.v4.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.annotations.SerializedName;
import com.sugarcrm.api.SugarEntity;



/**
 * SugarBean response for the v4 API
 * @author mmarum
 */
public class SugarBean extends SugarBeanReference implements com.sugarcrm.api.SugarEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8177933963645181958L;

	@SerializedName("name_value_list")
	protected HashMap<String, HashMap<String, String>> values;


	// Needed for Gson
	public SugarBean()
	{

	}


	public SugarBean(final HashMap<String, HashMap<String, String>> name_value_list, final String module_name)
	{
		super(module_name);
		this.values = name_value_list;
	}


	public SugarBean(final HashMap<String, HashMap<String, String>> name_value_list, final String module_name, final String guid)
	{
		super(module_name, guid);
		this.values = name_value_list;
	}


	public SugarBean(final String module_name)
	{
		this(new HashMap<String, HashMap<String, String>>(), module_name);
	}


	public SugarBean(final String module_name, final String guid)
	{
		this(new HashMap<String, HashMap<String, String>>(), module_name, guid);
	}


	public SugarBean(final SugarEntity entity)
	{
		this(entity.getModuleName(), entity.getId());

		for (final Map<String, String> fieldMap : entity.getData())
		{
			set(fieldMap.get("name"), fieldMap.get("value"));
		}
	}


	@Override
	public String get(final String fieldName)
	{
		return this.values.get(fieldName).get("value");
	}


	@Override
	public ArrayList<HashMap<String, String>> getData()
	{
		final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (final Entry<String, HashMap<String, String>> entry : this.values.entrySet())
		{
			final HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", entry.getKey());
			map.put("value", entry.getValue().get("value"));
			list.add(map);
		}

		return list;
	}


	@Override
	public Collection<String> getFieldNames()
	{
		return this.values.keySet();
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
		HashMap<String, String> hashMap = this.values.get(fieldName);
		if (hashMap == null)
			this.values.put(fieldName, hashMap = new HashMap<String, String>());

		return hashMap.put("value", value);
	}

}
