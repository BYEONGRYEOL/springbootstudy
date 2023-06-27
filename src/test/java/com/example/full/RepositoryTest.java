package com.example.full;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    EntityRepository entityRepository;

    @Test
    public void InsertDummies(){
        IntStream.rangeClosed(1,10).forEach(i->{
            JPAEntity jpaEntity = JPAEntity.builder()
                    .memoText("asdfasdf"+i)
                    .build();

            entityRepository.save(jpaEntity);
        });
    }
}a
