package org.vinevweb.cardiohristov.domain.entities;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article extends BaseEntity {


    private String title;

    private String content;

    private LocalDateTime writtenOn;

    private User author;

    private Set<Comment> comments;

    private String pictureUrl;

    public Article() {
        this.setComments(new HashSet<>());
    }


    @Column(name = "content", columnDefinition = "TEXT" , nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(name = "author_id", nullable = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "written_on", nullable = false)
    public LocalDateTime getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(LocalDateTime writtenOn) {
        this.writtenOn = writtenOn;
    }

    @Column(name = "title" , nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @OneToMany(targetEntity = Comment.class, mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    @Column(name = "image_url", nullable = true)
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
