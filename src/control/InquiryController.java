package control;

import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

import entity.Inquiry;
import entity.Project;
import helpers.DataManager;

public class InquiryController {
    private static List<Inquiry> inquiries = DataManager.getInquiries();

    // Create an inquiry
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

    // Edit inquiry (by applicant)
    public static void editInquiry(String senderNRIC, Inquiry inquiry, String newMessage) {
        if (inquiries.contains(inquiry) && inquiry.getSenderNRIC().equals(senderNRIC)) {
            inquiry.setMessage(newMessage);
            System.out.println("Inquiry updated.");
        } else {
            System.out.println("Inquiry not found or permission denied.");
        }
    }

    // Delete inquiry (by applicant)
    public static void deleteInquiry(String senderNRIC, Inquiry inquiry) {
        if (inquiries.contains(inquiry) && inquiry.getSenderNRIC().equals(senderNRIC)) {
            inquiries.remove(inquiry);
            System.out.println("Inquiry deleted.");
        } else {
            System.out.println("Cannot delete: Inquiry not found or unauthorized.");
        }
    }

    // View all inquiries by applicant
    public static List<Inquiry> viewInquiries(String senderNRIC) {
        return inquiries.stream()
                .filter(inq -> inq.getSenderNRIC().equals(senderNRIC))
                .collect(Collectors.toList());
    }

    // View all inquiries for a project
    public static List<Inquiry> viewInquiries(Project project) {
        return inquiries.stream()
                .filter(inq -> project.equals(inq.getRelatedProject()))
                .collect(Collectors.toList());
    }

    // View all inquiries
    public static List<Inquiry> allInquiries() {
        return new ArrayList<>(inquiries);
    }

    // View open inquiries
    public static List<Inquiry> getOpenInquiries() {
        return inquiries.stream()
                .filter(inq -> inq.getStatus().equalsIgnoreCase("Open"))
                .collect(Collectors.toList());
    }

    // Resolve an inquiry by ID
    public static void resolveInquiry(String inquiryId) {
        for (Inquiry inq : inquiries) {
            if (inq.getInquiryId().equals(inquiryId)) {
                if (!inq.getStatus().equalsIgnoreCase("Resolved")) {
                    inq.setStatus("Resolved");
                    inq.setResolvedAt(LocalDateTime.now());
                    System.out.println("Inquiry marked as resolved:\n" + inq);
                } else {
                    System.out.println("Inquiry is already resolved.");
                }
                return;
            }
        }
        System.out.println("Inquiry not found.");
    }

    // Reply to an inquiry
    public static void replyToInquiry(String inquiryId, String replyMessage) {
        for (Inquiry inq : inquiries) {
            if (inq.getInquiryId().equals(inquiryId)) {
                inq.setReply(replyMessage);
                System.out.println("Reply sent:\n" + inq);
                return;
            }
        }
        System.out.println("Inquiry not found.");
    }
}
