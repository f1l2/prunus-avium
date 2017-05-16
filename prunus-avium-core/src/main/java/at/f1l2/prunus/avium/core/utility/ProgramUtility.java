package at.f1l2.prunus.avium.core.utility;

import java.time.format.DateTimeFormatter;
import java.util.List;
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

	public static String normalize(String input) {

		if (Objects.isNull(input)) {
			return "";
		}

		String midResult = skipHTMLTags(input);
		return midResult.replaceAll("[^a-zäöüA-ZÄÖÜ0-9- ]", "_");
	}

	public static String skipHTMLTags(String input) {
		return input.replaceAll("<[^>]*>", "");
	}

	public static String displayProgramsInShell(List<Program> programs) {

		StringBuilder sb = new StringBuilder();

		for (Program program : programs) {

			sb.append("|| ");
			sb.append(fixedLengthString(program.getUuid().toString(), 20));
			sb.append(" | ");
			sb.append(fixedLengthString(program.getTitle(), 40));
			sb.append(" | ");
			sb.append(fixedLengthString(program.getSubtitle(), 80));
			sb.append(" ||\n");

		}

		return sb.toString();
	}

	public static String fixedLengthString(String value, int length) {

		if (value.length() >= length) {
			value = value.substring(0, length);
		}

		return String.format("%1$" + length + "s", value);
	}
}
