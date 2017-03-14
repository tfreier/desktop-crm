/**
 * 
 */
package net.combase.desktopcrm.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import net.combase.desktopcrm.domain.AbstractCrmObject;
import net.combase.desktopcrm.domain.EmailTemplate;

/**
 * @author "Till Freier"
 *
 */
public final class CrmHelper
{
	private CrmHelper()
	{
	}

	private static Collection<EmailTemplate> emailTemplateCache = null;


	public static synchronized void updateEmailTemplateCache()
	{
		emailTemplateCache = new TreeSet<>(new Comparator<EmailTemplate>()
		{

			@Override
			public int compare(EmailTemplate o1, EmailTemplate o2)
			{
				return o1.getTitle().compareTo(o2.getTitle());
			}
		});

		emailTemplateCache.addAll(CrmManager.getEmailTemplateList());
	}


	public static synchronized Collection<EmailTemplate> getCachedEmailTemplates()
	{
		if (emailTemplateCache == null)
		{
			updateEmailTemplateCache();
		}

		return emailTemplateCache;
	}

	public static List<AbstractCrmObject> getActionObjects()
	{
		List<AbstractCrmObject> result = new ArrayList<>();
		List<AbstractCrmObject> checkList = new ArrayList<>();

		if (DataStoreManager.getSettings().isCaseReminder())
			checkList.addAll(CrmManager.getCaseList());

		if (DataStoreManager.getSettings().isOpportunityReminder())
			checkList.addAll(CrmManager.getOpportunityList());

		if (DataStoreManager.getSettings().isLeadReminder())
			checkList.addAll(CrmManager.getLeadList());


		for (final AbstractCrmObject lead : checkList)
		{
			System.out.println("check " + lead.getTitle() + " for actions.");
			if (!hasActionsPlanned(lead))
				result.add(lead);
		}
		System.out.println("found " + result.size() + " action items.");
		return result;
	}

	/**
	 * @param lead
	 * @return
	 */
	public static boolean hasActionsPlanned(final AbstractCrmObject lead)
	{
		boolean noAction = CrmManager.getTaskListByParent(lead.getId()).isEmpty();

		if (noAction)
			noAction = CrmManager.getMeetingListByParent(lead.getId()).isEmpty();
		
		if (noAction)
			noAction = CrmManager.getCallListByParent(lead.getId()).isEmpty();

		return !noAction;
	}


	public static List<AbstractCrmObject> getGlobalActionObjects()
	{
		List<AbstractCrmObject> result = new ArrayList<>();
		List<AbstractCrmObject> checkList = new ArrayList<>();

		checkList.addAll(CrmManager.getGlobalCaseList());
		checkList.addAll(CrmManager.getGlobalOpportunityList());
		checkList.addAll(CrmManager.getGlobalLeadList());

		for (final AbstractCrmObject lead : checkList)
		{
			System.out.println("check " + lead.getTitle() + " for actions.");
			if (!hasActionsPlanned(lead))
				result.add(lead);
		}
		System.out.println("found " + result.size() + " action items.");
		return result;
	}
}
