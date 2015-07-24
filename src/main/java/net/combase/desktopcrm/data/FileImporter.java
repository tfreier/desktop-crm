/**
 * 
 */
package net.combase.desktopcrm.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import net.combase.desktopcrm.domain.Campaign;
import net.combase.desktopcrm.domain.Lead;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * @author "Till Freier"
 *
 */
public class FileImporter
{
	public static Lead importFile(File f)
	{
		try
		{

			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(new FileReader(f));
			Iterator<CSVRecord> lines = records.iterator();
			if (!lines.hasNext())
				return null;
			Iterator<String> header = lines.next().iterator();
			if (!lines.hasNext())
				return null;
			Iterator<String> content = lines.next().iterator();

			Lead lead = new Lead(null, "new lead");
			StringBuilder desc = new StringBuilder();
			while (header.hasNext() && content.hasNext())
			{
				String key = header.next();
				String value = content.next();
				System.out.println(key + "=" + value);

				switch (key)
				{
					case "Campaign Code" :
						Campaign camp = CrmManager.getCampaignByName(value);
						if (camp != null)
							lead.setCampaignId(camp.getId());
						break;
					case "Company" :
						lead.setAccountName(value);
						break;
					case "Seqment" :
						desc.append(value).append("\n\n");
						break;
					case "Size" :
						desc.append("Size: ").append(value).append("\n");
						break;
					case "Size 2" :
						desc.append("Staff: ").append(value).append("\n");
						break;
					case "Size 3" :
						desc.append("Terminals: ").append(value).append("\n");
						break;
					case "Applications" :
						desc.append("Applications: ").append(value).append("\n");
						break;
					case "Deployment" :
						desc.append("Deployment: ").append(value).append("\n");
						break;
					case "Call Notes" :
						desc.append("\n\nCALL NOTES\n\n").append(value).append("\n\n");
						break;
					case "Timeframe" :
						desc.append("Timeframe: ").append(value).append("\n");
						break;
					case "Product" :
						desc.append("Product: ").append(value).append("\n");
						break;
					case "First Name" :
						lead.setFirstname(value);
						break;
					case "Last Name" :
						lead.setLastName(value);
						break;
					case "Job Title" :
						lead.setJobTitle(value);
						break;
					case "Phone" :
						lead.setPhone(value);
						break;
					case "Email" :
						lead.setEmail(value);
						break;
					case "Address" :
						lead.setAddress(value);
						break;
					case "City" :
						lead.setCity(value);
						break;
					case "State" :
						lead.setState(value);
						break;
					case "ZIP Code" :
						lead.setZip(value);
						break;
					case "Country" :
						lead.setCountry(value);
						break;
					case "Request" :
						desc.append("Request: ").append(value).append("\n");
					case "Timestamp" :
						desc.append("Timestamp: ").append(value).append("\n");
					case "Appointment" :
						desc.append("\nAppointment: ").append(value).append("\n\n");
						break;
					default :
						break;
				}
			}

			lead.setDescription(desc.toString());
			lead.setType("Customer");
			return CrmManager.saveLead(lead);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
