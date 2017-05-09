package at.f1l2.prunus.avium.core.player;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.f1l2.prunus.avium.core.PrunusAvium;
import at.f1l2.prunus.avium.core.exception.AviumCoreException;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.player.configuration.RemotePlayerConfig;
import at.f1l2.prunus.avium.core.utility.ParseJsonTypeUndefinied;

public class RemotePlayer implements RemotePlayerAccess {
	
	private RemotePlayerConfig config;
	
	public  RemotePlayer(RemotePlayerConfig playerConfiguration) {
		this.config = playerConfiguration;
	}
	

	@Override
	public String requestCurrentPlaylistRaw() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Program> requestCurrentPlaylist() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void downloadProgram(Program program, File sink) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void downloadPrograms(List<Program> programs, File sinkFolder) {
		// TODO Auto-generated method stub
		
	}
	
	private List<Program> parsePrograms(String currentPlaylist) {

		final List<Program> programs = new ArrayList<>();

		try {
			List<Map<String, Object>> doo = ParseJsonTypeUndefinied.doo(new ArrayList<>(), "[].broadcasts", currentPlaylist);

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
			throw new AviumCoreException("", e);
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


	
	
}
