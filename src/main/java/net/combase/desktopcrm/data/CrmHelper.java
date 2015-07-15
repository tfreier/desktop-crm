/**
 * 
 */
package net.combase.desktopcrm.data;

import java.util.ArrayList;
import java.util.List;

import net.combase.desktopcrm.domain.AbstractCrmObject;

/**
 * @author "Till Freier"
 *
 */
public final class CrmHelper
{
	private CrmHelper()
	{
	}

	public static List<AbstractCrmObject> getActionObjects()
	{
		List<AbstractCrmObject> result = new ArrayList<>();
		List<AbstractCrmObject> checkList = new ArrayList<>();

		if (DataStoreManager.getSettings().isCaseReminder())
			checkList.addAll(CrmManager.getCaseList());

		if (DataStoreManager.getSettings().isLeadReminder())
			checkList.addAll(CrmManager.getLeadList());

		if (DataStoreManager.getSettings().isOpportunityReminder())
			checkList.addAll(CrmManager.getOpportunityList());

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
			noAction = CrmManager.getCallListByParent(lead.getId()).isEmpty();

		return !noAction;
	}
}
