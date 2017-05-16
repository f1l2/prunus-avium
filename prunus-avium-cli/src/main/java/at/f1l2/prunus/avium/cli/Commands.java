package at.f1l2.prunus.avium.cli;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import at.f1l2.prunus.avium.core.index.IndexManagement;
import at.f1l2.prunus.avium.core.index.configuration.IndexDefaultConfiguration;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.RemotePlayer;
import at.f1l2.prunus.avium.core.player.RemotePlayerAccess;
import at.f1l2.prunus.avium.core.player.configuration.Oe1RemotePlayerConfig;

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
		return searchByTitle.stream().map(item -> item.toString()).collect(Collectors.joining("\n"));
	}

}