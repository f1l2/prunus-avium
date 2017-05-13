
package at.f1l2.prunus.avium.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BannerProvider extends DefaultBannerProvider {
	
	private Logger logger = LoggerFactory.getLogger(BannerProvider.class);

	@Override
	public String getBanner() {

		StringBuilder banner = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/META-INF/spring/banner.txt")))) {

			String line = null;
			while ((line = br.readLine()) != null) {
				banner.append(line);
				banner.append(OsUtils.LINE_SEPARATOR);
			}
		} catch (Exception e) {
			logger.error("", e);
		} 

		banner.append("Version:" + this.getVersion());
		return banner.toString();
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getWelcomeMessage() {
		return "Welcome to Prunus Avium Provider CLI";
	}

	@Override
	public String getProviderName() {
		return "Prunus Avium Provider";
	}
}