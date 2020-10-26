package se.experis.noticeboard.models;

import java.util.Date;
import java.util.List;


public class DisplayNoticeDTO {

    private String noticeTitle;
    private String noticeContent;
    private String noticeUserName;
    private Date noticeTimestamp;

    private List<Comment> comments;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
