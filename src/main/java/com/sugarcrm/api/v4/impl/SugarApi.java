package com.sugarcrm.api.v4.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.sugarcrm.api.SugarApiException;
import com.sugarcrm.api.SugarCredentials;
import com.sugarcrm.api.SugarEntity;
import com.sugarcrm.api.SugarSession;



/**
 * Sugar API v4 specific stuff
 * 
 * @author mmarum
 * @author Till Freier
 */
public class SugarApi
{

	public class GetEntryListRequest
	{
		protected String session;

		@SerializedName("module_name")
		protected String moduleName;

		protected String query;

		@SerializedName("order_by")
		protected String orderBy = "";

		protected int offset = 0;

		protected int deleted = 0;

		@SerializedName("max_results")
		protected int maxResults = 10;


		public GetEntryListRequest(final String session, final String moduleName)
		{
			this.session = session;
			this.moduleName = moduleName;
		}


		public int getDeleted()
		{
			return this.deleted;
		}


		public int getMaxResults()
		{
			return this.maxResults;
		}


		public String getModuleName()
		{
			return this.moduleName;
		}


		public int getOffset()
		{
			return this.offset;
		}


		public String getOrderBy()
		{
			return this.orderBy;
		}


		public String getQuery()
		{
			return this.query;
		}


		public String getSession()
		{
			return this.session;
		}


		public void setDeleted(final int deleted)
		{
			this.deleted = deleted;
		}


		public void setMaxResults(final int maxResults)
		{
			this.maxResults = maxResults;
		}


		public void setModuleName(final String moduleName)
		{
			this.moduleName = moduleName;
		}


		public void setOffset(final int offset)
		{
			this.offset = offset;
		}


		public void setOrderBy(final String orderBy)
		{
			this.orderBy = orderBy;
		}


		public void setQuery(final String query)
		{
			this.query = query;
		}


		public void setSession(final String session)
		{
			this.session = session;
		}

	}


	public class GetEntryRequest
	{

		protected String session;

		@SerializedName("module_name")
		protected String moduleName;

		protected String id;


		public GetEntryRequest(final String session, final String moduleName, final String id)
		{
			this.session = session;
			this.moduleName = moduleName;
			this.id = id;
		}

	}


	public class NameValue
	{
		protected String name;

		protected String value;
	}


	public class SetEntryRequest
	{

		protected String session;

		@SerializedName("module_name")
		protected String moduleName;

		@SerializedName("name_value_list")
		protected ArrayList<HashMap<String, String>> nameValueList;

		protected boolean track_view;


		public SetEntryRequest(final String session, final String moduleName, final ArrayList<HashMap<String, String>> nameValueList, final boolean track_view)
		{
			this.session = session;
			this.moduleName = moduleName;
			this.nameValueList = nameValueList;
			this.track_view = track_view;
		}

	}


	public class SetRelationshipRequest
	{

		protected String session;

		@SerializedName("module_name")
		protected String moduleName;

		@SerializedName("module_id")
		protected String moduleId;

		@SerializedName("link_field_name")
		protected String linkFieldName;

		@SerializedName("related_ids")
		protected ArrayList<String> relatedIds;

		private int delete = 0;


		public SetRelationshipRequest(final String session)
		{
			this.session = session;
		}


		public int getDelete()
		{
			return this.delete;
		}


		public String getLinkFieldName()
		{
			return this.linkFieldName;
		}


		public String getModuleId()
		{
			return this.moduleId;
		}


		public String getModuleName()
		{
			return this.moduleName;
		}


		public ArrayList<String> getRelatedIds()
		{
			return this.relatedIds;
		}


		public String getSession()
		{
			return this.session;
		}


		public void setDelete(final int delete)
		{
			this.delete = delete;
		}


		public void setLinkFieldName(final String linkFieldName)
		{
			this.linkFieldName = linkFieldName;
		}


		public void setModuleId(final String moduleId)
		{
			this.moduleId = moduleId;
		}


		public void setModuleName(final String moduleName)
		{
			this.moduleName = moduleName;
		}


		public void setRelatedIds(final ArrayList<String> relatedIds)
		{
			this.relatedIds = relatedIds;
		}


		public void setSession(final String session)
		{
			this.session = session;
		}

	}

	public class GetRelationshipRequest
	{

		protected String session;

		@SerializedName("module_name")
		protected String moduleName;

		@SerializedName("module_id")
		protected String moduleId;

		@SerializedName("link_field_name")
		protected String linkFieldName;

		@SerializedName("related_module_query")
		protected String relatedModuleQuery;


		@SerializedName("related_fields")
		protected ArrayList<String> relatedFields;


		public GetRelationshipRequest(final String session)
		{
			this.session = session;
		}


		public String getRelatedModuleQuery()
		{
			return relatedModuleQuery;
		}


		public void setRelatedModuleQuery(String relatedModuleQuery)
		{
			this.relatedModuleQuery = relatedModuleQuery;
		}


		public ArrayList<String> getRelatedFields()
		{
			return relatedFields;
		}


		public void setRelatedFields(ArrayList<String> relatedFields)
		{
			this.relatedFields = relatedFields;
		}


		public String getLinkFieldName()
		{
			return this.linkFieldName;
		}


		public String getModuleId()
		{
			return this.moduleId;
		}


		public String getModuleName()
		{
			return this.moduleName;
		}

		public String getSession()
		{
			return this.session;
		}


		public void setLinkFieldName(final String linkFieldName)
		{
			this.linkFieldName = linkFieldName;
		}


		public void setModuleId(final String moduleId)
		{
			this.moduleId = moduleId;
		}


		public void setModuleName(final String moduleName)
		{
			this.moduleName = moduleName;
		}

		public void setSession(final String session)
		{
			this.session = session;
		}

	}

	public class SugarLoginRequest
	{
		protected SugarCredentials user_auth;


		public void setUserAuth(final SugarCredentials auth)
		{
			this.user_auth = auth;
		}
	}

	private String REST_ENDPOINT = null;

	private URLCodec codec = null;

	private Gson json = null;


	public SugarApi(final String sugarUrl)
	{
		this.REST_ENDPOINT = sugarUrl + "/service/v4_1/rest.php";
		this.json = new GsonBuilder().create();
		this.codec = new URLCodec();
	}


	public SugarEntity getBean(final SugarSession session, final String moduleName, final String guid) throws SugarApiException
	{
		final String sessionId = session.getSessionID();
		final GetEntryRequest req = new GetEntryRequest(sessionId, moduleName, guid);
		String response = null;
		try
		{
			response = postToSugar(this.REST_ENDPOINT + "?method=get_entry&response_type=JSON&input_type=JSON&rest_data=" + this.codec.encode(this.json.toJson(req)));
		}
		catch (final EncoderException e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}

		final GetEntryResponse entryResp = this.json.fromJson(response, GetEntryResponse.class);
		if (entryResp.getEntryList() == null)
		{
			final ErrorResponse error = this.json.fromJson(response, ErrorResponse.class);
			final SugarApiException ex = new SugarApiException(error.getName());
			ex.setDescription(error.getDescription());
			ex.setNumber(error.getNumber());
			throw ex;
		}
		if (entryResp.getEntryList().length > 0)
		{
			return entryResp.getEntryList()[0];
		}

		return null;
	}


	public List<SugarEntity> getFindBeans(final SugarSession session, final String moduleName,
		final String query, final int offset, final int limit, final String orderBy)
		throws SugarApiException
	{
		final String sessionId = session.getSessionID();
		final GetEntryListRequest req = new GetEntryListRequest(sessionId, moduleName);
		req.setQuery(query);
		req.setMaxResults(limit);
		req.setOffset(offset);
		if (orderBy != null)
			req.setOrderBy(orderBy);

		String response = null;
		try
		{
			String jsonStr = this.json.toJson(req);
			response = postToSugar(this.REST_ENDPOINT + "?method=get_entry_list&response_type=JSON&input_type=JSON&rest_data=" + this.codec.encode(jsonStr));
		}
		catch (final EncoderException e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}

		final GetEntryListResponse entryResp = this.json.fromJson(response, GetEntryListResponse.class);
		final SugarBeanReference[] entryList = entryResp.getEntryList();
		if (entryList == null)
		{
			final ErrorResponse error = this.json.fromJson(response, ErrorResponse.class);
			final SugarApiException ex = new SugarApiException(error.getName());
			ex.setDescription(error.getDescription());
			ex.setNumber(error.getNumber());
			throw ex;
		}

		final List<SugarEntity> result = new ArrayList<SugarEntity>(entryList.length);
		for (final SugarBeanReference ref : entryList)
		{
			result.add(getBean(session, moduleName, ref.getId()));
		}

		return result;
	}


	public SugarSession getSugarSession(final SugarCredentials credentials) throws SugarApiException
	{

		final SugarLoginRequest loginReq = new SugarLoginRequest();
		loginReq.setUserAuth(credentials);

		SugarLoginResponse jResp = null;
		try
		{
			final String response = postToSugar(this.REST_ENDPOINT + "?method=login&response_type=JSON&input_type=JSON&rest_data=" + this.codec.encode(this.json.toJson(loginReq)));
			jResp = this.json.fromJson(response, SugarLoginResponse.class);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SugarApiException("Sugar Login failed", e);
		}
		return jResp;
	}


	public String postToSugar(final String urlStr) throws Exception
	{
		final URL url = new URL(urlStr);
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		if (conn.getResponseCode() != 200)
		{
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		final StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null)
		{
			sb.append(line);
		}
		rd.close();

		conn.disconnect();

		return sb.toString();
	}


	public String postToSugar(final String urlStr, final String content) throws Exception
	{
		final URL url = new URL(urlStr);
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("Content-Length", "" + Integer.toString(content.getBytes().length));
		conn.setUseCaches(false);

		if (System.getenv("sugardebug") != null)
		{
			System.out.println("post content: " + content);
		}

		final DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(content);
		wr.flush();
		wr.close();

		if (conn.getResponseCode() != 200)
		{
			// Buffer the result into a string
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				System.err.println(line);
			}
			rd.close();

			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		final StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null)
		{
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		if (System.getenv("sugardebug") != null)
		{
			System.out.println(sb.toString());
		}
		return sb.toString();
	}


	public String setBean(final SugarSession session, final SugarEntity bean) throws SugarApiException
	{
		final String sessionId = session.getSessionID();
		bean.set("id", bean.getId());
		final ArrayList<HashMap<String, String>> data = bean.getData();
		final SetEntryRequest req = new SetEntryRequest(sessionId, bean.getModuleName(), data, true);
		String response = null;
		try
		{
			final String jsonData = this.json.toJson(req);
			if (System.getenv("sugardebug") != null)
			{
				System.out.println(jsonData);
			}
			response = postToSugar(this.REST_ENDPOINT, "method=set_entry&response_type=JSON&input_type=JSON&rest_data=" + this.codec.encode(jsonData));
		}
		catch (final EncoderException e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}

		if (System.getenv("sugardebug") != null)
		{
			System.out.println("response:");
			System.out.println(response);
		}

		final SetEntryResponse entryResp = this.json.fromJson(response, SetEntryResponse.class);
		if (entryResp.getId() == null)
		{
			final ErrorResponse error = this.json.fromJson(response, ErrorResponse.class);
			final SugarApiException ex = new SugarApiException(error.getName());
			ex.setDescription(error.getDescription());
			ex.setNumber(error.getNumber());
			throw ex;
		}

		return entryResp.getId();
	}


	/**
	 * @param session
	 * @param moduleOne
	 * @param moduleOneId
	 * @param moduleTwo
	 * @param moduleTwoId
	 * @param delete
	 * @throws SugarApiException
	 */
	public void setRelationsship(final SugarSession session, final String moduleOne, final String moduleOneId, final String moduleTwo, final String moduleTwoId, final boolean delete) throws SugarApiException
	{
		final String sessionId = session.getSessionID();
		final SetRelationshipRequest req = new SetRelationshipRequest(sessionId);

		req.setModuleName(moduleOne);
		req.setModuleId(moduleOneId);
		req.setLinkFieldName(moduleTwo);
		req.setRelatedIds(new ArrayList<>(Arrays.asList(moduleTwoId)));
		if (delete)
			req.setDelete(1);

		String response = null;
		try
		{
			final String jsonData = this.json.toJson(req);
			if (System.getenv("sugardebug") != null)
			{
				System.out.println(jsonData);
			}
			response = postToSugar(this.REST_ENDPOINT, "method=set_relationship&response_type=JSON&input_type=JSON&rest_data=" + this.codec.encode(jsonData));
		}
		catch (final EncoderException e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}

		if (System.getenv("sugardebug") != null)
		{
			System.out.println("response:");
			System.out.println(response);
		}
	}

	public List<SugarEntity> getRelationsships(final SugarSession session, final String moduleOne,
		final String moduleOneId, final String moduleTwo, final String query)
		throws SugarApiException
	{
		final String sessionId = session.getSessionID();
		final GetRelationshipRequest req = new GetRelationshipRequest(sessionId);

		req.setModuleName(moduleOne);
		req.setModuleId(moduleOneId);
		req.setLinkFieldName(moduleTwo);
		req.setRelatedModuleQuery(query);
		req.setRelatedFields(new ArrayList<>(Arrays.asList("id")));

		String response = null;
		try
		{
			final String jsonData = this.json.toJson(req);
			if (System.getenv("sugardebug") != null)
			{
				System.out.println(jsonData);
			}

			response = postToSugar(this.REST_ENDPOINT,
				"method=get_relationships&response_type=JSON&input_type=JSON&rest_data=" +
					this.codec.encode(jsonData));

			if (System.getenv("sugardebug") != null)
			{
				System.out.println("response:");
				System.out.println(response);
			}

			final GetEntryListResponse entryResp = this.json.fromJson(response,
				GetEntryListResponse.class);
			final SugarBeanReference[] entryList = entryResp.getEntryList();
			if (entryList == null)
			{
				final ErrorResponse error = this.json.fromJson(response, ErrorResponse.class);
				final SugarApiException ex = new SugarApiException(error.getName());
				ex.setDescription(error.getDescription());
				ex.setNumber(error.getNumber());
				throw ex;
			}

			final List<SugarEntity> result = new ArrayList<SugarEntity>(entryList.length);
			for (final SugarBeanReference ref : entryList)
			{
				result.add(getBean(session, ref.getModuleName(), ref.getId()));
			}

			return result;
		}
		catch (final EncoderException e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SugarApiException("Could not fetch bean.", e);
		}


	}
}
