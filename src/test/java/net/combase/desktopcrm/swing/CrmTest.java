/**
 * 
 */
package net.combase.desktopcrm.swing;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author "Till Freier"
 *
 */
public class CrmTest
{
	public static void main(String[] args)
	{

		try
		{
			HttpGet get = new HttpGet("http://10.1.0.11/cgi-bin/ConfigManApp.com?number=702-927-6689");
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
			provider.setCredentials(AuthScope.ANY, credentials);
			HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
