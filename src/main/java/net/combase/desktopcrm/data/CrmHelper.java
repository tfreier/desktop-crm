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
		checkList.addAll(CrmManager.getLeadList());
		checkList.addAll(CrmManager.getOpportunityList());
		for (final AbstractCrmObject lead : checkList)
		{
			if (!hasActionsPlanned(lead))
				result.add(lead);
		}

		return result;
	}

	/**
	 * @param lead
	 * @return
	 */
	private static boolean hasActionsPlanned(final AbstractCrmObject lead)
	{
		boolean noAction = CrmManager.getTaskListByParent(lead.getId()).isEmpty();

		if (noAction)
			noAction = CrmManager.getCallListByParent(lead.getId()).isEmpty();

		return !noAction;
	}
}
