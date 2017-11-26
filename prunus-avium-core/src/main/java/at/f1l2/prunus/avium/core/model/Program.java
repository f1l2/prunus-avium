package at.f1l2.prunus.avium.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Program {

	private UUID uuid;

	private String href;

	private String title;

	private String subtitle;

	private String programTitle;

	private LocalDateTime begin;

	private LocalDateTime end;

	public Program() {
		setUuid(UUID.randomUUID());
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

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

	@Override
	public String toString() {
		return "Program [href=" + href + ", title=" + title + ", subtitle=" + subtitle + ", begin=" + begin + ", end="
				+ end + "]";
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

}
