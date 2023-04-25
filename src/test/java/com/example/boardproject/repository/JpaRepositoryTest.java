package com.example.boardproject.repository;

import com.example.boardproject.config.JpaConfig;
import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.ArticleComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("testdb")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // .yml에 설정되어있는 있는 테스트db 사용
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // 오디팅
@DataJpaTest //메서드 마다 트렌젝션을 걸어줌 (실행->롤백)그냥 SpringBootTest로 돌려버리면 안되나?
//@SpringBootTest
//@Transactional
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    //그냥 필드주입 하면 안돼?
    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void given_when_then() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleComment> articleComments = articleCommentRepository.findAll();
        assertThat(articles).isNotNull().hasSize(100);
        assertThat(articleComments).isNotNull().hasSize(300);
    }

    @DisplayName("insert test")
    @Test
    void 기본데이터에하나추가() {
        Article article = Article.of("honey", "test_data", "helloWorld");
        ArticleComment articleComment = ArticleComment.of(article, "test_data");
        articleRepository.save(article);
        articleCommentRepository.save(articleComment);
        long articleSize = articleRepository.count();
        long articleCommentSize = articleCommentRepository.count();
        assertThat(articleSize).isEqualTo(101);
        assertThat(articleCommentSize).isEqualTo(301);
    }

    @DisplayName("update test")
    @Test
    void giventTestData_whenUpdating() {
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#honey";
        article.setHashtag(updatedHashtag);

        Article savedArticle = articleRepository.saveAndFlush(article); // update쿼리 확인
        //테스트코드의 트렌젝션은 메서드가 끝난뒤 롤백되기 때문에 DB에서 확인할 수 없다.

        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete test")
    @Test
    void delete() {
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        articleRepository.delete(article);


        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
    }

}