package at.f1l2.prunus.avium.cli;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
		return ProgramUtility.displayProgramsInShell(searchByTitle);
		// return searchByTitle.stream().map(item ->
		// item.toString()).collect(Collectors.joining("\n"));
	}

	@CliCommand(value = { "download", "d" }, help = "search playlist")
	public void download(@CliOption(key = { "query", "q" }) String queryStr) {

		List<Program> searchByTitle = im.searchByTitle(queryStr);

		if (!CollectionUtils.isEmpty(searchByTitle)) {

			File file = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads").toFile();
			if (!file.exists()) {
				file.mkdirs();
			}

			rpa.downloadPrograms(searchByTitle, file);
		}
	}
}