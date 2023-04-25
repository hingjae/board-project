package com.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;


@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}) // 검색을 위해 인덱스 작업을 해줌. 본문검색은 용량이 너무 커서 지원하지 않음.
@Entity
public class Article extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql id 생성전략 == identity
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;
    @Setter
    @Column(nullable = false, length = 10000)
    private String content;

    @Setter
    private String hashtag;

    //cascade => article이 사라지면 연관관계의 댓글도 사라짐.
    @OrderBy("id")  // 정렬기준 id
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // mappedby안해주면 article_comment를 합쳐서 새로운 테이블을 만듦. 영속성전파
    @ToString.Exclude //무한루프 방지
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    //jpa는 기본생성자 필수
    protected Article() {
    }

    // 식별자는 이미 auto로 되어있음 , 메타데이터는 생성자에 포함하지 않음.
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    //팩토리 메서드
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag); //생성자 메서드 사용
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return getId() != null && getId().equals(article.getId());
    }
    //식별자가 부여되지 않으면 false, 객체 전체가 아닌 식별자를 보고 동등성 검사를 진행. (효율성 증가)

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
