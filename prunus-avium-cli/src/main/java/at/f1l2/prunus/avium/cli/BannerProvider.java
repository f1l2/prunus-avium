
package at.f1l2.prunus.avium.cli;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

		InputStream resourceAsStream = null;
		try {
			resourceAsStream = getClass().getClassLoader().getResourceAsStream("/banner.txt");
		} catch (Exception e1) {
			logger.error("", e1);
			return "Banner missing. First.";
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {

			String line = null;
			while ((line = br.readLine()) != null) {
				banner.append(line);
				banner.append(OsUtils.LINE_SEPARATOR);
			}
		} catch (Exception e) {
			logger.error("", e);
			return "Banner missing. Second.";
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