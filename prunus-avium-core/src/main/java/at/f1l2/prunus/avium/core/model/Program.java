package at.f1l2.prunus.avium.core.model;

import java.time.LocalDateTime;

public class Program {

	private String href;

	private String title;
	
	private String subtitle;
	
	private LocalDateTime begin;
	
	private LocalDateTime end;

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getSubtitle() {
		return subtitle;
	}


	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	
	public LocalDateTime getBegin() {
		return begin;
	}


	public void setBegin(LocalDateTime begin) {
		this.begin = begin;
	}


	public LocalDateTime getEnd() {
		return end;
	}


	public void setEnd(LocalDateTime end) {
		this.end = end;
	}


	public String getHref() {
		return href;
	}


	public void setHref(String href) {
		this.href = href;
	}
	
	
	public String displayTitlePlusFileExtension() {
		return displayTitle().concat(".mp3");
	}
	
	public String displayTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append(normalize(getTitle()));
		sb.append(" - ");
		sb.append(normalize(getSubtitle()));
		return sb.toString();
	}
	
	private String normalize(String input) {
		String midResult = skipHTMLTags(input);
		return midResult.replaceAll("[^a-z����A-Z���0-9- ]", "_");
	}
	
	
	private String skipHTMLTags(String input) {
		return input.replaceAll("<[^>]*>", "");
	}
	
	
	
	@Override
	public String toString() {
		return "Program [href=" + href + ", title=" + title + ", subtitle=" + subtitle + ", begin=" + begin + ", end="
				+ end + "]";
	}

}
