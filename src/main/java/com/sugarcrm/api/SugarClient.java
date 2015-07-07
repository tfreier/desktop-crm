package com.sugarcrm.api;

import java.util.List;

import com.sugarcrm.api.v4.impl.SugarApi;
import com.sugarcrm.api.v4.impl.SugarBean;



/**
 * Sugar Client
 * @author mmarum
 */
public class SugarClient
{

	private SugarApi sugar = null;


	public SugarClient(final String sugarUrl)
	{
		// Only support 1 version of the API right now
		this.sugar = new SugarApi(sugarUrl);
	}


	public List<SugarEntity> findBeans(final SugarSession session, final String moduleName, final String query, final int offset, final int limit) throws SugarApiException
	{
		return this.sugar.getFindBeans(session, moduleName, query, offset, limit);
	}


	public SugarEntity getBean(final SugarSession session, final String moduleName, final String guid) throws SugarApiException
	{
		return this.sugar.getBean(session, moduleName, guid);
	}


	public SugarSession getSugarSession(final String userId, final String password) throws SugarApiException
	{
		return this.sugar.getSugarSession(new SugarCredentials(userId, password));
	}


	public SugarSession getSugarSession(final SugarCredentials credentials) throws SugarApiException
	{
		return this.sugar.getSugarSession(credentials);
	}


	public String setBean(final SugarSession session, final SugarBean bean) throws SugarApiException
	{
		return this.sugar.setBean(session, bean);
	}


	public void setRelationsship(final SugarSession session, final String moduleOne, final String moduleOneId, final String moduleTwo, final String moduleTwoId, final boolean delete) throws SugarApiException
	{
		this.sugar.setRelationsship(session, moduleOne, moduleOneId, moduleTwo, moduleTwoId, delete);
	}

}
