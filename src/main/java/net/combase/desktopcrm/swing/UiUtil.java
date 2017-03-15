/**
 * 
 */
package net.combase.desktopcrm.swing;

/**
 * @author "Till Freier"
 */
public final class UiUtil
{
	private UiUtil()
	{
	}


	public static void runAndRepeat(final Runnable runner, final long initDelayMillis, final long haltMillis)
	{
		new Thread(new Runnable() {

			@Override
			public void run()
			{
				try
				{
					Thread.sleep(initDelayMillis);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				boolean init = true;
				while (true)
				{
					try
					{
						if (init)
							init = false;
						else
							Thread.sleep(haltMillis);

						runner.run();
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();;
	}
}
