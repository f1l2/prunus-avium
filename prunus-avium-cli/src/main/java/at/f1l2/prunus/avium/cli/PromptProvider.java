package at.f1l2.prunus.avium.cli;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

import at.f1l2.prunus.avium.core.PrunusAvium;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PromptProvider extends DefaultPromptProvider {

	@Override
	public String getPrompt() {
		return "prunus_avium>";
	}

	@Override
	public String getProviderName() {
		return PrunusAvium.APPLICATION_NAME + " prompt provider";
	}

}
