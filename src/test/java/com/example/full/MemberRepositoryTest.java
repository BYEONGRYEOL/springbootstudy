package com.example.full;

import com.example.full.entity.member.Member;
import com.example.full.exception.MemberNotFoundException;
import com.example.full.repository.member.MemberRepository;
import com.example.full.repository.member.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    private void clear(){
        em.flush();
        em.clear();
    }
    @Test
    void createAndReadTest(){
        //given
        Member member = createMember();

        //when
        memberRepository.save(member);
        clear();

        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest(){
        //given
        Member member = createMember();
        //when
        memberRepository.save(member);
        clear();
        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);

        assertThat(foundMember.getCreatedDate()).isNotNull();
        assertThat(foundMember.getModifiedDate()).isNotNull();
        assertThat(foundMember.getCreatedDate()).isEqualTo(foundMember.getModifiedDate());
    }

    @Test
    void updateTest(){
        //given
        String updatedNickname = "updated";
        Member member = memberRepository.save(createMember());
        clear();
        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        foundMember.updateNickname(updatedNickname);
        clear();
        //then
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(updatedMember.getNickname()).isEqualTo(updatedNickname);
    }

    private Member createMember(){
        return new Member("sbl1998@naver.com", "asdf", "서병렬", "지수조아", emptyList());
    }
}
