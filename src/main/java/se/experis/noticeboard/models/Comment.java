package se.experis.noticeboard.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIdentityInfo(
        generator =
                ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Date timestamp;

    @Column
    private Date editedTimestamp;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Notice notice;

    //GETTERS & SETTERS

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getEditedTimestamp() {
        return editedTimestamp;
    }

    public void setEditedTimestamp(Date editedTimestamp) {
        this.editedTimestamp = editedTimestamp;
    }
}
