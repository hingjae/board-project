package com.example.boardproject.controller;

import com.example.boardproject.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@Import(SecurityConfig.class)
@WebMvcTest(MainController.class) //컴포넌트 스캔을 최소화 해주기 위함.
class MainControllerTest {

    private final MockMvc mvc;

    //test에서 생성자 주입을 할 땐 Autowired를 꼭 걸어주기
    public MainControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("/에 GET요청을 보낼 시 /articles로 redirect")
    @Test
    void givenRootPath_whenRequestRootPage_thenRedirectToArticlesPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}