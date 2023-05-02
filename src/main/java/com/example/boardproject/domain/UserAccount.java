package com.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class UserAccount extends AuditingFields{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userId;
    private String userPassword;
    private String email;
    private String nickname;
    private String memo;

    protected UserAccount() {}

    public static UserAccount of(String userId, String password, String email, String nickname, String memo) {
        return new UserAccount(userId, password, email, nickname, memo);
    }

    public UserAccount(String userId, String password, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = password;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }
}
