package interfaces;

interface IHDBStaff{
    List<Inquiry> viewProjectInquiries(Project proj);
    void replyToInquiry(String inquiryId, String replyMessage);
    void resolveInquiry(String inquiryId);
}