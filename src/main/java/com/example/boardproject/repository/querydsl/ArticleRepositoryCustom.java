package com.example.boardproject.repository.querydsl;

import com.example.boardproject.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

//DB에서 도메인 전체를 가져오는 게 아닌 부분적으로 가져오기 때문에 querydsl사용
public interface ArticleRepositoryCustom {

    /**
     * @deprecated 해시태그 도메인을 새로 만들었으므로 이 코드는 더 이상 사용할 필요 없다.
     */
    @Deprecated
    List<String> findAllDistinctHashtags();
    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
