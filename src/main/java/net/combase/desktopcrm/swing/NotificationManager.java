/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.EventQueue;
import java.util.List;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEvent;
import ch.swingfx.twinkle.event.NotificationEventAdapter;
import net.combase.desktopcrm.data.AsteriskManager;
import net.combase.desktopcrm.data.AsteriskManager.AsteriskCallEventListener;
import net.combase.desktopcrm.data.CrmHelper;
import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.data.DataStoreManager;
import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.Call;
import net.combase.desktopcrm.domain.CallType;
import net.combase.desktopcrm.domain.Meeting;



/**
 * @author "Till Freier"
 */
public class NotificationManager
{
	private static boolean init = false;

	private static String lastCallNumber = "";


	public static synchronized void init()
	{
		if (init)
			throw new RuntimeException("notification manager is already initialized");
		
		init = true;

		initActionTimer();
		initCallTimer();
		initMeetingTimer();

		AsteriskManager.setup();
		AsteriskManager.addListener(new AsteriskCallEventListener() {

			@Override
			public void incomingCall(final String number)
			{
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run()
					{
						if (number.equals(lastCallNumber))
						{
							System.out.println("skip call event");
							return;
						}

						new CallWindow(number, CallType.INBOUND);

						lastCallNumber = number;
					}
				});
			}


			@Override
			public void outgoingCall(final String number)
			{
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run()
					{
						if (number.equals(lastCallNumber))
						{
							System.out.println("skip call event");
							return;
						}

						new CallWindow(number, CallType.OUTBOUND);

						lastCallNumber = number;
					}
				});
			}
		});
	}


	/**
	 * 
	 */
	private static void initActionTimer()
	{
		UiUtil.runAndRepeat(new Runnable() {
			@Override
			public void run()
			{
				List<AbstractCrmObject> leadList = CrmHelper.getActionObjects();
				for (final AbstractCrmObject lead : leadList)
				{
					System.out.println("request action item for " + lead.getTitle());
					String msg = "'" + lead.getTitle() + "' has no planned actions. Click here to schedule a task.";

					NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
					nb.withTitle("Plan Follow Up Action");
					nb.withMessage(msg);
					nb.withIcon(CrmIcons.TALK);
					nb.withDisplayTime(60000);

					nb.withListener(new NotificationEventAdapter() {
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
		UiUtil.runAndRepeat(new Runnable() {
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

						nb.withListener(new NotificationEventAdapter() {
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


	/**
	 * 
	 */
	private static void initMeetingTimer()
	{
		UiUtil.runAndRepeat(new Runnable() {
			@Override
			public void run()
			{
				System.out.println("Check for meetings...");

				if (DataStoreManager.getSettings().isMeetingReminder())
				{
					final Meeting c = CrmManager.getUpcomingMeeting();
					if (c != null)
					{
						String msg = "You have an upcoming meeting: " + c.getTitle();

						NotificationBuilder nb = DesktopUtil.createNotificationBuilder();
						nb.withTitle("Upcoming Meeting");
						nb.withMessage(msg);
						nb.withIcon(CrmIcons.COFFEE);
						nb.withDisplayTime(30000);

						nb.withListener(new NotificationEventAdapter() {
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
		}, 7000, 120000);
	}
}
