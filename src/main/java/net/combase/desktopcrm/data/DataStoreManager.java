/**
 * 
 */
package net.combase.desktopcrm.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.combase.desktopcrm.domain.DataStore;
import net.combase.desktopcrm.domain.Settings;

/**
 * @author till
 *
 */
public class DataStoreManager {

	private static Settings settings;

	private static DataStore load() throws JAXBException, FileNotFoundException
	{
		File f = getStoreFile();
		
		if (!f.exists())
			return new DataStore();
		
		JAXBContext context = JAXBContext.newInstance(DataStore.class);
		Unmarshaller m = context.createUnmarshaller();
		return (DataStore)m.unmarshal(new FileReader(f));
	}
	private static void write(DataStore store) throws JAXBException, IOException
	{
		File f = getStoreFile();
		
		JAXBContext context = JAXBContext.newInstance(DataStore.class);
		Marshaller m = context.createMarshaller();
		m.marshal(store, new FileWriter(f));
	}

	private static File getStoreFile() {
		String home = System.getProperty("user.home");
		File f = new File(home, ".desktopCrmStore.xml");
		return f;
	}
	
	public static Settings getSettings()
	{
		if (settings != null)
			return settings;
		try {
			return settings = load().getSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Settings();
	}
	
	public static void writeSettings(Settings s)
	{
		try {
			DataStore load = load();
			load.setSettings(s);
			write(load);
			settings = s;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
