package boundary;

import java.util.List;

import entity.HDBManager;
// import entity.Inquiry;
import entity.Project;

public interface HDBStaffInt {
    public void viewProjectInquiries(List<Project> proj);
	public void replyToProjInquiry(String inquiryId, String replyMessage);
    public void resolveInquiry(String inquiryId);
}
