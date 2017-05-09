package at.f1l2.prunus.avium.core.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import at.f1l2.prunus.avium.core.exception.AviumCoreException;

public class HttpResource {
	
	
	private HttpResource() {
	}
	
	public static String requestResource(URL url) {
		try {
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
			String inputLine;

			StringBuilder input = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				input.append(inputLine);
			}
			in.close();

			return input.toString();

		} catch (IOException e) {
			throw new AviumCoreException("Http", e);
		}
	}

}
