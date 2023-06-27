package com.example.full.service;

import com.example.full.entity.JPAEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.full.repository.EntityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntityService {
    private final EntityRepository entityRepository;

    public List<JPAEntity> getEntityService(String name){
        if(name.isBlank())
            return entityRepository.findAll();
        else return entityRepository.findAllBymemoText(name);
    }

    public boolean createEntityService(JPAEntity jpaEntity){
        entityRepository.save(jpaEntity);
        return true;
    }
}
