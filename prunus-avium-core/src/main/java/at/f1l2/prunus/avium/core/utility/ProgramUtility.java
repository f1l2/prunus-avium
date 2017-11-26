package at.f1l2.prunus.avium.core.utility;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.Mp3File;

import at.f1l2.prunus.avium.core.PrunusAvium;
import at.f1l2.prunus.avium.core.model.Program;

public class ProgramUtility {

	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("ddMMyyyy");

	private static final String MP3_FILE_EXTENSION = ".mp3";

	private ProgramUtility() {
	}

	public static String displayTitlePlusFileExtension(Program program) {
		return displayTitle(program).concat(MP3_FILE_EXTENSION);
	}

	public static String displayTitle(Program program) {
		final StringBuilder sb = new StringBuilder();
		if (Objects.nonNull(program.getBegin())) {
			sb.append(DTF.format(program.getBegin()));
			sb.append("_");
		}
		sb.append(complyWithFilenameConvention(program.getProgramTitle()));
		sb.append("_");
		sb.append(complyWithFilenameConvention(program.getTitle()));
		return sb.toString();
	}

	public static String complyWithFilenameConvention(String input) {
		if (Objects.isNull(input)) {
			return "";
		}
		return input.replaceAll("[\\\\/:*?\"<>|]", "_");
	}

	public static String skipHTMLTags(String input) {
		if (Objects.isNull(input)) {
			return "";
		}
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
			sb.append(fixedLengthString(program.getProgramTitle(), lengthCol4));
			sb.append(" | ");
			sb.append(fixedLengthString(program.getTitle(), lengthCol5));
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

	public static void writeMP3Tag(List<Program> programs, String artist) {

		for (Program program : programs) {
			writeMP3Tag(program, artist);
		}
	}

	public static void writeMP3Tag(Program program, String artist) {
		final File file = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads",
				ProgramUtility.displayTitlePlusFileExtension(program)).toFile();

		final File tempFile = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads",
				"temp_" + ProgramUtility.displayTitlePlusFileExtension(program)).toFile();
		try {
			final Mp3File mp3file = new Mp3File(file);
			ID3v1 id3v1Tag;
			if (mp3file.hasId3v1Tag()) {
				id3v1Tag = mp3file.getId3v1Tag();
			} else {
				id3v1Tag = new ID3v1Tag();
				mp3file.setId3v1Tag(id3v1Tag);
			}
			id3v1Tag.setArtist(artist);
			id3v1Tag.setTitle(ProgramUtility.displayTitle(program));
			id3v1Tag.setYear(program.getBegin() != null ? String.valueOf(program.getBegin().getYear())
					: String.valueOf(LocalDate.now().getYear()));
			id3v1Tag.setAlbum(program.getProgramTitle() != null ? program.getProgramTitle() : "");
			id3v1Tag.setComment(program.getSubtitle());

			mp3file.save(tempFile.getAbsolutePath());

			boolean delete = file.delete();
			if (!delete) {
			}

			boolean renameTo = tempFile.renameTo(file);
			if (!renameTo) {
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
