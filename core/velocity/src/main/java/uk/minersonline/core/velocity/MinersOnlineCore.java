package uk.minersonline.core.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
		id = "miners_online_core",
		name = "MinersOnlineCore",
		version = BuildConstants.VERSION,
		description = "The core plugin for the Miners Online server",
		url = "https://minersonline.uk",
		authors = {"ajh123"}
)
public class MinersOnlineCore {

	@Inject
	private Logger logger;

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
	}
}
