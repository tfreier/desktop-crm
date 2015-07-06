/**
 * 
 */
package net.combase.desktopcrm.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author till
 *
 */
@XmlRootElement
public class DataStore {
	private Settings settings = new Settings();

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	
}
