package at.f1l2.prunus.avium.cli;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

import at.f1l2.prunus.avium.core.PrunusAvium;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HistoryFileNameProvider extends DefaultHistoryFileNameProvider {

	@Override
	public String getHistoryFileName() {
		return FilenameUtils.concat(FileUtils.getTempDirectory().getAbsolutePath(),
				PrunusAvium.APPLICATION_NAME + "_shell_history");
	}

	@Override
	public String getProviderName() {
		return PrunusAvium.APPLICATION_NAME + " history file name provider.";
	}

}
