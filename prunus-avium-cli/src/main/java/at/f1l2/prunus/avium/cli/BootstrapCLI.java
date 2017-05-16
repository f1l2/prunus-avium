package at.f1l2.prunus.avium.cli;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Bootstrap;

public class BootstrapCLI {

	final static private Logger logger = LoggerFactory.getLogger(BootstrapCLI.class);

	public static void main(String[] args) throws IOException {
		logger.info("Temp folder {}", FileUtils.getTempDirectory().getAbsolutePath());
		Bootstrap.main(args);
	}
}
