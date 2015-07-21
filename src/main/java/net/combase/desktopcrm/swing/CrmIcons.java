/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;

/**
 * @author "Till Freier"
 *
 */
public final class CrmIcons
{
	public static final ImageIcon USER = loadImage("/user.png");
	public static final ImageIcon CALL = loadImage("/call.png");
	public static final ImageIcon VIEW = loadImage("/view.png");
	public static final ImageIcon DONE = loadImage("/done.png");
	public static final ImageIcon RECHEDULE = loadImage("/reschedule.png");
	public static final ImageIcon WARN = loadImage("/warning.png");
	public static final ImageIcon MAIL = loadImage("/mail.png");

	private CrmIcons()
	{
		super();
	}

	private static ImageIcon loadImage(String path)
	{
		Image img;
		try
		{
			img = new ImageIcon(IOUtils.toByteArray(TaskTableModel.class.getResourceAsStream(path))).getImage();

			return new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return new ImageIcon();
	}

}
