package entity;

import java.time.LocalDateTime;

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


    public String getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(String inquiryId) {
        this.inquiryId = inquiryId;
    }

    public String getSenderNRIC() {
        return senderNRIC;
    }

    public void setSenderNRIC(String senderNRIC) {
        this.senderNRIC = senderNRIC;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply (){
        return reply;
    }

    public void setReply (String reply){
        this.reply = reply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Project getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
    }

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
