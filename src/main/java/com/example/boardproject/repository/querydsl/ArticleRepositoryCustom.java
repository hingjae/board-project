package com.example.boardproject.repository.querydsl;

import java.util.List;

//DB에서 도메인 전체를 가져오는 게 아닌 부분적으로 가져오기 때문에 querydsl사용
public interface ArticleRepositoryCustom {

    List<String> findAllDistinctHashtags();
}
