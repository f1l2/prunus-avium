package at.f1l2.prunus.avium.core.player;

import java.io.File;
import java.util.List;

import at.f1l2.prunus.avium.core.model.Program;

public interface RemotePlayerAccess {

	public String requestCurrentPlaylistRaw();
	
	public List<Program> requestCurrentPlaylist();
	
	public void downloadProgram(Program program, File sink);
	
	public void downloadPrograms(List<Program> programs, File sinkFolder);
	
}
