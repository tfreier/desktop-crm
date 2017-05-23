/**
 * 
 */
package net.combase.desktopcrm.data;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sugarcrm.api.SugarApiException;
import com.sugarcrm.api.SugarCredentials;
import com.sugarcrm.api.SugarEntity;
import com.sugarcrm.api.SugarSession;
import com.sugarcrm.api.v4.impl.SugarApi;
import com.sugarcrm.api.v4.impl.SugarBean;

import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Account;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.CallType;
import net.combase.desktopcrm.domain.Campaign;
import net.combase.desktopcrm.domain.Case;
import net.combase.desktopcrm.domain.Contact;
import net.combase.desktopcrm.domain.EmailTemplate;
import net.combase.desktopcrm.domain.Lead;
import net.combase.desktopcrm.domain.Meeting;
import net.combase.desktopcrm.domain.Opportunity;
import net.combase.desktopcrm.domain.Settings;
import net.combase.desktopcrm.domain.Task;



/**
 * @author till
 */
public class CrmManager
{
	private static interface CrmObjectCreator<T extends AbstractCrmObject>
	{
		T createObject(String id, String title);


		void prepare(T obj, SugarEntity bean);
	}


	private static String createExtendedTitle(String type, String id)
	{
		String result = "";
		if (type != null && id != null && !id.isEmpty())
			switch (type)
			{
				case "Cases":
					Case c = CrmManager.getCase(id);
					if (c != null)
						result = c.getTitle();
					break;
				case "Leads":
					Lead lead = CrmManager.getLead(id);
					if (lead != null)
						result = lead.getTitle();
					if (lead.getAccountName() != null)
						result += " | " + lead.getAccountName();
					break;
				case "Contacts":
					Contact contact = CrmManager.getContact(id);
					if (contact != null)
						result = contact.getFirstname() + " " + contact.getLastName();
					break;
				case "Accounts":
					Account acc = CrmManager.getAccount(id);
					if (acc != null)
						result = acc.getTitle();
					break;
				case "Prospects":
					break;
				case "Opportunities":
					Opportunity o = CrmManager.getOpprtunity(id);
					if (o != null)
						result = o.getTitle();
					Collection<Contact> contacts = CrmManager.getContactListByOpportunity(id);
					if (contacts != null && !contacts.isEmpty())
					{
						Contact next = contacts.iterator().next();
						result += "; " + next.getTitle();
					}
					break;
				default:
					break;
			}

		return result;
	}


	private static final CrmObjectCreator<Task> TASK_CREATOR = new CrmObjectCreator<Task>() {

		@Override
		public Task createObject(String id, String title)
		{
			return new Task(id, title);
		}


		@Override
		public void prepare(Task obj, SugarEntity bean)
		{
			String dueDate = bean.get("date_due");
			if (dueDate != null && !dueDate.trim().isEmpty())
				obj.setDue(new DateTime(formatter.parseDateTime(dueDate)).plusHours(gmtOffset));

			obj.setParentType(bean.get("parent_type"));
			obj.setParentId(bean.get("parent_id"));

			obj.setContactId(bean.get("contact_id"));
			
			String extTitle = createExtendedTitle(obj.getParentType(), obj.getParentId());
			
			String cname = bean.get("contact_name");
			if (cname != null && !cname.isEmpty())
				extTitle += ", "+cname;
			
			obj.setExtendedTitle(extTitle);
			obj.setStatus(bean.get("status"));
		}

	};

	public static final CrmObjectCreator<Case> CASE_CREATOR = new CrmObjectCreator<Case>() {

		@Override
		public Case createObject(String id, String title)
		{
			return new Case(id, title);
		}


		@Override
		public void prepare(Case obj, SugarEntity bean)
		{
			obj.setNumber(bean.get("case_number"));
		}
	};

	public static final CrmObjectCreator<Call> CALL_CREATOR = new CrmObjectCreator<Call>() {

		@Override
		public Call createObject(String id, String title)
		{
			return new Call(id, title);
		}


		@Override
		public void prepare(Call obj, SugarEntity bean)
		{
			String startDate = bean.get("date_start");
			if (startDate != null && !startDate.trim().isEmpty())
				obj.setStart(new DateTime(formatter.parseDateTime(startDate)));
			obj.setPlanned("Planned".equals(bean.get("status")));

			obj.setParentType(bean.get("parent_type"));
			obj.setParentId(bean.get("parent_id"));
			obj.setContactId(bean.get("contact_id"));

			obj.setExtendedTitle(createExtendedTitle(obj.getParentType(), obj.getParentId()));
		}
	};

	public static final CrmObjectCreator<Meeting> MEETING_CREATOR = new CrmObjectCreator<Meeting>() {

		@Override
		public Meeting createObject(String id, String title)
		{
			return new Meeting(id, title);
		}


		@Override
		public void prepare(Meeting obj, SugarEntity bean)
		{
			String startDate = bean.get("date_start");
			if (startDate != null && !startDate.trim().isEmpty())
				obj.setStart(new DateTime(formatter.parseDateTime(startDate)));
		}
	};

	public static final CrmObjectCreator<Opportunity> OPPORTUNITY_CREATOR = new CrmObjectCreator<Opportunity>() {

		@Override
		public Opportunity createObject(String id, String title)
		{
			return new Opportunity(id, title);
		}


		@Override
		public void prepare(Opportunity obj, SugarEntity bean)
		{
		}

	};

	public static final CrmObjectCreator<Campaign> CAMPAIGN_CREATOR = new CrmObjectCreator<Campaign>() {

		@Override
		public Campaign createObject(String id, String title)
		{
			return new Campaign(id, title);
		}


		@Override
		public void prepare(Campaign obj, SugarEntity bean)
		{
		}

	};

	public static final CrmObjectCreator<Account> ACCOUNT_CREATOR = new CrmObjectCreator<Account>() {

		@Override
		public Account createObject(String id, String title)
		{
			return new Account(id, title);
		}


		@Override
		public void prepare(Account obj, SugarEntity bean)
		{
		}

	};

	public static final CrmObjectCreator<Contact> CONTACT_CREATOR = new CrmObjectCreator<Contact>() {

		@Override
		public Contact createObject(String id, String title)
		{
			return new Contact(id, title);
		}


		@Override
		public void prepare(Contact obj, SugarEntity bean)
		{
			LEAD_CREATOR.prepare(obj, bean);

			obj.setAccountId(bean.get("account_id"));
		}

	};

	private static final CrmObjectCreator<Lead> LEAD_CREATOR = new CrmObjectCreator<Lead>() {

		@Override
		public Lead createObject(String id, String title)
		{
			return new Lead(id, title);
		}


		@Override
		public void prepare(Lead obj, SugarEntity bean)
		{
			obj.setFirstname(bean.get("first_name"));
			obj.setLastName(bean.get("last_name"));
			obj.setAccountName(bean.get("account_name"));
			obj.setDescription(bean.get("description"));
			obj.setEmail(bean.get("email1"));
			obj.setPhone(bean.get("phone_work"));
			obj.setMobile(bean.get("phone_mobile"));
			obj.setJobTitle(bean.get("title"));
			obj.setCity(bean.get("primary_address_city"));
			obj.setState(bean.get("primary_address_state"));
			obj.setCountry(bean.get("primary_address_country"));
			obj.setAddress(bean.get("primary_address_street"));
			obj.setZip(bean.get("primary_address_postalcode"));
			obj.setType(bean.get("account_type_c"));
			obj.setCampaignId(bean.get("campaign_id"));
		}

	};

	private static final CrmObjectCreator<EmailTemplate> EMAIL_TEMPLATE_CREATOR = new CrmObjectCreator<EmailTemplate>() {

		@Override
		public EmailTemplate createObject(String id, String title)
		{
			return new EmailTemplate(id, title);
		}


		@Override
		public void prepare(EmailTemplate obj, SugarEntity bean)
		{
			String body = StringEscapeUtils.unescapeHtml4(bean.get("body_html"));
			obj.setHtmlBody("<html><body>" + body + "</body></html>");
			obj.setBody(bean.get("body"));
			obj.setSubject(bean.get("subject"));
		}

	};

	private static SugarSession session;

	private static SugarApi api;

	private static String sugarUrl;

	private static int gmtOffset = 0;

	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC();


	public static synchronized boolean setup()
	{
		Settings settings = DataStoreManager.getSettings();
		gmtOffset = settings.getGmtOffset();
		sugarUrl = settings.getCrmUrl();
		api = new SugarApi(sugarUrl);
		try
		{
			session = api.getSugarSession(new SugarCredentials(settings.getUser(), settings.getPassword()));

			String sessionId = session.getSessionID();
			
			CookieManager manager = new CookieManager();
		    manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		    CookieStore store = manager.getCookieStore();
		    store.add(new URI(sugarUrl), new HttpCookie("PHPSESSID", sessionId));
		    CookieHandler.setDefault(manager);
		    
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}


	private static boolean checkSetup()
	{
		if (api == null || session == null)
			return setup();

		return true;
	}


	public static List<Task> getTaskList()
	{
		checkSetup();
		Comparator<Task> comparator = new Comparator<Task>() {

			@Override
			public int compare(Task o1, Task o2)
			{
				int ret = 0;

				if (o1.getDue() != null && o2.getDue() != null)
					ret = o1.getDue().compareTo(o2.getDue());

				if (ret != 0)
					return ret;

				if (o1.getDue() == null && o2.getDue() != null)
					return 1;

				if (o1.getDue() != null && o2.getDue() == null)
					return -1;

				return o1.getId().compareTo(o2.getId());
			}
		};

		final Set<Task> set = new TreeSet<>(comparator);

		String moduleName = "Tasks";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "tasks.status<>'Completed' and tasks.status<>'Deferred' and tasks.assigned_user_id='" + userId + "'";

		Collection<Task> collection = loadCrmObjects(TASK_CREATOR, moduleName, query, "tasks.date_due");
		set.addAll(collection);

		return new ArrayList<>(set);
	}


	public static List<Task> getOpenTaskListByParent(String parentId)
	{
		String moduleName = "Tasks";
		String query = "tasks.status<>'Completed' and (tasks.parent_id='" + parentId + "' or tasks.contact_id='" + parentId + "')";

		Collection<Task> collection = loadCrmObjects(TASK_CREATOR, moduleName, query, "tasks.date_due");

		return new ArrayList<>(collection);
	}


	public static List<Task> getTaskListByParent(String parentId)
	{
		String moduleName = "Tasks";
		String query = "(tasks.parent_id='" + parentId + "' or tasks.contact_id='" + parentId + "')";

		Collection<Task> collection = loadCrmObjects(TASK_CREATOR, moduleName, query, "tasks.date_due DESC");

		return new ArrayList<>(collection);
	}


	public static List<Call> getCallListByParent(String parentId)
	{
		String moduleName = "Calls";
		String query = "calls.status='Planned' and calls.parent_id='" + parentId + "'";

		Collection<Call> collection = loadCrmObjects(CALL_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static List<Meeting> getMeetingListByParent(String parentId)
	{
		String moduleName = "Meetings";
		String query = "meetings.status='Planned' and meetings.parent_id='" + parentId + "'";

		Collection<Meeting> collection = loadCrmObjects(MEETING_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static Call getUpcomingCall()
	{
		checkSetup();
		String moduleName = "Calls";
		String userId = session.getUser().getUserId();

		DateTime from = new DateTime();
		from = from.toDateTime(DateTimeZone.UTC).plusMinutes(10).minusHours(gmtOffset);
		DateTime to = new DateTime();
		to = to.toDateTime(DateTimeZone.UTC).plusMinutes(16).minusHours(gmtOffset);

		String query = "calls.assigned_user_id='" + userId + "' " + "and calls.status='Planned' and calls.date_start between " + "'" + formatter.print(from) + "' and '" + formatter.print(to) + "'";

		final Collection<Call> collection = loadCrmObjects(CALL_CREATOR, moduleName, query);

		final Iterator<Call> iterator = collection.iterator();

		if (iterator.hasNext())
			return iterator.next();

		return null;
	}


	public static Meeting getUpcomingMeeting()
	{
		checkSetup();
		String moduleName = "Meetings";
		String userId = session.getUser().getUserId();

		DateTime from = new DateTime();
		from = from.toDateTime(DateTimeZone.UTC).plusMinutes(10).minusHours(gmtOffset);
		DateTime to = new DateTime();
		to = to.toDateTime(DateTimeZone.UTC).plusMinutes(16).minusHours(gmtOffset);

		String query = "meetings.assigned_user_id='" + userId + "' " + "and meetings.status='Planned' and meetings.date_start between " + "'" + formatter.print(from) + "' and '" + formatter.print(to) + "'";

		final Collection<Meeting> collection = loadCrmObjects(MEETING_CREATOR, moduleName, query);

		final Iterator<Meeting> iterator = collection.iterator();

		if (iterator.hasNext())
			return iterator.next();

		return null;
	}


	public static Contact getContactByNumber(String number)
	{
		return loadCrmObjectByNumber(CONTACT_CREATOR, "Contacts", number);
	}


	public static Lead getLeadByNumber(String number)
	{
		return loadCrmObjectByNumber(LEAD_CREATOR, "Leads", number);
	}


	private static <T extends AbstractCrmObject> T loadCrmObjectByNumber(final CrmObjectCreator<T> creator, String moduleName, String number)
	{
		checkSetup();

		StringBuilder numberPattern = new StringBuilder();
		char[] charArray = number.toCharArray();
		for (char c : charArray)
		{
			numberPattern.append('%').append(c);
		}
		numberPattern.append('%');
		String nq = numberPattern.toString();

		StringBuilder q = new StringBuilder();

		q.append('(').append(moduleName.toLowerCase()).append(".phone_work like '");
		q.append(nq).append("' or ").append(moduleName.toLowerCase()).append(".phone_mobile like '");
		q.append(nq).append("') ");

		final Collection<T> collection = loadCrmObjects(creator, moduleName, q.toString());

		final Iterator<T> iterator = collection.iterator();

		if (iterator.hasNext())
			return iterator.next();

		return null;
	}


	public static Collection<Lead> findLeads(String search)
	{
		return loadCrmContactBySearch(LEAD_CREATOR, "Leads", search);
	}


	public static Collection<Contact> findContacts(String search)
	{
		return loadCrmContactBySearch(CONTACT_CREATOR, "Contacts", search);
	}


	private static <T extends Lead> Collection<T> loadCrmContactBySearch(final CrmObjectCreator<T> creator, String moduleName, String search)
	{
		if (search == null || search.trim().isEmpty())
			return Collections.emptySet();

		checkSetup();

		String[] words = search.split(" ");

		StringBuilder q = new StringBuilder();

		q.append('(');

		if (words.length == 1)
		{
			q.append(moduleName.toLowerCase());
			q.append(".first_name like '%");
			q.append(words[0]).append("%' or ");
			q.append(moduleName.toLowerCase());
			q.append(".last_name like '%");
			q.append(words[0]).append("%' ");
		}
		else
		{
			for (String w : words)
			{
				q.append('(');
				q.append(moduleName.toLowerCase());
				q.append(".first_name like '%");
				q.append(w).append("%' or ");
				q.append(moduleName.toLowerCase());
				q.append(".last_name like '%");
				q.append(w).append("%' ");
				q.append(") and ");
			}
			q.append('1');
		}

		q.append(')');

		final Collection<T> collection = loadCrmObjects(creator, moduleName, q.toString());

		return collection;
	}


	public static Collection<Account> findAccount(String search)
	{
		if (search == null || search.trim().isEmpty())
			return Collections.emptySet();

		checkSetup();

		StringBuilder q = new StringBuilder();

		q.append('(');
		q.append("accounts.name like '%");
		q.append(search).append("%' ");
		q.append(')');

		final Collection<Account> collection = loadCrmObjects(ACCOUNT_CREATOR, "Accounts", q.toString());

		return collection;
	}


	public static Collection<Case> findCase(String search)
	{
		if (search == null || search.trim().isEmpty())
			return Collections.emptySet();

		checkSetup();

		StringBuilder q = new StringBuilder();

		q.append('(');
		q.append("cases.name like '%");
		q.append(search).append("%' ");
		q.append(')');

		final Collection<Case> collection = loadCrmObjects(CASE_CREATOR, "Cases", q.toString());

		return collection;
	}


	public static Collection<Opportunity> findOpportunity(String search)
	{
		if (search == null || search.trim().isEmpty())
			return Collections.emptySet();

		checkSetup();

		StringBuilder q = new StringBuilder();

		q.append('(');
		q.append("opportunities.name like '%");
		q.append(search).append("%' ");
		q.append(')');

		final Collection<Opportunity> collection = loadCrmObjects(OPPORTUNITY_CREATOR, "Opportunities", q.toString());

		return collection;
	}


	public static List<Lead> getLeadList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Leads";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "leads.status<>'Dead' and leads.status<>'dead' and leads.status<>'converted' and leads.status<>'Converted' and" + " leads.converted=0" + " and leads.assigned_user_id='" + userId + "'";

		Collection<Lead> collection = loadCrmObjects(LEAD_CREATOR, moduleName, query, "leads.date_entered DESC");

		return new ArrayList<>(collection);
	}


	public static List<Lead> getGlobalLeadList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Leads";
		String query = "leads.status<>'Dead' and leads.status<>'dead' and leads.status<>'converted' and leads.status<>'Converted' and" + " leads.converted=0";

		Collection<Lead> collection = loadCrmObjects(LEAD_CREATOR, moduleName, query, "leads.date_entered DESC");

		return new ArrayList<>(collection);
	}


	public static List<EmailTemplate> getEmailTemplateList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "EmailTemplates";
		String query = "type<>'campaign'";

		Collection<EmailTemplate> collection = loadCrmObjects(EMAIL_TEMPLATE_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static List<Case> getCaseList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Cases";
		String userId = session.getUser().getUserId();
		String query = "cases.state<>'Closed' and cases.state<>'closed'" + " and cases.assigned_user_id='" + userId + "'";

		Collection<Case> collection = loadCrmObjects(CASE_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static List<Case> getGlobalCaseList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Cases";
		String query = "cases.state<>'Closed' and cases.state<>'closed'";

		Collection<Case> collection = loadCrmObjects(CASE_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static List<Opportunity> getOpportunityList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Opportunities";
		String userId = session.getUser().getUserId();
		String query = "opportunities.sales_stage not like 'Closed%' and opportunities.sales_stage not like 'closed%'" + " and opportunities.assigned_user_id='" + userId + "'";

		Collection<Opportunity> collection = loadCrmObjects(OPPORTUNITY_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static List<Opportunity> getGlobalOpportunityList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Opportunities";
		String query = "opportunities.sales_stage not like 'Closed%' and opportunities.sales_stage not like 'closed%'";

		Collection<Opportunity> collection = loadCrmObjects(OPPORTUNITY_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static Collection<Opportunity> getOpportunityListByAccount(String accountId)
	{
		if (!checkSetup())
			return new ArrayList<>();

		try
		{
			List<SugarEntity> result = api.getRelationsships(session, "Accounts", accountId, "opportunities", "opportunities.sales_stage not like 'Closed%' and opportunities.sales_stage not like 'closed%'");

			return convertEntityList(OPPORTUNITY_CREATOR, "Opportunities", result);
		}
		catch (SugarApiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public static Collection<Contact> getContactListByCase(String caseId)
	{
		if (!checkSetup())
			return new ArrayList<>();

		try
		{
			List<SugarEntity> result = api.getRelationsships(session, "Cases", caseId, "contacts", "");

			return convertEntityList(CONTACT_CREATOR, "Contacts", result);
		}
		catch (SugarApiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public static Collection<Contact> getContactListByOpportunity(String id)
	{
		if (!checkSetup())
			return new ArrayList<>();

		try
		{
			List<SugarEntity> result = api.getRelationsships(session, "Opportunities", id, "contacts", "");

			return convertEntityList(CONTACT_CREATOR, "Contacts", result);
		}
		catch (SugarApiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	private static <T extends AbstractCrmObject> Collection<T> loadCrmObjects(final CrmObjectCreator<T> creator, String moduleName, String query)
	{
		return loadCrmObjects(creator, moduleName, query, null);
	}


	private static <T extends AbstractCrmObject> Collection<T> loadCrmObjects(final CrmObjectCreator<T> creator, String moduleName, String query, String orderBy)
	{

		if (!checkSetup())
			return new ArrayList<>();

		try
		{
			System.out.println(query);
			final List<SugarEntity> beans = new ArrayList<>();
			List<SugarEntity> page;
			do
			{
				page = api.getFindBeans(session, moduleName, query, beans.size(), 20, orderBy);
				beans.addAll(page);
			}
			while (page.size() == 20);

			return convertEntityList(creator, moduleName, beans);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return Collections.emptySet();
	}


	/**
	 * @param creator
	 * @param moduleName
	 * @param beans
	 * @return
	 */
	private static <T extends AbstractCrmObject> Collection<T> convertEntityList(final CrmObjectCreator<T> creator, String moduleName, final List<SugarEntity> beans)
	{
		List<T> result = new ArrayList<>(beans.size());
		for (SugarEntity entity : beans)
		{
			final T t = convertEntity(creator, moduleName, entity);
			result.add(t);
		}

		return result;
	}


	/**
	 * @param creator
	 * @param moduleName
	 * @param entity
	 * @return
	 */
	private static <T extends AbstractCrmObject> T convertEntity(final CrmObjectCreator<T> creator, String moduleName, SugarEntity entity)
	{
		String id = entity.getId();
		final T t = creator.createObject(id, entity.get("name"));
		t.setViewUrl(createObjectUrl(moduleName, id));
		t.setEditUrl(createObjectEditUrl(moduleName, id));
		t.setActivitiesUrl(createObjectActivitiesUrl(moduleName, id));
		t.setAssignedUser(entity.get("assigned_user_name"));
		creator.prepare(t, entity);
		return t;
	}


	/**
	 * @param moduleName
	 * @param id
	 * @return
	 */
	public static String createObjectUrl(String moduleName, String id)
	{
		return sugarUrl + "/index.php?action=DetailView&module=" + moduleName + "&record=" + id;
	}


	public static String createObjectEditUrl(String moduleName, String id)
	{
		return sugarUrl + "/index.php?action=EditView&module=" + moduleName + "&record=" + id;
	}

	public static String createObjectActivitiesUrl(String moduleName, String id)
	{
		return sugarUrl + "/index.php?module=Activities&action=Popup&query=true&record="+id+"&module_name="+moduleName+"&mode=single&create=false";
	}


	public static void rescheduleTask(Task t, DateTime due)
	{
		try
		{
			SugarEntity bean = api.getBean(session, "Tasks", t.getId());

			StringBuilder sb = new StringBuilder(bean.get("description"));
			sb.append("\r\n");
			sb.append("\r\n");
			String newDueStr = due.toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd hh:mm");
			String oldDueStr = formatter.parseDateTime(bean.get("date_due")).plusHours(gmtOffset).toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd hh:mm");
			sb.append("Rescheduled from ").append(oldDueStr).append(" to ").append(newDueStr);
			bean.set("date_due", formatter.print(due.toDateTime(DateTimeZone.UTC).minusHours(gmtOffset)));
			bean.set("description", sb.toString());

			api.setBean(session, bean);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void createCall(Call c)
	{
		try
		{
			SugarEntity bean = new SugarBean("Calls");
			bean.set("date_start", formatter.print(c.getStart().minusHours(gmtOffset)));
			bean.set("date_end", formatter.print(new DateTime().minusHours(gmtOffset)));

			Duration d = new Duration(c.getStart(), new DateTime());
			bean.set("duration_hours", String.valueOf(d.toPeriod().getHours()));
			bean.set("duration_minutes", String.valueOf(d.toPeriod().getMinutes()));

			bean.set("name", c.getTitle());
			bean.set("description", c.getDescription());
			bean.set("status", "Held");

			bean.set("direction", c.getType() == CallType.OUTBOUND ? "Outbound" : "Inbound");

			bean.set("parent_type", c.getRelatedObjectType());
			bean.set("parent_id", c.getRelatedObjectId());

			String userId = session.getUser().getUserId();
			bean.set("assigned_user_id", userId);

			String id = api.setBean(session, bean);
			api.setRelationsship(session, c.getRelatedObjectType(), c.getRelatedObjectId(), "calls", id, false);
			if (c.getContactId() != null)
				api.setRelationsship(session, "Contacts", c.getContactId(), "calls", id, false);
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void completeTask(Task t)
	{
		try
		{
			SugarEntity bean = api.getBean(session, "Tasks", t.getId());

			bean.set("status", "Completed");

			api.setBean(session, bean);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void rescheduleCall(Call call, DateTime start)
	{
		try
		{
			SugarEntity bean = api.getBean(session, "Calls", call.getId());

			StringBuilder sb = new StringBuilder(bean.get("description"));
			sb.append("\r\n");
			sb.append("\r\n");
			String newDueStr = start.toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd hh:mm");
			String oldDueStr = formatter.parseDateTime(bean.get("date_start")).plusHours(gmtOffset).toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd hh:mm");
			sb.append("Rescheduled from ").append(oldDueStr).append(" to ").append(newDueStr);
			bean.set("date_start", formatter.print(start.minusHours(gmtOffset)));
			bean.set("description", sb.toString());

			api.setBean(session, bean);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void heldCall(Call call)
	{
		try
		{
			SugarEntity bean = api.getBean(session, "Calls", call.getId());

			bean.set("status", "Held");

			api.setBean(session, bean);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static List<Call> getCallList()
	{
		checkSetup();

		final Set<Call> set = new TreeSet<>(new Comparator<Call>() {

			@Override
			public int compare(Call o1, Call o2)
			{
				int ret = 0;

				if (o1.isPlanned() && !o2.isPlanned())
					return -1;
				if (!o1.isPlanned() && o2.isPlanned())
					return 1;

				if (o1.getStart() != null && o2.getStart() != null)
					ret = o1.getStart().compareTo(o2.getStart());

				if (!o1.isPlanned())
					ret = ret * -1;

				if (ret != 0)
					return ret;

				if (o1.getStart() == null && o2.getStart() != null)
					return 1;

				if (o1.getStart() != null && o2.getStart() == null)
					return -1;

				return o1.getId().compareTo(o2.getId());
			}
		});

		String moduleName = "Calls";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "((calls.status='Planned' and calls.date_start>'2000-01-01') or calls.date_start>CURDATE()) and calls.assigned_user_id='" + userId + "'";

		Collection<Call> collection = loadCrmObjects(CALL_CREATOR, moduleName, query);
		set.addAll(collection);

		return new ArrayList<>(set);
	}


	public static Case getCase(String id)
	{
		return loadCrmObject(id, "Cases", CASE_CREATOR);
	}


	public static Lead getLead(String id)
	{
		return loadCrmObject(id, "Leads", LEAD_CREATOR);
	}


	public static Opportunity getOpprtunity(String id)
	{
		return loadCrmObject(id, "Opportunities", OPPORTUNITY_CREATOR);
	}


	public static Campaign getCampaignByName(String name)
	{
		Collection<Campaign> result = loadCrmObjects(CAMPAIGN_CREATOR, "Campaigns", "campaigns.name='" + name + "'");
		if (result.isEmpty())
			return null;

		return result.iterator().next();
	}


	/**
	 * @param id
	 * @param moduleName
	 * @param creator
	 */
	private static <T extends AbstractCrmObject> T loadCrmObject(String id, final String moduleName, CrmObjectCreator<T> creator)
	{
		checkSetup();
		try
		{
			SugarEntity bean = api.getBean(session, moduleName, id);

			return convertEntity(creator, moduleName, bean);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}


	public static Contact getContact(String id)
	{
		return loadCrmObject(id, "Contacts", CONTACT_CREATOR);
	}

	public static Account getAccount(String id)
	{
		return loadCrmObject(id, "Accounts", ACCOUNT_CREATOR);
	}


	public static Lead saveLead(Lead l)
	{
		SugarBean b = new SugarBean("Leads");
		b.set("first_name", l.getFirstname());
		b.set("last_name", l.getLastName());
		b.set("account_name", l.getAccountName());
		b.set("description", l.getDescription());
		b.set("email1", l.getEmail());
		b.set("phone_work", l.getPhone());
		b.set("title", l.getJobTitle());
		b.set("primary_address_city", l.getCity());
		b.set("primary_address_state", l.getState());
		b.set("primary_address_country", l.getCountry());
		b.set("primary_address_street", l.getAddress());
		b.set("primary_address_postalcode", l.getZip());
		b.set("account_type_c", l.getType());
		b.set("assigned_user_id", session.getUser().getUserId());
		b.set("campaign_id", l.getCampaignId());

		try
		{
			String result = api.setBean(session, b);

			if (l.getCampaignId() != null && l.getId() == null)
			{
				api.setRelationsship(session, "Campaigns", l.getCampaignId(), "leads", result, false);

				SugarBean log = new SugarBean("CampaignLog");
				log.set("campaign_id", l.getCampaignId());
				log.set("related_id", result);
				log.set("related_type", "Leads");
				log.set("activity_type", "lead");
				log.set("target_type", "Leads");
				log.set("activity_date", formatter.print(new DateTime().minusHours(gmtOffset)));
				log.set("target_id", result);
				log.set("marketing_id", "DesktopCRM Import");
				log.set("more_information", "DesktopCRM Import");

				String logResult = api.setBean(session, log);

				System.out.println("CampaingLog Result: " + logResult);
			}

			return getLead(result);
		}
		catch (SugarApiException e)
		{
			e.printStackTrace();
		}

		return null;
	}

}
