package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.ArticleUpdateDto;
import com.example.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut; // 테스트 대상
    @Mock
    private ArticleRepository articleRepository; // repository -> service로 주입


    @DisplayName("게시글을 검색하면, 게스글 페이지 반환한다.")
    @Test
    void givenSearchParam_whenSearchingArticles_thenReturnArticles() {

        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");//제목, 본문, 아이디, 해시태그

        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnArticle() {

        Article article = sut.searchArticle(2L);//제목, 본문, 아이디, 해시태그

        assertThat(article).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면 게시글을 생성함.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        //Given
        given(articleRepository.save(any(Article.class))).willReturn(null);
        //when
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "honey", "title", "content", "hashtag"));

        //then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenSavingArticle_thenUpdateArticle() {
        //Given
        given(articleRepository.save(any(Article.class))).willReturn(null);
        //when
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "hashtag"));
        //then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenThenArticle_thenDeletesArticle() {
        //Given
        willDoNothing().given(articleRepository).delete(any(Article.class));
        //when
        sut.deleteArticle(1L);
        //then
        then(articleRepository).should().delete(any(Article.class));
    }
}