public class QueryExecutor {
    public QueryExecutor(DBManager dbManager) {
    }

    public String findUserWithShortestEmails() {
        return null;
    }

    public String getUsersWithSpecificSubject(String subject) {
        return subject;
    }

    public String getUsersWithoutSpecificSubject(String subject) {
        return subject;
    }

    public void sendEmailToAllRecipients(int senderId, String subject, String text) {
    }

    public String getUsersAndEmailCounts() {
        return null;
    }

    public String getUserEmails(int userId) {
        return null;
    }

    public String getEmailsByDate(String startDate, String endDate) {
        return startDate;
    }

    public String getEmailsByKeywords(String keywords) {
        return keywords;
    }

    public String getEmailsBySenderReceiver(int senderId, int receiverId) {
        return null;
    }
}
