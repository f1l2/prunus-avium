package at.f1l2.prunus.avium.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.utility.HttpResource;
import at.f1l2.prunus.avium.core.utility.ParseJsonTypeUndefinied;

public class PrunusAvium {

	private static Logger logger = LoggerFactory.getLogger(PrunusAvium.class);

	public static void main(String[] args) {

		logger.info("Program has started");

		PrunusAvium oe1Downloader = new PrunusAvium();

		String webpage = oe1Downloader.requestPlayerSite();
		List<Program> programs = oe1Downloader.parsePrograms(webpage);

		System.out.println(programs.size());

		List<Program> dimensionen = programs.stream().sorted(Comparator.comparing(Program::getBegin))
				.filter(item -> item.getTitle().startsWith("Dimensionen")).collect(Collectors.toList());
		//

		System.out.println(dimensionen.stream().map(item -> item.getTitle()).collect(Collectors.joining(", ")));

		logger.info("Program has finished");

		 oe1Downloader.downloadPrograms(dimensionen);
	}

	private String requestAPI(String stringUrl) {
		try {
			URL url = new URL(stringUrl);
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
			System.out.println(e);
		}

		return null;
	}

	private String requestPlayerSite() {

		return requestAPI("https://audioapi.orf.at/oe1/api/json/current/broadcasts/");
	}

	private List<Program> parsePrograms(String html) {

		List<Program> programs = new ArrayList<>();

		try {
			List<Map<String, Object>> doo = ParseJsonTypeUndefinied.doo(new ArrayList<>(), "[].broadcasts", html);

			for (Map<String, Object> linkedHashMap : doo) {
				Program program = new Program();

				program.setHref(getValueFromMap("href", linkedHashMap));
				program.setTitle(getValueFromMap("title", linkedHashMap));
				program.setSubtitle(getValueFromMap("subtitle", linkedHashMap));

				String beginTimestamp = getValueFromMap("start", linkedHashMap);
				LocalDateTime begin = Instant.ofEpochMilli(Long.valueOf(beginTimestamp)).atZone(ZoneId.systemDefault())
						.toLocalDateTime();

				program.setBegin(begin);

				String endTimestamp = getValueFromMap("end", linkedHashMap);
				LocalDateTime end = Instant.ofEpochMilli(Long.valueOf(endTimestamp)).atZone(ZoneId.systemDefault())
						.toLocalDateTime();

				program.setBegin(begin);
				program.setEnd(end);

				programs.add(program);

			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return programs;
	}

	private String getValueFromMap(String key, Map<String, Object> map) {
		if (map.containsKey(key)) {
			Object value = map.get(key);
			if (Objects.nonNull(value)) {
				return value.toString();
			}
		}
		return null;
	}

	private ArrayList<Object> getArrayFromMap(String key, Map<Object, Object> map) {
		if (map.containsKey(key)) {
			Object value = map.get(key);

			if (value instanceof ArrayList) {
				return ((ArrayList<Object>) value);
			}
		}
		return new ArrayList<>();
	}

	private Map<Object, Object> getMapFromMap(String key, Map<Object, Object> map) {
		if (map.containsKey(key)) {
			Object value = map.get(key);

			if (value instanceof Map) {
				return ((Map<Object, Object>) value);
			}
		}
		return new HashMap<>();
	}

	private Byte[] downloadPrograms(List<Program> programs) {

		for (Program program : programs) {
			System.out.println("Start download " + program.displayTitle());
			downloadProgram(program);
			System.out.println("End download");
		}

		return null;

	}

	private Byte[] downloadProgram(Program program) {

		String response = requestAPI(program.getHref());

		try {

			List<Map<String, Object>> doo = ParseJsonTypeUndefinied.doo(new ArrayList<>(), "streams", response);

			if (doo != null && doo.size() > 0) {
				Object object = doo.get(0).get("loopStreamId");

				System.out.println(object.toString());

				String url = "http://loopstream01.apa.at/?channel=oe1&shoutcast=0&id=" + object.toString();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InputStream is = null;
				try {
					is = new URL(url).openStream();
					byte[] byteChunk = new byte[4096]; // Or whatever
														// size you want
														// to read in at
														// a time.
					int n;

					while ((n = is.read(byteChunk)) > 0) {
						baos.write(byteChunk, 0, n);
					}
				} catch (IOException e) {
					System.err.printf("Failed while reading bytes from %s: %s", url, e.getMessage());
					e.printStackTrace();
					// Perform any other exception handling that's
					// appropriate.
				} finally {
					if (is != null) {
						is.close();
					}
				}

				FileOutputStream fos = new FileOutputStream(new File(program.displayTitlePlusFileExtension()));
				fos.write(baos.toByteArray());

				baos.close();

				System.out.println(url);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

}
