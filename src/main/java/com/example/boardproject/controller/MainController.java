package com.example.boardproject.controller;

import com.example.boardproject.dto.response.ArticleCommentResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "forward:/articles";
    }

    @ResponseBody
    @GetMapping("/test-rest")
    public ArticleCommentResponse test(Long id) {
        return ArticleCommentResponse.of(
                id,
                "content",
                LocalDateTime.now(),
                "email@email.com",
                "honey",
                "honey"
        );
    }
}
