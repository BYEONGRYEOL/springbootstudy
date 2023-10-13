package com.example.full.controller;

import com.example.full.service.EntityService;
import com.example.full.entity.JPAEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntityController {
    private final EntityService entityService;

    @GetMapping(value = "/entities")
    public List<JPAEntity> getEntities(
            @RequestParam(required = false, defaultValue = "") String name
    ){
        return entityService.getEntityService(name);
    }

    @PostMapping(value = "/entity")
    public boolean createEntity(
            @RequestBody JPAEntity jpaEntity
    ) {
      return entityService.createEntityService(jpaEntity);
    }
}
