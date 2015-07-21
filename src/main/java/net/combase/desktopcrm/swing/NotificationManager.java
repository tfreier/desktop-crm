/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.combase.desktopcrm.data.CrmHelper;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.data.DataStoreManager;
import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Call;
import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEvent;
import ch.swingfx.twinkle.event.NotificationEventAdapter;

/**
 * @author "Till Freier"
 *
 */
public class NotificationManager
{
	private static Timer timer = null;

	public static synchronized void init()
	{
		if (timer != null)
			throw new RuntimeException("notification manager is already initialized");

		timer = new Timer(true);

		initActionTimer();
		initCallTimer();
	}

	/**
	 * 
	 */
	private static void initActionTimer()
	{
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				List<AbstractCrmObject> leadList = CrmHelper.getActionObjects();
				for (final AbstractCrmObject lead : leadList)
				{
					System.out.println("request action item for " + lead.getTitle());
					String msg = "'" + lead.getTitle() +
						"' has no planned actions. Click here to schedule a task.";

					NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
					nb.withTitle("Plan Follow Up Action");
					nb.withMessage(msg);
					nb.withIcon(CrmIcons.TALK);
					nb.withDisplayTime(60000);

					nb.withListener(new NotificationEventAdapter()
					{
						@Override
						public void clicked(NotificationEvent event)
						{
							DesktopUtil.openBrowser(lead.getViewUrl());
						}
					});

					nb.showNotification();

					// wait 2 minutes
					try
					{
						Thread.sleep(180000);
					}
					catch (InterruptedException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}, 3000, 300000);
	}

	/**
	 * 
	 */
	private static void initCallTimer()
	{
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				System.out.println("Check for calls...");

				if (DataStoreManager.getSettings().isCallReminder())
				{
					final Call c = CrmManager.getUpcomingCall();
					if (c != null)
					{
						String msg = "You have an upcoming call: " + c.getTitle();

						NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
						nb.withTitle("Upcoming Call");
						nb.withMessage(msg);
						nb.withIcon(CrmIcons.CALL);
						nb.withDisplayTime(30000);

						nb.withListener(new NotificationEventAdapter()
						{
							@Override
							public void clicked(NotificationEvent event)
							{
								DesktopUtil.openBrowser(c.getViewUrl());
							}
						});

						nb.showNotification();
					}
				}
			}
		}, 5000, 120000);
	}
}
