/**
 * 
 */
package net.combase.desktopcrm.swing;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Lead;

/**
 * @author "Till Freier"
 *
 */
public class CrmTest
{
	public static void main(String[] args)
	{
		Lead lead = CrmManager.getLead("bc856627-71aa-0ce1-0da8-53dd1acbe78c");
	}
}
