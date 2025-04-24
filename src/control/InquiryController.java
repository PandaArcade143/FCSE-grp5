package control;

import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

import entity.Inquiry;
import entity.Project;
import helpers.DataManager;

/**
 * Controls logic related to inquiries in the system.
 * Provides methods to create, edit, delete, view, reply to, and resolve inquiries.
 */
public class InquiryController {
    private static List<Inquiry> inquiries = DataManager.getInquiries();

    /**
     * Creates and submits a new inquiry.
     *
     * @param senderNRIC the NRIC of the applicant submitting the inquiry
     * @param subject the subject of the inquiry
     * @param project the related project
     * @param message the message content
     */
    public static void createInquiry(String senderNRIC, String subject, Project project, String message) {
        String inquiryId = UUID.randomUUID().toString();

        Inquiry newInquiry = new Inquiry(
                inquiryId,
                senderNRIC,
                subject,
                message,
                "Open",
                LocalDateTime.now(),
                null,
                project
        );

        inquiries.add(newInquiry);
        System.out.println("Inquiry submitted successfully.\n" + newInquiry);
    }

    /**
     * Edits an existing inquiry message if the sender matches the logged-in user.
     *
     * @param senderNRIC the NRIC of the user attempting to edit
     * @param inquiry the inquiry to be edited
     * @param newMessage the new message content
     */
    public static void editInquiry(String senderNRIC, Inquiry inquiry, String newMessage) {
        if (inquiries.contains(inquiry) && inquiry.getSenderNRIC().equals(senderNRIC)) {
            inquiry.setMessage(newMessage);
            System.out.println("Inquiry updated.");
        } else {
            System.out.println("Inquiry not found or permission denied.");
        }
    }

    /**
     * Deletes an inquiry if the sender matches the logged-in user.
     *
     * @param senderNRIC the NRIC of the user attempting to delete
     * @param inquiry the inquiry to delete
     */
    public static void deleteInquiry(String senderNRIC, Inquiry inquiry) {
        if (inquiries.contains(inquiry) && inquiry.getSenderNRIC().equals(senderNRIC)) {
            inquiries.remove(inquiry);
            System.out.println("Inquiry deleted.");
        } else {
            System.out.println("Cannot delete: Inquiry not found or unauthorized.");
        }
    }

    /**
     * Views all inquiries submitted by a specific user.
     *
     * @param senderNRIC the NRIC of the user
     * @return list of inquiries submitted by the user
     */
    public static List<Inquiry> viewInquiries(String senderNRIC) {
        return inquiries.stream()
                .filter(inq -> inq.getSenderNRIC().equals(senderNRIC))
                .collect(Collectors.toList());
    }

    /**
     * Views all inquiries related to a specific project.
     *
     * @param project the project to filter by
     * @return list of inquiries linked to the project
     */
    public static List<Inquiry> viewInquiries(Project project) {
        return inquiries.stream()
                .filter(inq -> project.equals(inq.getRelatedProject()))
                .collect(Collectors.toList());
    }

    /**
     * Returns all inquiries in the system.
     *
     * @return list of all inquiries
     */
    public static List<Inquiry> allInquiries() {
        return new ArrayList<>(inquiries);
    }

    /**
     * Returns a list of inquiries that are still open (not resolved).
     *
     * @return list of open inquiries
     */
    public static List<Inquiry> getOpenInquiries() {
        return inquiries.stream()
                .filter(inq -> inq.getStatus().equalsIgnoreCase("Open"))
                .collect(Collectors.toList());
    }

    /**
     * Resolves an inquiry by setting its status and resolution time.
     *
     * @param inquiryId the ID of the inquiry to resolve
     */
    public static void resolveInquiry(String inquiryId) {
        for (Inquiry inq : inquiries) {
            if (inq.getInquiryId().equals(inquiryId)) {
                if (!inq.getStatus().equalsIgnoreCase("Resolved")) {
                    inq.setStatus("Resolved");
                    inq.setResolvedAt(LocalDateTime.now());
                    //System.out.println("Inquiry marked as resolved:\n" + inq);
                } else {
                    System.out.println("Inquiry is already resolved.");
                }
                return;
            }
        }
        System.out.println("Inquiry not found.");
    }

    /**
     * Replies to an inquiry by setting the reply message.
     *
     * @param inquiryId the ID of the inquiry to reply to
     * @param replyMessage the reply message
     */
    public static void replyToInquiry(String inquiryId, String replyMessage) {
        for (Inquiry inq : inquiries) {
        	if(inq.getStatus() == "Resolved") {
        		System.out.println("Inquiry already resolved.");
        	}
            if (inq.getInquiryId().equals(inquiryId)) {
                inq.setReply(replyMessage);
                System.out.println("\n\n\nReply sent~~~~\n" + inq);
                return;
            }
        }
        System.out.println("Inquiry not found.");
    }
}
