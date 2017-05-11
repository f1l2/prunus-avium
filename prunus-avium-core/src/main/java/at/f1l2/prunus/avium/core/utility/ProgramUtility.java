package at.f1l2.prunus.avium.core.utility;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import at.f1l2.prunus.avium.core.model.Program;

public class ProgramUtility {

	private ProgramUtility() {
	}
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyy");
	
	public static String displayTitlePlusFileExtension(Program program) {
		return displayTitle(program).concat(".mp3");
	}
	
	public static String displayTitle(Program program) {
		StringBuilder sb = new StringBuilder();
		if (Objects.nonNull(program.getBegin())) {
			sb.append(dtf.format(program.getBegin()));
			sb.append("_");
		}
		sb.append(normalize(program.getTitle()));
		sb.append("_");
		sb.append(normalize(program.getSubtitle()));
		return sb.toString();
	}
	
	private static String normalize(String input) {
		String midResult = skipHTMLTags(input);
		return midResult.replaceAll("[^a-zäöüA-ZÄÖÜ0-9- ]", "_");
	}
	
	
	private static String skipHTMLTags(String input) {
		return input.replaceAll("<[^>]*>", "");
	}
	
}
