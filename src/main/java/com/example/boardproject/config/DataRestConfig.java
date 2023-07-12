package com.example.boardproject.config;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.ArticleComment;
import com.example.boardproject.domain.Hashtag;
import com.example.boardproject.domain.UserAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class DataRestConfig {

    // data rest 기본 설정은 id를 감추는 것인데,
    // 회원 계정에 한해서 `userId`가 노출되게끔 해줘야 함
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig((config, cors) ->
                config
                        .exposeIdsFor(UserAccount.class)
                        .exposeIdsFor(Article.class)
                        .exposeIdsFor(ArticleComment.class)
                        .exposeIdsFor(Hashtag.class)

        );
    }

}