package com.example.boardproject.dto.request;

import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.HashtagDto;
import com.example.boardproject.dto.UserAccountDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String content
) {

    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(
                userAccountDto, title, content, hashtagDtos
        );

    }
}
