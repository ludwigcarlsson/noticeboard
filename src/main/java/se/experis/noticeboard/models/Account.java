package se.experis.noticeboard.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
        generator =
                ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonGetter("notices")
    private List<String> notices() {
        return notices.stream()
                .map(notice -> {
                    return "/api/v1/notices/"+notice.getId();
                }).collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<Notice> notices = new HashSet<>();

    @JsonGetter("comments")
    private List<String> comments() {
        return comments.stream()
                .map(comment -> {
                    return "/api/v1/notices/"+comment.getNotice().getId()+"/comments/"+comment.getId();
                }).collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();


    // GETTERS & SETTERS

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Notice> getNotices() {
        return notices;
    }

    public void setNotices(Set<Notice> notices) {
        this.notices = notices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
