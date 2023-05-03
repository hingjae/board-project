package com.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(indexes = {
        @Index(columnList = "userId", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50) private String userId;
    @Column(nullable = false) private String userPassword;
    @Column(length = 100) private String email;
    @Column(length = 100) private String nickname;
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
