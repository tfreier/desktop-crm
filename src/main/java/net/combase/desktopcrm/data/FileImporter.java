/**
 * 
 */
package net.combase.desktopcrm.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import net.combase.desktopcrm.domain.Campaign;
import net.combase.desktopcrm.domain.Lead;



/**
 * @author "Till Freier"
 */
public class FileImporter
{
	public static Collection<Lead> importFile(File f)
	{
		try
		{

			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(new FileReader(f));
			Iterator<CSVRecord> lines = records.iterator();
			if (!lines.hasNext())
				return null;
			CSVRecord headerRow = lines.next();
			List<Lead> leads = new ArrayList<>();
			while (lines.hasNext())
			{
				final Iterator<String> content = lines.next().iterator();
				final Iterator<String> header = headerRow.iterator();

				Lead lead = new Lead(null, "new lead");
				StringBuilder desc = new StringBuilder();

				// use capterra as default campaign since it doesn't reference
				// itself in the import file
				Campaign camp = CrmManager.getCampaignByName("Capterra");
				if (camp != null)
					lead.setCampaignId(camp.getId());

				int column = 0;

				while (header.hasNext() && content.hasNext())
				{
					column++;
					String key = header.next();
					String value = content.next();
					System.out.println(key + "=" + value);

					if (column == 1 && key.startsWith("RFQ"))
					{
						camp = CrmManager.getCampaignByName("BuyerZone");
						if (camp != null)
							lead.setCampaignId(camp.getId());
					}

					switch (key)
					{
						case "Campaign Code":
							camp = CrmManager.getCampaignByName(value);
							if (camp != null)
								lead.setCampaignId(camp.getId());
							break;
						case "Organization":
						case "Company":
							lead.setAccountName(value);
							break;
						case "Seqment":
							desc.append(value).append("\n\n");
							break;
						case "Size":
							desc.append("Size: ").append(value).append("\n");
							break;
						case "Size 2":
						case "# of Employees":
							desc.append("Staff: ").append(value).append("\n");
							break;
						case "Size 3":
							desc.append("Terminals: ").append(value).append("\n");
							break;
						case "Applications":
							desc.append("Applications: ").append(value).append("\n");
							break;
						case "Deployment":
							desc.append("Deployment: ").append(value).append("\n");
							break;
						case "Call Notes":
						case "Problem Buyer Needs to Solve":
							desc.append("\n\nCALL NOTES\n\n").append(value).append("\n\n");
							break;
						case "Budget Details":
							desc.append("\n\nBudget Details\n\n").append(value).append("\n\n");
						case "Buyer's Requirements":
							desc.append("\n\nBudget Details\n\n").append(value).append("\n\n");
							break;
						case "Timeframe":
						case "Purchase Timeframe":
							desc.append("Timeframe: ").append(value).append("\n");
							break;
						case "Stage in Buying Process":
							desc.append("Stage in Buying Process: ").append(value).append("\n");
							break;
						case "Product":
						case "Type of Software Needed":
							desc.append("Product: ").append(value).append("\n");
							break;
						case "Current Software":
							desc.append("Current Software: ").append(value).append("\n");
							break;
						case "Buyer Has Budget":
							desc.append("Buyer Has Budget: ").append(value).append("\n");
							break;
						case "Budget Amount":
							desc.append("Budget Amount: ").append(value).append("\n");
							break;
						case "Decision Maker":
							desc.append("Decision Maker: ").append(value).append("\n");
							break;
						case "Name":
						case "Contact Name":
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
						case "First Name":
							lead.setFirstname(value);
							break;
						case "Last Name":
							lead.setLastName(value);
							break;
						case "Job Title":
						case "Title":
							lead.setJobTitle(value);
							break;
						case "Phone":
							lead.setPhone(value);
							break;
						case "Email":
							lead.setEmail(value);
							break;
						case "Address":
							lead.setAddress(value);
							break;
						case "City":
							lead.setCity(value);
							break;
						case "State":
							lead.setState(value);
							break;
						case "ZIP Code":
						case "ZIP":
						case "Zip":
							lead.setZip(value);
							break;
						case "Country":
						case "Location":
							lead.setCountry(value);
							break;
						case "Request":
							desc.append("Request: ").append(value).append("\n");
							break;
						case "Timestamp":
							desc.append("Timestamp: ").append(value).append("\n");
							break;
						case "Appointment":
							desc.append("\nAppointment: ").append(value).append("\n\n");
							break;
						default:
							desc.append("").append(key).append(": ").append(value).append("\n\n");
							break;
					}
				}

				lead.setDescription(desc.toString());
				lead.setType("Customer");
				leads.add(CrmManager.saveLead(lead));
			}

			return leads;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
