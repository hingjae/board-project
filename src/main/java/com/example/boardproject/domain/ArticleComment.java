package com.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}) // 검색을 위해 인덱스 작업을 해줌. 댓글은 본문(content) 용량이 적어서 인덱스를 걸어줌.
@Entity
public class ArticleComment extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // optional = false -> 필수값 article과 필수적으로 관계를 맺음. cascade == none
    @Setter @ManyToOne(optional = false) private Article article;
    @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount;

    @Setter
    @Column(updatable = false) // 부모댓글이 바뀌지 않음
    private Long parentCommentId; // 부모 댓글 ID

    @ToString.Exclude
    @OrderBy("createdAt ASC ")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL) //부모댓글이 지워지면 자식댓글도 지워짐.
    private Set<ArticleComment> childComments = new LinkedHashSet<>();

    @Setter @Column(nullable = false, length = 500) private String content;

    protected ArticleComment() {
    }

    private ArticleComment(Article article, UserAccount userAccount, Long parentCommentId, String content) {
        this.article = article;
        this.userAccount = userAccount;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new ArticleComment(article, userAccount, null, content);
    }

    public void addChildComment(ArticleComment child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
