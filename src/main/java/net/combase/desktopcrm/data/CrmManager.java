/**
 * 
 */
package net.combase.desktopcrm.data;

import java.util.Arrays;
import java.util.List;

import net.combase.desktopcrm.domain.Task;

/**
 * @author till
 *
 */
public class CrmManager {
	
	public static List<Task> getTaskList()
	{
		return Arrays.asList(new Task("453453", "Do this"),new Task("4534qw53", "Do that"));
	}
}
