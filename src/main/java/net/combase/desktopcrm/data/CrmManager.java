/**
 * 
 */
package net.combase.desktopcrm.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.Case;
import net.combase.desktopcrm.domain.Lead;
import net.combase.desktopcrm.domain.Opportunity;
import net.combase.desktopcrm.domain.Settings;
import net.combase.desktopcrm.domain.Task;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sugarcrm.api.SugarApiException;
import com.sugarcrm.api.SugarCredentials;
import com.sugarcrm.api.SugarEntity;
import com.sugarcrm.api.SugarSession;
import com.sugarcrm.api.v4.impl.SugarApi;

/**
 * @author till
 *
 */
public class CrmManager
{
	private static interface CrmObjectCreator<T extends AbstractCrmObject>
	{
		T createObject(String id, String title);

		void prepare(T obj, SugarEntity bean);
	}

	private static final CrmObjectCreator<Task> TASK_CREATOR = new CrmObjectCreator<Task>()
	{

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
		}


	};
	public static final CrmObjectCreator<Case> CASE_CREATOR = new CrmObjectCreator<Case>()
	{

		@Override
		public Case createObject(String id, String title)
		{
			return new Case(id, title);
		}

		@Override
		public void prepare(Case obj, SugarEntity bean)
		{
		}
	};
	public static final CrmObjectCreator<Call> CALL_CREATOR = new CrmObjectCreator<Call>()
		{

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
		}
	};

	public static final CrmObjectCreator<Opportunity> OPPORTUNITY_CREATOR = new CrmObjectCreator<Opportunity>()
		{

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


	private static SugarSession session;
	private static SugarApi api;
	private static String sugarUrl;
	private static int gmtOffset = 0;
	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(
		"yyyy-MM-dd HH:mm:ss").withZoneUTC();


	public static synchronized boolean setup()
	{
		Settings settings = DataStoreManager.getSettings();
		gmtOffset = settings.getGmtOffset();
		sugarUrl = settings.getCrmUrl();
		api = new SugarApi(sugarUrl);
		try
		{
			session = api.getSugarSession(new SugarCredentials(settings.getUser(),
				settings.getPassword()));

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
		Comparator<Task> comparator = new Comparator<Task>()
		{

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
		String query = "tasks.status<>'Completed' and tasks.assigned_user_id='" + userId + "'";


		Collection<Task> collection = loadCrmObjects(TASK_CREATOR, moduleName, query);
		set.addAll(collection);

		return new ArrayList<>(set);
	}

	public static List<Task> getTaskListByParent(String parentId)
	{
		String moduleName = "Tasks";
		String query = "tasks.status<>'Completed' and tasks.parent_id='" + parentId + "'";

		Collection<Task> collection = loadCrmObjects(TASK_CREATOR, moduleName, query,
			"tasks.date_due");

		return new ArrayList<>(collection);
	}

	public static List<Call> getCallListByParent(String parentId)
	{
		String moduleName = "Calls";
		String query = "calls.status<>'Planned' and calls.parent_id='" + parentId + "'";


		Collection<Call> collection = loadCrmObjects(CALL_CREATOR, moduleName, query);

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

		String query = "calls.assigned_user_id='" + userId + "' " +
			"and calls.status='Planned' and calls.date_start between " + "'" +
			formatter.print(from) + "' and '" + formatter.print(to) + "'";


		final Collection<Call> collection = loadCrmObjects(CALL_CREATOR, moduleName, query);

		final Iterator<Call> iterator = collection.iterator();

		if (iterator.hasNext())
			return iterator.next();

		return null;
	}

	public static List<Lead> getLeadList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		final CrmObjectCreator<Lead> creator = new CrmObjectCreator<Lead>()
		{

			@Override
			public Lead createObject(String id, String title)
			{
				return new Lead(id, title);
			}

			@Override
			public void prepare(Lead obj, SugarEntity bean)
			{
			}


		};

		String moduleName = "Leads";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "leads.status<>'Dead' and leads.status<>'dead' and leads.status<>'converted' and leads.status<>'Converted' and" +
			" leads.converted=0" + " and leads.assigned_user_id='" + userId + "'";


		Collection<Lead> collection = loadCrmObjects(creator, moduleName, query);

		return new ArrayList<>(collection);
	}

	public static List<Case> getCaseList()
	{
		if (!checkSetup())
			return new ArrayList<>();


		String moduleName = "Cases";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "cases.state<>'Closed' and cases.state<>'closed'" +
			" and cases.assigned_user_id='" + userId + "'";


		Collection<Case> collection = loadCrmObjects(CASE_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}


	public static List<Opportunity> getOpportunityList()
	{
		if (!checkSetup())
			return new ArrayList<>();

		String moduleName = "Opportunities";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "opportunities.sales_stage not like 'Closed%' and opportunities.sales_stage not like 'closed%'" +
			" and opportunities.assigned_user_id='" + userId + "'";


		Collection<Opportunity> collection = loadCrmObjects(OPPORTUNITY_CREATOR, moduleName, query);

		return new ArrayList<>(collection);
	}

	public static Collection<Opportunity> getOpportunityListByAccount(String accountId)
	{
		if (!checkSetup())
			return new ArrayList<>();

		try
		{
			List<SugarEntity> result = api.getRelationsships(session, "Accounts", accountId,
				"opportunities",
				"opportunities.sales_stage not like 'Closed%' and opportunities.sales_stage not like 'closed%'");

			return convertEntityList(OPPORTUNITY_CREATOR, "Opportunities", result);
		}
		catch (SugarApiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return new ArrayList<>();

	}

	private static <T extends AbstractCrmObject> Collection<T> loadCrmObjects(
		final CrmObjectCreator<T> creator, String moduleName, String query)
	{
		return loadCrmObjects(creator, moduleName, query, null);
	}

	private static <T extends AbstractCrmObject> Collection<T> loadCrmObjects(
		final CrmObjectCreator<T> creator, String moduleName, String query, String orderBy)
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
				page = api.getFindBeans(session, moduleName, query, beans.size(), 20,
				orderBy);
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
	private static <T extends AbstractCrmObject> Collection<T> convertEntityList(
		final CrmObjectCreator<T> creator, String moduleName, final List<SugarEntity> beans)
	{
		List<T> result = new ArrayList<>(beans.size());
		for (SugarEntity entity : beans)
		{
			final T t = creator.createObject(entity.getId(), entity.get("name"));
			t.setViewUrl(sugarUrl + "/index.php?action=DetailView&module=" + moduleName +
				"&record=" + entity.getId());
			creator.prepare(t, entity);
			result.add(t);
		}

		return result;
	}


	public static void rescheduleTask(Task t, DateTime due)
	{
		try
		{
			SugarEntity bean = api.getBean(session, "Tasks", t.getId());

			StringBuilder sb = new StringBuilder(bean.get("description"));
			sb.append("\r\n");
			sb.append("\r\n");
			String newDueStr = due.toDateTime(DateTimeZone.getDefault()).toString(
				"yyyy-MM-dd hh:mm");
			String oldDueStr = formatter.parseDateTime(bean.get("date_due"))
				.plusHours(gmtOffset)
				.toDateTime(DateTimeZone.getDefault())
				.toString("yyyy-MM-dd hh:mm");
			sb.append("Rescheduled from ").append(oldDueStr).append(" to ").append(newDueStr);
			bean.set("date_due", formatter.print(due.minusHours(gmtOffset)));
			bean.set("description", sb.toString());


			api.setBean(session, bean);

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
			String newDueStr = start.toDateTime(DateTimeZone.getDefault()).toString(
				"yyyy-MM-dd hh:mm");
			String oldDueStr = formatter.parseDateTime(bean.get("date_start"))
				.plusHours(gmtOffset)
				.toDateTime(DateTimeZone.getDefault())
				.toString("yyyy-MM-dd hh:mm");
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

		final Set<Call> set = new TreeSet<>(new Comparator<Call>()
		{

			@Override
			public int compare(Call o1, Call o2)
			{
				if (o1.getId().equals(o2.getId()))
					return 0;
				if (o1.getStart() != null && o2.getStart() != null)
					return o1.getStart().compareTo(o2.getStart());

				if (o2.getStart() == null)
					return -1;

				return 1;
			}
		});

		String moduleName = "Calls";
		String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
		String query = "calls.status='Planned' and calls.date_start>'2000-01-01' and calls.assigned_user_id='" +
			userId + "'";


		Collection<Call> collection = loadCrmObjects(CALL_CREATOR, moduleName, query);
		set.addAll(collection);

		return new ArrayList<>(set);
	}


}
