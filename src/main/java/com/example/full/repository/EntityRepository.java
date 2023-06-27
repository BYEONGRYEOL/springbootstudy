package com.example.full.repository;

import com.example.full.entity.JPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EntityRepository extends JpaRepository<JPAEntity, Long> { // 기본 키 타입
    // 자동으로 spring의 빈을 ㅗ등록된다.

    List<JPAEntity> findAllBymemoText(String name);
}
