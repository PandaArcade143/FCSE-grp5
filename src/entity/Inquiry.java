package entity;

import java.time.LocalDateTime;

public class Inquiry {
	String senderNRIC;
	String message;
	Project relatedProject;
	String reply;
	boolean resolved;
	LocalDateTime timestamp;

	public Inquiry(String senderNric, String message, Project relatedProject) {
		this.senderNRIC = senderNric;
		this.message = message;
		this.relatedProject = relatedProject;
	}

	public void setReply(String reply) {
		this.reply = reply;
		
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
		
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
		
	}

	public String getSenderNRIC() {
		return senderNRIC;
	}

	public void setSenderNRIC(String senderNRIC) {
		this.senderNRIC = senderNRIC;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Project getRelatedProject() {
		return relatedProject;
	}

	public void setRelatedProject(Project relatedProject) {
		this.relatedProject = relatedProject;
	}

	public String getReply() {
		return reply;
	}

	public boolean isResolved() {
		return resolved;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	

}
