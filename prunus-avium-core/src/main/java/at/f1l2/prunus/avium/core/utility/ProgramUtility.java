package at.f1l2.prunus.avium.core.utility;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import at.f1l2.prunus.avium.core.model.Program;

public class ProgramUtility {

	private ProgramUtility() {
	}

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");

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
		return midResult.replaceAll("[^a-zA-Z0-9- ]", "_");
	}

	public static String skipHTMLTags(String input) {
		return input.replaceAll("<[^>]*>", "");
	}

	public static String displayProgramsInShell(List<Program> programs) {

		int lengthCol1 = 20;
		int lengthCol2 = 19;
		int lengthCol3 = 19;
		int lengthCol4 = 40;
		int lengthCol5 = 80;
		int lengthDelimiter = 18;
		int lenthTable = lengthCol1 + lengthCol2 + lengthCol3 + lengthCol4 + lengthCol5 + lengthDelimiter;

		StringBuilder sb = new StringBuilder();

		sb.append(String.join("", Collections.nCopies(lenthTable, "_")));
		sb.append("\n\n");
		for (Program program : programs) {
			sb.append("|| ");
			sb.append(fixedLengthString(program.getUuid().toString(), lengthCol1));
			sb.append(" | ");
			sb.append(DateTimeFormatter.ISO_DATE_TIME.format(program.getBegin()));
			sb.append(" | ");
			sb.append(DateTimeFormatter.ISO_DATE_TIME.format(program.getEnd()));
			sb.append(" | ");
			sb.append(fixedLengthString(program.getTitle(), lengthCol4));
			sb.append(" | ");
			sb.append(fixedLengthString(program.getSubtitle(), lengthCol5));
			sb.append(" ||\n");
		}

		sb.append(String.join("", Collections.nCopies(lenthTable, "_")));
		sb.append("\n");
		return sb.toString();
	}

	public static String fixedLengthString(String value, int length) {

		if (value.length() >= length) {
			value = value.substring(0, length);
		}

		return String.format("%1$" + length + "s", value);
	}
}
