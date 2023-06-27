package com.example.full;

import com.example.full.entity.JPAEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import com.example.full.repository.EntityRepository;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@Service
@SpringBootTest
public class RepositoryTest {

    public Logger log = FullApplication.log;
    @Autowired
    EntityRepository entityRepository;

    @Test
    public void InsertDummies(){
        IntStream.rangeClosed(1,10).forEach(i->{
            JPAEntity jpaEntity = JPAEntity.builder()
                    .memoText("asdf" + i%2 )
                    .build();

            entityRepository.save(jpaEntity);
        });
    }
    @Test
    public void findAllTest(){
        List<JPAEntity> entityList = entityRepository.findAll();
        for(JPAEntity e : entityList){
            log.info("[Findall]:" + e.getId() +"|||" + e.getMemoText());
        }
    }
}
