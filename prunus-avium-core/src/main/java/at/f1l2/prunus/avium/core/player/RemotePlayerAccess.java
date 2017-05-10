package at.f1l2.prunus.avium.core.player;

import java.io.File;
import java.util.List;

import at.f1l2.prunus.avium.core.model.Program;

public interface RemotePlayerAccess {

	/**
	 * Method returns current playlist in raw format (JSON). It contains all programs within last week.
	 * 
	 * @return
	 */
	public String requestCurrentPlaylistRaw();
	
	/**
	 * Method returns all programs. It contains all programs within last week.
	 * 
	 * @return
	 */
	public List<Program> requestCurrentPlaylist();
	
	/**
	 * Method downloads program and stores it.
	 * 
	 * @param program
	 * @param sink
	 */
	public void downloadProgram(Program program, File sink);
	
	/**
	 * Method downloads a list of programs and stores it in a folder. The folder is passed as parameter.
	 * 
	 * @param programs
	 * @param sinkFolder
	 */
	public void downloadPrograms(List<Program> programs, File sinkFolder);
	
}
