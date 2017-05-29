package at.f1l2.prunus.avium.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import at.f1l2.prunus.avium.core.PrunusAvium;
import at.f1l2.prunus.avium.core.index.IndexManagement;
import at.f1l2.prunus.avium.core.index.configuration.IndexDefaultConfiguration;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.RemotePlayer;
import at.f1l2.prunus.avium.core.player.RemotePlayerAccess;
import at.f1l2.prunus.avium.core.player.configuration.Oe1RemotePlayerConfig;
import at.f1l2.prunus.avium.core.utility.ProgramUtility;

@Component
public class Commands implements CommandMarker {

	private RemotePlayerAccess rpa = new RemotePlayer(new Oe1RemotePlayerConfig(), new ProgramBuilder());

	private IndexManagement im = new IndexManagement(new IndexDefaultConfiguration());

	@CliCommand(value = { "refresh", "r" }, help = "refresh playlist")
	public String refreshPlaylist() {
		List<Program> requestCurrentPlaylist = rpa.requestCurrentPlaylist();
		if (!CollectionUtils.isEmpty(requestCurrentPlaylist)) {
			im.cleanIndex();
			im.buildIndex(requestCurrentPlaylist);
		}
		return "Playlist is refreshed.";
	}

	@CliCommand(value = { "search", "s" }, help = "search playlist")
	public String searchPlaylist(@CliOption(key = { "query", "q" }) String queryStr) {
		List<Program> searchByTitle = im.searchByTitle(queryStr);

		searchByTitle.addAll(im.searchByID(queryStr));

		return ProgramUtility.displayProgramsInShell(searchByTitle);
	}

	@CliCommand(value = { "download", "d" }, help = "search playlist")
	public void download(@CliOption(key = { "query", "q" }) String queryStr)
			throws UnsupportedTagException, InvalidDataException, IOException {

		List<Program> searchByTitle = im.searchByTitle(queryStr);

		if (!CollectionUtils.isEmpty(searchByTitle)) {

			File file = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads").toFile();
			if (!file.exists()) {
				file.mkdirs();
			}

			rpa.downloadPrograms(searchByTitle, file);
			writeMP3Tag(searchByTitle);
		}

	}

	private void writeMP3Tag(List<Program> programs) {
		writeMP3Tag(programs, "OE1");
	}

	private void writeMP3Tag(List<Program> programs, String artist) {

		for (Program program : programs) {

			File file = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads",
					ProgramUtility.displayTitlePlusFileExtension(program)).toFile();

			File tempFile = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads",
					"temp_" + ProgramUtility.displayTitlePlusFileExtension(program)).toFile();
			try {
				Mp3File mp3file = new Mp3File(file);
				ID3v1 id3v1Tag;
				if (mp3file.hasId3v1Tag()) {
					id3v1Tag = mp3file.getId3v1Tag();
				} else {
					// mp3 does not have an ID3v1 tag, let's create one..
					id3v1Tag = new ID3v1Tag();
					mp3file.setId3v1Tag(id3v1Tag);
				}
				id3v1Tag.setArtist(artist);
				id3v1Tag.setTitle(ProgramUtility.displayTitle(program));
				id3v1Tag.setYear(Integer.toString(LocalDate.now().getYear()));
				id3v1Tag.setComment(program.getSubtitle());

				mp3file.save(tempFile.getAbsolutePath());

				boolean delete = file.delete();
				boolean renameTo = tempFile.renameTo(file);

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}