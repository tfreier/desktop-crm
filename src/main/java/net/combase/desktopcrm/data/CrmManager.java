/**
 * 
 */
package net.combase.desktopcrm.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.combase.desktopcrm.domain.Settings;
import net.combase.desktopcrm.domain.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
	private static SugarSession session;
	private static SugarApi api;
	private static String sugarUrl;
	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public static boolean setup()
	{
		Settings settings = DataStoreManager.getSettings();
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
		if (!checkSetup())
			return new ArrayList<>();
		try
		{
			String userId = session.getUser().getUserId(); // "a2e0e9a3-4d63-a56b-315b-546a4cdf41a8";//
			String query = "tasks.status<>'Completed' and tasks.assigned_user_id='" + userId + "'";
			System.out.println(query);
			final List<SugarEntity> beans = api.getFindBeans(session, "Tasks",
				query,
				0, 100);

			final Set<Task> list = new TreeSet<>(new Comparator<Task>()
			{

				@Override
				public int compare(Task o1, Task o2)
				{
					if (o2.getDue() == o1.getDue())
						return 0;
					if (o2.getDue() == null)
						return -1;
					if (o1.getDue() == null)
						return 1;

					return o1.getDue().compareTo(o2.getDue());
				}
			});
			for (SugarEntity entity : beans)
			{
				final Task t = new Task(entity.getId(), entity.get("name"));
				String dueDate = entity.get("date_due");
				if (dueDate != null && !dueDate.trim().isEmpty())
				t.setDue(new DateTime(formatter.parseDateTime(dueDate)));
				t.setViewUrl(sugarUrl + "/index.php?action=DetailView&module=Tasks&record=" +
					entity.getId());
				list.add(t);
			}
			return new ArrayList<>(list);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public static void rescheduleTask(Task t, DateTime due)
	{
		try
		{
			SugarEntity bean = api.getBean(session, "Tasks", t.getId());

			StringBuilder sb = new StringBuilder(bean.get("description"));
			sb.append("\r\n");
			sb.append("\r\n");
			String date_due = formatter.print(due);
			sb.append("Rescheduled from ")
				.append(bean.get("date_due"))
				.append(" to ")
				.append(date_due);
			bean.set("date_due", date_due);
			bean.set("description", sb.toString());


			api.setBean(session, bean);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void completeTask(Task t, DateTime due)
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


}
