package com.example.boardproject.dto.response;

import com.example.boardproject.dto.ArticleCommentDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Long parentCommentId,
        Set<ArticleCommentResponse> childComments
){

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname, String userId) {
        return ArticleCommentResponse.of(id, content, createdAt, email, nickname, userId, null);
    }

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname, String userId, Long parentCommentId) {
        return new ArticleCommentResponse(
                id, content, createdAt, email, nickname, userId, parentCommentId, new LinkedHashSet<>()
        );
        //new HashSet<>(new TreeSet<>(childCommentComparator))
        //new LinkedHashSet<>(new TreeSet<>(childCommentComparator))
        //new TreeSet<>(childCommentComparator)
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return ArticleCommentResponse.of(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                dto.parentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }

}