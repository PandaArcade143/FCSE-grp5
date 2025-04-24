package boundary;

import java.util.List;

import entity.HDBManager;
// import entity.Inquiry;
import entity.Project;
/**
 * Interface representing HDB staff functionality related to inquiry management.
 * Implemented by user roles such as HDBManagerUI and HDBOfficerUI.
 */
public interface HDBStaffInt {
	 /**
     * Displays all inquiries associated with the provided list of projects.
     *
     * @param proj The list of projects to retrieve and display inquiries for.
     */
    public void viewProjectInquiries(List<Project> proj);
    /**
     * Replies to an inquiry by inquiry ID and optionally triggers inquiry resolution.
     *
     * @param inquiryId    The ID of the inquiry to reply to.
     * @param replyMessage The message content to be sent as a reply.
     */
	public void replyToProjInquiry(String inquiryId, String replyMessage);
	/**
     * Prompts to resolve an inquiry by its ID and updates its status accordingly.
     *
     * @param inquiryId The ID of the inquiry to be resolved.
     */
    public void resolveInquiry(String inquiryId);
}
