package com.example.full.entity.member;

import com.example.full.entity.common.EntityDate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;


    private String password;
    @Column(nullable = false, length=20)
    private String username;

    @Column(nullable = false, length=20)
    private String nickname;

    private Set<MemberRole> roles;
    public Member(String email, String password, String username,String nickname, List<Role> roles){
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles.stream().map(role -> new MemberRole(this, r)).collect(Collectors.toSet());

    }
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
}
