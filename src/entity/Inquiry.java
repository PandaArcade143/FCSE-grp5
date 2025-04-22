package entity;

import java.time.LocalDateTime;

/**
 * Represents an inquiry submitted by an applicant regarding a BTO project.
 * Contains metadata such as subject, message, status, timestamps, and reply.
 */

public class Inquiry {
    private String inquiryId; 
    private String senderNRIC;
    private String subject; 
    private String message;
    private String reply;
    private String status; 
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private Project relatedProject;

    /**
     * Constructs an Inquiry with all required fields.
     *
     * @param inquiryId       the unique identifier of the inquiry
     * @param senderNRIC      the NRIC of the user who submitted the inquiry
     * @param subject         the subject or title of the inquiry
     * @param message         the detailed message content
     * @param status          the current status of the inquiry (e.g. open/closed)
     * @param createdAt       the timestamp when the inquiry was created
     * @param resolvedAt      the timestamp when the inquiry was resolved (can be null)
     * @param relatedProject  the project this inquiry refers to (can be null)
     */
    public Inquiry(String inquiryId, String senderNRIC, String subject, String message,
                   String status, LocalDateTime createdAt, LocalDateTime resolvedAt, Project relatedProject) {
        this.inquiryId = inquiryId;
        this.senderNRIC = senderNRIC;
        this.subject = subject;
        this.message = message;
        this.reply = "";
        this.status = status;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
        this.relatedProject = relatedProject;
    }

    /** @return the inquiry's unique ID */
    public String getInquiryId() {
        return inquiryId;
    }

    /** @param inquiryId the new ID to assign */
    public void setInquiryId(String inquiryId) {
        this.inquiryId = inquiryId;
    }

    /** @return the NRIC of the sender */
    public String getSenderNRIC() {
        return senderNRIC;
    }

    /** @param senderNRIC the NRIC of the person who submitted the inquiry */
    public void setSenderNRIC(String senderNRIC) {
        this.senderNRIC = senderNRIC;
    }

    /** @return the subject of the inquiry */
    public String getSubject() {
        return subject;
    }

    /** @param subject the subject or title of the inquiry */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /** @return the main message body of the inquiry */
    public String getMessage() {
        return message;
    }

    /** @param message the detailed inquiry message */
    public void setMessage(String message) {
        this.message = message;
    }

    /** @return the officer's reply to the inquiry */
    public String getReply (){
        return reply;
    }

    /** @param reply the officer's reply message */ 
    public void setReply (String reply){
        this.reply = reply;
    }

    /** @return the status of the inquiry (e.g., Open, Resolved) */
    public String getStatus() {
        return status;
    }

    /** @param status the status to set for this inquiry */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return the creation timestamp of the inquiry */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt the creation time to set */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the time the inquiry was resolved */
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    /** @param resolvedAt the time the inquiry was resolved */
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    /** @return the project associated with this inquiry */
    public Project getRelatedProject() {
        return relatedProject;
    }

    /** @param relatedProject the project linked to this inquiry */
    public void setRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
    }
    
    /**
     * Returns a human-readable string representing the full inquiry details.
     *
     * @return formatted inquiry details
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inquiry ID: ").append(inquiryId).append("\n");
        sb.append("Sender NRIC: ").append(senderNRIC).append("\n");
        sb.append("Subject: ").append(subject).append("\n");
        sb.append("Message: ").append(message).append("\n");
        sb.append("Reply: ").append(reply != null && !reply.isEmpty() ? reply : "No reply yet").append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Created At: ").append(createdAt).append("\n");
        sb.append("Resolved At: ").append(resolvedAt != null ? resolvedAt : "Not resolved yet").append("\n");
        sb.append("Related Project: ").append(relatedProject != null ? relatedProject.getName() : "None");

        return sb.toString();
    }

}
