package at.f1l2.prunus.avium.core.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.f1l2.prunus.avium.core.exception.AviumCoreException;

public class HttpResource {

	private static Logger logger = LoggerFactory.getLogger(HttpResource.class);

	private HttpResource() {
	}

	public static String requestResource(String url) {
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			String inputLine;

			StringBuilder input = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				input.append(inputLine);
			}
			in.close();

			return input.toString();

		} catch (IOException e) {
			logger.error("", e);
			throw new AviumCoreException("Exception occured accessing http resource.", e);
		}
	}

	public static void requestStream(String url, OutputStream osData) {

		InputStream is = null;
		try {

			URL resourceURL = new URL(url);
			int contentLength = resourceURL.openConnection().getContentLength();

			logger.info("Content-Length: {}", contentLength);

			is = resourceURL.openStream();

			byte[] byteChunk = new byte[4096];
			int n;

			long contentRead = 0;
			long percOld = -1;
			long percNew = -1;
			while ((n = is.read(byteChunk)) > 0) {
				contentRead = contentRead + n;
				osData.write(byteChunk, 0, n);

				percNew = contentRead * 100 / contentLength;

				if (percOld == -1 || percNew > percOld + 4) {
					percOld = percNew;
					logger.info("Process {} MB ({} %)", contentRead / 1000 * 1000, percNew);
				}
			}
		} catch (IOException e) {
			logger.error("Failed while reading bytes from %s: %s", url, e.getMessage(), e);
			throw new AviumCoreException("Exception occured accessing http resource.");

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
