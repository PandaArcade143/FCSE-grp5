package entity;

interface IHDBStaff{
    void replyToInquiry(String inquiryId, String replyMessage);
    void resolveInquiry(String inquiryId);
}