package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.domain.constant.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.ArticleWithCommentsDto;
import com.example.boardproject.dto.UserAccountDto;
import com.example.boardproject.repository.ArticleRepository;
import com.example.boardproject.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut; // 테스트 대상
    @Mock
    private ArticleRepository articleRepository; // repository -> service로 주입
    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("게시글 없이 검색하는 경우")
    @Test
    void givenNoSearchParam_whenSearchArticles_thenReturnsArticlesPage() {

        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);

    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenSearchParam_whenSearchArticles_thenReturnsArticlesPage() {
        //given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());
        //when
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        //then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("게시글을 id로 조회하면 해당 게시글을 댓글과 함께 반환한다.")
    @Test
    void givenArticleId_whenFindById_thenReturnArticleWithComment() {
        Long articleId = 1L;
        Article article = createArticle(articleId);
        articleRepository.save(article);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("검석어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnEmptyPage() {
        Pageable pageable = Pageable.ofSize(20);

        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);

        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("게시글을 해시태그로 검색하면, 해당 게시글페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticlesViaHashtag_thenReturnPage() {
        String hashtag = "#Blue";
        Pageable pageable = Pageable.ofSize(20);

        given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));

        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtag, pageable);

        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtag(hashtag, pageable);
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // Given
        List<String> expectedHashtags = List.of("#java", "#spring", "#boot");
        given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

        // When
        List<String> actualHashtags = sut.getHashtags();

        // Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(articleRepository).should().findAllDistinctHashtags();
    }



    private Article createArticle() {
        return createArticle(1L);
    }

    private Article createArticle(Long articleId) {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content",
                "hashtag"
        );
        return article;
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());

        sut.saveArticle(dto);

        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 수정 정보를 입력하면 게시글을 수정한다")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdateArticle() {
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
        sut.updateArticle(dto.id(), dto);
        then(articleRepository).should().getReferenceById(dto.id());

    }

    @DisplayName("없는 게시글에 수정정보를 입력하면, 경고로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonExistArticle_whenUpdatingArticle_thenLogsWarning() {
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        //when
        sut.updateArticle(dto.id(), dto);

        then(articleRepository).should().getReferenceById(dto.id());
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content");
    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                createUserAccountDto(),
                title,
                content,
                "hashtag"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "honey",
                "password",
                "honey@email.com",
                "honey",
                "memo",
                LocalDateTime.now(),
                "honey",
                LocalDateTime.now(),
                "honey"
        );
    }

    private UserAccount createUserAccount() {
        return createUserAccount("honey");
    }

    private UserAccount createUserAccount(String userId) {
        return UserAccount.of(
                userId,
                "password",
                "honey@email.com",
                "honey",
                "memo"
        );
    }


}