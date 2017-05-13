package at.f1l2.prunus.avium.cli;

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
		String base = System.getProperty("java.io.tmpdir");
		return FilenameUtils.concat(base, PrunusAvium.APPLICATION_NAME + "_shell_history");
	}

	@Override
	public String getProviderName() {
		return PrunusAvium.APPLICATION_NAME + " history file name provider.";
	}
	
}
