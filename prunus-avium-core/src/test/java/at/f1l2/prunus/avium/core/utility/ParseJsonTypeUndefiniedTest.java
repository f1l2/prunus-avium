package at.f1l2.prunus.avium.core.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ParseJsonTypeUndefiniedTest {
	
	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		
		String json = "[{\"day\":20170502,"
				+ "\"broadcasts\":"
				+ "["
				+ "{\"href\":\"https://audioapi.orf.at/fm4/api/json/current/broadcast/4CZ/20170502\",\"station\":\"fm4\",\"entity\":\"Broadcast\",\"id\":6342,\"broadcastDay\":20170502,\"programKey\":\"4CZ\",\"program\":\"4CZ\",\"title\":\"Chez Hermes\",\"subtitle\":\"<p>Mit Herrn Hermes. Aktuelles, Abstruses und Außergewöhnliches mit Anstand und Stil gesalzen und durch den Äther gepfeffert.</p>\",\"ressort\":null,\"state\":\"C\",\"isOnDemand\":true,\"isGeoProtected\":false,\"start\":1493762373000,\"startISO\":\"2017-05-02T23:59:33+02:00\",\"startOffset\":-7200000,\"scheduledStart\":1493762400000,\"scheduledStartISO\":\"2017-05-03T00:00:00+02:00\",\"scheduledStartOffset\":-7200000,\"end\":1493765976000,\"endISO\":\"2017-05-03T00:59:36+02:00\",\"endOffset\":-7200000,\"scheduledEnd\":1493766000000,\"scheduledEndISO\":\"2017-05-03T01:00:00+02:00\",\"scheduledEndOffset\":-7200000,\"niceTime\":1493762400000,\"niceTimeISO\":\"2017-05-03T00:00:00+02:00\",\"niceTimeOffset\":-7200000},"
				+ "{\"href\":\"https://audioapi.orf.at/fm4/api/json/current/broadcast/4SL/20170502\",\"station\":\"fm4\",\"entity\":\"Broadcast\",\"id\":6343,\"broadcastDay\":20170502,\"programKey\":\"4SL\",\"program\":\"4SL\",\"title\":\"Sleepless\",\"subtitle\":\"<p>Less sleep, but a whole lot more of everything else. With Joe-Joe, Robin Lee & Johnny Bliss.</p>\",\"ressort\":null,\"state\":\"C\",\"isOnDemand\":true,\"isGeoProtected\":false,\"start\":1493765976000,\"startISO\":\"2017-05-03T00:59:36+02:00\",\"startOffset\":-7200000,\"scheduledStart\":1493766000000,\"scheduledStartISO\":\"2017-05-03T01:00:00+02:00\",\"scheduledStartOffset\":-7200000,\"end\":1493784011000,\"endISO\":\"2017-05-03T06:00:11+02:00\",\"endOffset\":-7200000,\"scheduledEnd\":1493784000000,\"scheduledEndISO\":\"2017-05-03T06:00:00+02:00\",\"scheduledEndOffset\":-7200000,\"niceTime\":1493766000000,\"niceTimeISO\":\"2017-05-03T01:00:00+02:00\",\"niceTimeOffset\":-7200000}]"
				+ "}]";
		
		List<Map<String, Object>> doo = ParseJsonTypeUndefinied.doo(new ArrayList<>(), "[].broadcasts", json);
		
		for (Map<String, Object> do1 : doo) {
			System.out.println(do1.get("href"));
			System.out.println(do1.get("title"));
			System.out.println(do1.get("subtitle"));
			
			
		}
		
	}

}
