package com.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields{

    @Id
    @Column(length = 50) private String userId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }
}
