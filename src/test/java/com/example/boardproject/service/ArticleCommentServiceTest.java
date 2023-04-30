package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.dto.ArticleCommentDto;
import com.example.boardproject.repository.ArticleCommentRepository;
import com.example.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("게시글 Id로 조회하면 게시글의 댓글리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchComments_thenReturnsComments() {
        //given
        Long articleId = 1L;
        UserAccount userAccount = UserAccount.of("honey", "password", "email", "nickname", "memo");
        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of(userAccount, "title", "content", "hashtag"))
        );
        //when
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
        //then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 정보를 입력하면 댓글을 저장한다.")
    @Test
    void given_when_then() {
        //given
        Long articleId = 1L;
        UserAccount userAccount = UserAccount.of("honey", "password", "email", "nickname", "memo");
        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of(userAccount, "title", "content", "hashtag"))
        );
        //when
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
        //then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }
}