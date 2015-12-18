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

			// use capterra as default campaign since it doesn't reference itself in the import file
			Campaign camp = CrmManager.getCampaignByName("Capterra");
			if (camp != null)
				lead.setCampaignId(camp.getId());

			while (header.hasNext() && content.hasNext())
			{
				String key = header.next();
				String value = content.next();
				System.out.println(key + "=" + value);

				switch (key)
				{
					case "Campaign Code" :
						camp = CrmManager.getCampaignByName(value);
						if (camp != null)
							lead.setCampaignId(camp.getId());
						break;
					case "Organization" :
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
					case "# of Employees" :
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
					case "Problem Buyer Needs to Solve" :
						desc.append("\n\nCALL NOTES\n\n").append(value).append("\n\n");
						break;
					case "Budget Details" :
						desc.append("\n\nBudget Details\n\n").append(value).append("\n\n");
					case "Buyer's Requirements" :
						desc.append("\n\nBudget Details\n\n").append(value).append("\n\n");
						break;
					case "Timeframe" :
					case "Purchase Timeframe" :
						desc.append("Timeframe: ").append(value).append("\n");
						break;
					case "Stage in Buying Process" :
						desc.append("Stage in Buying Process: ").append(value).append("\n");
						break;
					case "Product" :
					case "Type of Software Needed" :
						desc.append("Product: ").append(value).append("\n");
						break;
					case "Current Software" :
						desc.append("Current Software: ").append(value).append("\n");
						break;
					case "Buyer Has Budget" :
						desc.append("Buyer Has Budget: ").append(value).append("\n");
						break;
					case "Budget Amount" :
						desc.append("Budget Amount: ").append(value).append("\n");
						break;
					case "Decision Maker" :
						desc.append("Decision Maker: ").append(value).append("\n");
						break;
					case "Name" :
						String[] split = value.split(" ");
						if (split.length < 2)
							lead.setLastName(value);
						else
						{
							lead.setFirstname(split[0]);
							int i = 0;
							StringBuilder sb = new StringBuilder();
							for (String string : split)
							{
								i++;
								if (i == 1)
									continue;
								sb.append(string);
								if (i < split.length)
									sb.append(' ');
							}
							lead.setLastName(sb.toString());
						}
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
					case "Location" :
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
