package at.f1l2.prunus.avium.core.player.configuration;

public class Oe1RemotePlayerConfig extends RemotePlayerConfig {

	@Override
	public void setCurrentBroadcastListUrl() {	
		this.currentBroadcastListUrl = "https://audioapi.orf.at/oe1/api/json/current/broadcasts";
	}

	@Override
	public void setResourcesUrl() {
		this.resourcesUrl = "http://loopstream01.apa.at/?channel=oe1&shoutcast=0&id=";
	}

}
