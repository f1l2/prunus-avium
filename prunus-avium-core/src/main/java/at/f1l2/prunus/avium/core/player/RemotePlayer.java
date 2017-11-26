package at.f1l2.prunus.avium.core.player;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.f1l2.prunus.avium.core.exception.AviumCoreException;
import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.configuration.RemotePlayerConfig;
import at.f1l2.prunus.avium.core.utility.HttpResource;
import at.f1l2.prunus.avium.core.utility.ParseJsonTypeUndefinied;
import at.f1l2.prunus.avium.core.utility.ProgramUtility;

public class RemotePlayer implements RemotePlayerAccess {

	private static Logger logger = LoggerFactory.getLogger(RemotePlayer.class);

	private RemotePlayerConfig config;

	private ProgramBuilder builder;

	public RemotePlayer(RemotePlayerConfig playerConfiguration, ProgramBuilder programBuilder) {
		this.config = playerConfiguration;
		this.builder = programBuilder;
	}

	@Override
	public String requestCurrentPlaylistRaw() {
		return HttpResource.requestResource(config.getCurrentBroadcastListUrl());
	}

	@Override
	public List<Program> requestCurrentPlaylist() {
		return parsePrograms(requestCurrentPlaylistRaw());
	}

	@Override
	public void downloadProgram(Program program, File sink) {
		logger.info("Program downloaded started. Disply title: {}", ProgramUtility.displayTitle(program));
		final LocalDateTime startTime = LocalDateTime.now();

		downloadHardPart(program, sink);

		final LocalDateTime endTime = LocalDateTime.now();
		Duration duration = Duration.between(startTime, endTime);
		logger.info("Program downloaded ended. Duration in seconds: {}. Display title {}", duration.getSeconds(),
				ProgramUtility.displayTitle(program));
	}

	@Override
	public void downloadPrograms(List<Program> programs, File sinkFolder) {
		for (Program program : programs) {
			downloadProgram(program, new File(FilenameUtils.concat(sinkFolder.getAbsolutePath(),
					ProgramUtility.displayTitlePlusFileExtension(program))));
		}
	}

	private void downloadHardPart(Program program, File file) {

		String programDescription = HttpResource.requestResource(program.getHref());

		try {
			List<Map<String, Object>> doo = ParseJsonTypeUndefinied.doo(new ArrayList<>(), "streams",
					programDescription);

			if (doo != null && !doo.isEmpty()) {
				Object object = doo.get(0).get("loopStreamId");
				String url = config.getResourcesUrl() + object.toString();

				FileOutputStream fos = new FileOutputStream(file);
				HttpResource.requestStream(url, fos);
				fos.close();
			}

		} catch (Exception e) {
			throw new AviumCoreException("Unexpected exception occured during download of file.", e);
		}
	}

	private List<Program> parsePrograms(String currentPlaylist) {

		logger.info(currentPlaylist);

		final List<Program> programs = new ArrayList<>();

		if (null == currentPlaylist) {
			return programs;
		}

		try {
			List<Map<String, Object>> rawPrograms = ParseJsonTypeUndefinied.doo(new ArrayList<>(), "[].broadcasts",
					currentPlaylist);
			for (Map<String, Object> rawProgram : rawPrograms) {
				programs.add(builder.build(rawProgram));
			}

		} catch (Exception e) {
			throw new AviumCoreException("", e);
		}
		return programs;
	}
}
