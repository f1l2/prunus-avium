package at.f1l2.prunus.avium.core.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;

import at.f1l2.prunus.avium.core.utility.ProgramUtility;

public class ProgramBuilder {

	private static final String KEYHREF = "href";

	private static final String KEYTITLE = "title";

	private static final String KEYSUBTITLE = "subtitle";

	private static final String KEYSTART = "start";

	private static final String KEYEND = "end";

	public Program build(Map<String, Object> values) {

		Program program = new Program();

		program.setHref(getValueFromMap(KEYHREF, values));
		program.setTitle(ProgramUtility.normalize(getValueFromMap(KEYTITLE, values)));
		program.setSubtitle(ProgramUtility.normalize(getValueFromMap(KEYSUBTITLE, values)));

		String beginTimestamp = getValueFromMap(KEYSTART, values);
		if (Objects.nonNull(beginTimestamp)) {
			LocalDateTime begin = Instant.ofEpochMilli(Long.valueOf(beginTimestamp)).atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			program.setBegin(begin);

		}

		String endTimestamp = getValueFromMap(KEYEND, values);
		if (Objects.nonNull(endTimestamp)) {
			LocalDateTime end = Instant.ofEpochMilli(Long.valueOf(endTimestamp)).atZone(ZoneId.systemDefault())
					.toLocalDateTime();

			program.setEnd(end);
		}

		return program;
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
}
