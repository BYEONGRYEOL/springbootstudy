package com.example.full;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<JPAEntity, Long> { // 기본 키 타입
    // 자동으로 spring의 빈을 ㅗ등록된다.

}
