package at.f1l2.prunus.avium.core.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.f1l2.prunus.avium.core.exception.AviumCoreException;

public class ParseJsonTypeUndefinied {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <K, V> List<Map<K, V>> doo(List<Map<K, V>> result, String code, Object json)
			throws JsonParseException, JsonMappingException, IOException {

		String[] commandos = code.split("\\.");
		String commando = commandos[0];

		boolean isLast = commandos.length <= 1 ? true : false;

		if (json.toString().startsWith("[{")) {
			Map<K, V>[] maps = objectMapper.readValue(json.toString(), HashMap[].class);
			for (Map<K, V> map : maps) {
				if (!isLast) {
					doo(result, code.substring(commando.length() + 1), map);
				} else {
					result.add(map);
				}
			}
			return result;
		}

		if (json.toString().startsWith("{")) {

			Map<K, V> map;
			if (json instanceof String) {
				map = objectMapper.readValue(json.toString(), HashMap.class);
			} else {
				map = (HashMap<K, V>) json;
			}

			OutputStream os = new ByteArrayOutputStream();
			objectMapper.writeValue(os, map.get(commando));

			return doo(result, isLast ? "" : code.substring(commando.length() + 1), os.toString());

		}

		throw new AviumCoreException("Unexpected position.");
	}
}
