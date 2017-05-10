package at.f1l2.prunus.avium.core.player.configuration;

public abstract class RemotePlayerConfig {

	protected String currentBroadcastListUrl;
	
	protected String resourcesUrl;

	public RemotePlayerConfig() {
		setCurrentBroadcastListUrl();
		setResourcesUrl();
	}
	
	
	public String getCurrentBroadcastListUrl() {
		return currentBroadcastListUrl;
	}

	public abstract void setCurrentBroadcastListUrl();


	public String getResourcesUrl() {
		return resourcesUrl;
	}


	public abstract void setResourcesUrl();

	
	
}
