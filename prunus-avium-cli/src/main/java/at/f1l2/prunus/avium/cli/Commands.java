package at.f1l2.prunus.avium.cli;

import java.util.List;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import at.f1l2.prunus.avium.core.PrunusAviumFacade;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.utility.ProgramUtility;

@Component
public class Commands implements CommandMarker {

	private PrunusAviumFacade facade = new PrunusAviumFacade();

	@CliCommand(value = { "refresh", "r" }, help = "refresh playlist")
	public String refreshPlaylist() {
		facade.refreshPlaylist();
		return "Playlist is refreshed.";
	}

	@CliCommand(value = { "search", "s" }, help = "search playlist")
	public String searchPlaylist(@CliOption(key = { "query", "q" }) String queryStr,
			@CliOption(key = { "time", "t" }) String time) {
		final List<Program> searchByTitle = facade.search(queryStr);
		return ProgramUtility.displayProgramsInShell(searchByTitle);
	}

	@CliCommand(value = { "download", "d" }, help = "search playlist")
	public String download(@CliOption(key = { "query", "q" }) String queryStr) {
		try {
			facade.downloadAllBySearch(queryStr);
		} catch (Exception e) {
			return "An exception occurred. For further details can be found in the log file.";
		}
		return "Download finished successfully.";
	}
}
