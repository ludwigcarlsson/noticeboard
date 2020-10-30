package se.experis.noticeboard.models;

import java.util.Date;
import java.util.Set;


public class DisplayNoticeDTO {

    private String noticeTitle;
    private String noticeContent;
    private String noticeUserName;
    private long accountId;
    private Date noticeTimestamp;
    private Date editedNoticeTimestamp;

    private Set<Comment> comments;

        //GETTERS & SETTERS

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeUserName() {
        return noticeUserName;
    }

    public void setNoticeUserName(String noticeUserName) {
        this.noticeUserName = noticeUserName;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Date getNoticeTimestamp() {
        return noticeTimestamp;
    }

    public void setNoticeTimestamp(Date noticeTimestamp) {
        this.noticeTimestamp = noticeTimestamp;
    }

    public Date getEditedNoticeTimestamp() {
        return editedNoticeTimestamp;
    }

    public void setEditedNoticeTimestamp(Date editedNoticeTimestamp) {
        this.editedNoticeTimestamp = editedNoticeTimestamp;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
