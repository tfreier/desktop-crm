/**
 * 
 */
package net.combase.desktopcrm.swing;

import net.combase.desktopcrm.domain.AbstractCrmObject;

/**
 * @author "Till Freier"
 *
 */
public final class DataSelectionEventManager
{
	public interface DataSelectionListener
	{
		public void dataSelected(AbstractCrmObject data);
	}

	public interface DataSelectionActivationListener
	{
		public void initiateDataSelection();
	}

	private static DataSelectionListener listener;
	private static DataSelectionActivationListener activationListener;

	private DataSelectionEventManager()
	{
		super();
	}

	public static void initiateDataSelection(DataSelectionListener l)
	{
		listener = l;
		activationListener.initiateDataSelection();
	}

	public static void setDataSelActivationListener(DataSelectionActivationListener l)
	{
		activationListener = l;
	}

	public static void dataSelected(AbstractCrmObject data)
	{
		if (listener == null)
			return;

		listener.dataSelected(data);
		listener = null;
	}

}
