package at.f1l2.prunus.avium.core;



import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.RemotePlayer;
import at.f1l2.prunus.avium.core.player.RemotePlayerAccess;
import at.f1l2.prunus.avium.core.player.configuration.Oe1RemotePlayerConfig;


public class PrunusAvium {

	private static Logger logger = LoggerFactory.getLogger(PrunusAvium.class);

	public static void main(String[] args) {

		logger.info("Prunus Avium started");

		RemotePlayerAccess rpa = new RemotePlayer(new Oe1RemotePlayerConfig(), new ProgramBuilder());

		List<Program> programs = rpa.requestCurrentPlaylist();
		
		logger.info("# programs: {}", programs.size());


		List<Program> dimensionen = programs.stream().sorted(Comparator.comparing(Program::getBegin))
				.filter(item -> item.getTitle().startsWith("Dimensionen")).collect(Collectors.toList());
		//

		 rpa.downloadPrograms(dimensionen, new File(System.getProperty("java.io.tmpdir")));
		 
		 logger.info("Program has finished");
	}



}
