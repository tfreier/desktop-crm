package com.sugarcrm.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Generic Bean API (mostly for accessing custom fields not defined within an existing API)
 * 
 * @author mmarum
 * 
 */

public interface SugarEntity
{
	String get(String fieldName);

	ArrayList<HashMap<String, String>> getData();

	Collection<String> getFieldNames();

	String getId();

	String getModuleName();

	String set(String fieldName, String value);
}
