package se.experis.noticeboard.models;

import java.util.Date;
import java.util.List;
import java.util.Set;


public class DisplayNoticeDTO {

    private String noticeTitle;
    private String noticeContent;
    private String noticeUserName;
    private Date noticeTimestamp;

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

    public Date getNoticeTimestamp() {
        return noticeTimestamp;
    }

    public void setNoticeTimestamp(Date noticeTimestamp) {
        this.noticeTimestamp = noticeTimestamp;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
