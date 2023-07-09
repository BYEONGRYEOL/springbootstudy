package com.example.full.repository.member;

import com.example.full.entity.member.Role;
import com.example.full.entity.member.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleCustomRepository {
    //JpaRepository에서 Query Creation할 메서드들 선언
    Optional<Role> findByRoleType(RoleType roleType);
}
