package at.f1l2.prunus.avium.core.index.configuration;

import java.nio.file.Path;

public abstract class IndexConfiguration {

	protected Path indexPath;

	public IndexConfiguration() {
		setIndexPath();
	}

	public Path getIndexPath() {
		return indexPath;
	}

	public abstract void setIndexPath();

}
