package at.f1l2.prunus.avium.core.index.configuration;

import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import at.f1l2.prunus.avium.core.PrunusAvium;

public class IndexDefaultConfiguration extends IndexConfiguration {

	public IndexDefaultConfiguration() {
		super();
	}

	@Override
	public void setIndexPath() {
		Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "index");
		this.indexPath = Paths.get(FileUtils.getUserDirectoryPath(), PrunusAvium.APPLICATION_NAME, "index");
	}

}
