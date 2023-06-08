package com.example.boardproject.controller;

import com.example.boardproject.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
//@WebMvcTest // controller와 연관된 내용만 import함. (date rest는 import하지 않음.) -> 에러.
@DisplayName("data rest 테스트 - 스프링 통합 테스트")
@Transactional // 스프링 통합테스트를 할 땐 roll back 하기
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@SpringBootTest
public class DataRestTest {

    @Autowired
    MockMvc mvc;

    @DisplayName("[api] 게시글 조회")
    @Test
    void givenNothing_whenRequestArticles_thenJsonResponse() throws Exception {
        mvc.perform(get("/api/articles"))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestArticleCommentsFromArticle_thenJsonResponse() throws Exception {
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestArticleCommentsId_thenJsonResponse() throws Exception {
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 유저 관련 api는 지원하지 않음")
    @Test
    void givenNothing_whenRequest_thenThrowException() throws Exception {
        mvc.perform(get("/api/userAccounts"))
                .andExpect(status().is4xxClientError());

        mvc.perform(post("/api/userAccounts"))
                .andExpect(status().is4xxClientError());

        mvc.perform(put("/api/userAccounts"))
                .andExpect(status().is4xxClientError());
    }
}
