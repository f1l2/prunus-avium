package at.f1l2.prunus.avium.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.f1l2.prunus.avium.core.index.IndexManagement;
import at.f1l2.prunus.avium.core.index.configuration.IndexDefaultConfiguration;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.RemotePlayer;
import at.f1l2.prunus.avium.core.player.RemotePlayerAccess;
import at.f1l2.prunus.avium.core.player.configuration.Oe1RemotePlayerConfig;

public class PrunusAvium {

	public static final String APPLICATION_NAME = "Prunus Avium";

	private static Logger logger = LoggerFactory.getLogger(PrunusAvium.class);

	private PrunusAvium() {
		// Private constructor due class only contains static methods.
	}

	public static void main(String[] args) throws IOException {

		logger.info("Prunus Avium started");

		RemotePlayerAccess rpa = new RemotePlayer(new Oe1RemotePlayerConfig(), new ProgramBuilder());

		List<Program> programs = rpa.requestCurrentPlaylist();

		logger.info("# programs: {}", programs.size());

		IndexManagement im = new IndexManagement(new IndexDefaultConfiguration());
		im.cleanIndex();
		im.buildIndex(programs);

		final List<Program> dimensionen = im.search("Dimensionen");

		logger.info("File storage: {}", System.getProperty("java.io.tmpdir"));

		rpa.downloadPrograms(dimensionen, new File(System.getProperty("java.io.tmpdir")));

		logger.info("Program has finished");

	}
}
