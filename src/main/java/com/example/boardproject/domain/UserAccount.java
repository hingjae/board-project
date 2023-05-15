package com.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString(callSuper = true) // 부모 클래스인 오디팅 클래스의 필드도 ToString에 포함.
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
        return new UserAccount(userId, password, email, nickname, memo, null);
    }

    public static UserAccount of(String userId, String password, String email, String nickname, String memo, String createdBy) {
        return new UserAccount(userId, password, email, nickname, memo, createdBy);
    }

    private UserAccount(String userId, String password, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.userPassword = password;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
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
