package at.f1l2.prunus.avium.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import at.f1l2.prunus.avium.core.index.IndexManagement;
import at.f1l2.prunus.avium.core.index.configuration.IndexDefaultConfiguration;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.RemotePlayer;
import at.f1l2.prunus.avium.core.player.RemotePlayerAccess;
import at.f1l2.prunus.avium.core.player.configuration.Oe1RemotePlayerConfig;
import at.f1l2.prunus.avium.core.utility.ProgramUtility;

public class PrunusAviumFacade {

	private RemotePlayerAccess rpa = new RemotePlayer(new Oe1RemotePlayerConfig(), new ProgramBuilder());

	private IndexManagement im = new IndexManagement(new IndexDefaultConfiguration());

	public void refreshPlaylist() {
		final List<Program> requestCurrentPlaylist = rpa.requestCurrentPlaylist();
		if (!requestCurrentPlaylist.isEmpty())
			im.cleanIndex();
		im.buildIndex(requestCurrentPlaylist);
	}

	public List<Program> search(String queryStr) {
		return im.search(queryStr);
	}

	public void downloadAllBySearch(String queryStr) {
		final List<Program> programs = search(queryStr);

		if (!programs.isEmpty()) {

			File file = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "downloads").toFile();
			if (!file.exists()) {
				file.mkdirs();
			}
			rpa.downloadPrograms(programs, file);
			ProgramUtility.writeMP3Tag(programs, "Oe1");

		}
	}
}
