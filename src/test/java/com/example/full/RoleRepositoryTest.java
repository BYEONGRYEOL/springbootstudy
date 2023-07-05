package com.example.full;

import com.example.full.entity.member.Role;
import com.example.full.entity.member.RoleType;
import com.example.full.exception.RoleNotFoundException;
import com.example.full.repository.member.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    private void clear() {
        em.flush();
        em.clear();
    }


    @Test
    void createAndReadTest() {
        //given
        Role role = new Role(RoleType.ROLE_NORMAL);
        clear();
        //when
        roleRepository.save(role);
        //then
        Role foundRole = roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new);

        assertThat(foundRole.getId()).isEqualTo(role.getId());
    }

    @Test
    void deleteTest() {
        //given
        Role role = roleRepository.save(new Role(RoleType.ROLE_NORMAL));
        clear();
        //when
        roleRepository.delete(role);
        //then
        assertThatThrownBy(() -> roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new))
                .isInstanceOf(RoleNotFoundException.class);

    }
    @Test
    void uniqueRoleTypeTest(){
        //given
        Role role =new Role(RoleType.ROLE_NORMAL);
        roleRepository.save(role);
        clear();

        Role duplicateRole = new Role(RoleType.ROLE_NORMAL);
        //when
        //then
        assertThatThrownBy(()->
            roleRepository.save(duplicateRole)
        ).isInstanceOf(DataIntegrityViolationException.class);

    }
}
