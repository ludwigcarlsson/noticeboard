package se.experis.noticeboard.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@JsonIdentityInfo(
        generator =
                ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    @CreationTimestamp
    private Date timestamp;

    @Column
    private Date editedTimestamp;

    @JsonGetter("account")
    private String account() {
        return "/api/v1/account/"+account.getUserName();
    }

    @ManyToOne
    private Account account;

    @JsonGetter("comments")
    private List<String> comments() {
        return comments.stream()
                .map(comment -> {
                    return "/api/v1/notices/"+getId()+"/comments/"+comment.getId();
                }).collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    //GETTERS & SETTERS

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
